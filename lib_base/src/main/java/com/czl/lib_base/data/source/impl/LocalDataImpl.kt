package com.czl.lib_base.data.source.impl

import android.annotation.SuppressLint
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.data.db.SearchHistoryEntity
import com.czl.lib_base.data.db.UserEntity
import com.czl.lib_base.data.db.WebHistoryEntity
import com.czl.lib_base.data.source.LocalDataSource
import com.czl.lib_base.util.SpHelper
import com.google.gson.reflect.TypeToken
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
import org.litepal.extension.findFirst

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
class LocalDataImpl : LocalDataSource {

//    companion object {
//        val INSTANCE: LocalDataImpl by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LocalDataImpl() }
//    }

    override fun getLocalData(): String {

        val data: IntRange = 1..50
        val list: MutableList<Int> = mutableListOf()
        for (i in data) {
            list.add(i)
        }
        return list.toString()
    }

    override fun getLoginName(): String? {
        return SpHelper.decodeString(AppConstants.SpKey.LOGIN_NAME)
    }

    override fun saveUserData(userBean: UserBean) {
        SpHelper.encode(AppConstants.SpKey.USER_ID, userBean.id)
        SpHelper.encode(AppConstants.SpKey.LOGIN_NAME, userBean.publicName)
        SpHelper.encode(
            AppConstants.SpKey.USER_JSON_DATA,
            GsonUtils.toJson(userBean, object : TypeToken<UserBean>() {}.type)
        )
    }

    override fun getUserData(): UserBean? {
        val userJsonData = SpHelper.decodeString(AppConstants.SpKey.USER_JSON_DATA)
        return if (userJsonData.isNullOrBlank())
            return null
        else GsonUtils.fromJson(
            userJsonData,
            object : TypeToken<UserBean>() {}.type
        )
    }

    override fun getUserId(): Int {
        return SpHelper.decodeInt(AppConstants.SpKey.USER_ID)
    }

    override fun clearLoginState() {
        SpHelper.clearAll()
    }

    override fun saveUserSearchHistory(keyword: String): Flowable<Boolean> {
        return Flowable.create({
            // 未登录
            if (getUserId() == 0) {
                it.onComplete()
                return@create
            }
            // 找到当前用户的数据
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            // 遍历查询当前用户的搜索历史是否与现在搜索的内容重复 并删除
            if (entity?.getAllHistory() != null && entity.getAllHistory().isNotEmpty()
            ) {
                entity.getAllHistory().filter { x -> x.history == keyword }
                    .forEach { y -> y.delete() }
            }
            val user = UserEntity(getUserId(), getLoginName())
            val searchHistoryEntity =
                SearchHistoryEntity(keyword, System.currentTimeMillis().toInt(), user)
            user.historyEntities.add(searchHistoryEntity)
            searchHistoryEntity.userEntity = user
            user.saveOrUpdate("uid =?", user.uid.toString())
            it.onNext(searchHistoryEntity.save())
        }, BackpressureStrategy.BUFFER)
    }

    override fun getSearchHistoryByUid(): Flowable<List<SearchHistoryEntity>> {
        return Flowable.create({
            // 未登录
            if (getUserId() == 0) {
                it.onNext(emptyList())
                return@create
            }
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            if (entity == null) {
                it.onComplete()
                return@create
            }
            it.onNext(entity.getRecentHistory())
        }, BackpressureStrategy.BUFFER)
    }


    override fun deleteSearchHistory(history: String): Disposable {
        return Flowable.just(1L)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                // 找到当前用户的数据
                val entity =
                    LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
                entity?.getAllHistory()?.find { it.history == history }?.delete()
            }
    }

    @SuppressLint("CheckResult")
    override fun saveUserBrowseHistory(title: String, link: String) {
        Flowable.just(1)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                if (getUserId() == 0) {
                    return@subscribe
                }
                // 找到当前用户的数据
                val entity =
                    LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
                val allWebHistory = entity?.getAllWebHistory()
                // 遍历去重 并删除
                if (!allWebHistory.isNullOrEmpty()) {
                    allWebHistory.filter { x -> x.webLink == link && x.webTitle == title }
                        .forEach { y -> y.delete() }
                }
                val userEntity = UserEntity(getUserId(), getLoginName())
                val webHistoryEntity =
                    WebHistoryEntity(title, link, System.currentTimeMillis().toInt(), userEntity)
                userEntity.browseEntities.add(webHistoryEntity)
                webHistoryEntity.userEntity = userEntity
                userEntity.saveOrUpdate("uid =?", userEntity.uid.toString())
                webHistoryEntity.save()
            }) {
                it.printStackTrace()
                LogUtils.e("阅读历史保存失败，error=${it.message}")
            }
    }

    override fun getUserBrowseHistoryByUid(): Flowable<List<WebHistoryEntity>> {
        return Flowable.create({
            // 未登录
            if (getUserId() == 0) {
                it.onNext(emptyList())
                return@create
            }
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            if (entity == null) {
                it.onNext(emptyList())
                return@create
            }
            it.onNext(entity.getAllWebHistory())
        }, BackpressureStrategy.BUFFER)
    }

    override fun deleteBrowseHistory(title: String, link: String): Observable<Int> {
        return Observable.create { emitter ->
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            emitter.onNext(
                entity?.getAllWebHistory()?.find { it.webLink == link && it.webTitle == title }
                    ?.delete() ?: 0
            )
        }
    }

    override fun deleteAllWebHistory(): Observable<Int> {
        return Observable.create { emitter->
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            emitter.onNext(LitePal.deleteAll(WebHistoryEntity::class.java,"userentity_id=?",entity?.id.toString()))
        }
    }

    override fun saveFollowSysModeFlag(isFollow: Boolean) {
        SpHelper.encode(AppConstants.SpKey.SYS_UI_MODE,isFollow)
    }

    override fun getFollowSysUiModeFlag(): Boolean {
        return SpHelper.decodeBoolean(AppConstants.SpKey.SYS_UI_MODE)
    }

    override fun saveUiMode(nightModeFlag: Boolean) {
        SpHelper.encode(AppConstants.SpKey.USER_UI_MODE,nightModeFlag)
    }

    override fun getUiMode(): Boolean {
        return SpHelper.decodeBoolean(AppConstants.SpKey.USER_UI_MODE)
    }
}