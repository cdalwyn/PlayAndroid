package com.czl.lib_base.data.source.impl

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.data.db.SearchHistoryEntity
import com.czl.lib_base.data.db.UserEntity
import com.czl.lib_base.data.source.LocalDataSource
import com.czl.lib_base.util.SpUtils
import com.google.gson.reflect.TypeToken
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
import org.litepal.extension.deleteAll
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
        return SpUtils.decodeString(AppConstants.SpKey.LOGIN_NAME)
    }

    override fun saveUserData(userBean: UserBean) {
        SpUtils.encode(AppConstants.SpKey.USER_ID, userBean.id)
        SpUtils.encode(AppConstants.SpKey.LOGIN_NAME, userBean.publicName)
        SpUtils.encode(
            AppConstants.SpKey.USER_JSON_DATA,
            GsonUtils.toJson(userBean, object : TypeToken<UserBean>() {}.type)
        )
    }

    override fun getUserData(): UserBean? {
        val userJsonData = SpUtils.decodeString(AppConstants.SpKey.USER_JSON_DATA)
        return if (userJsonData.isNullOrBlank())
            return null
        else GsonUtils.fromJson(
            userJsonData,
            object : TypeToken<UserBean>() {}.type
        )
    }

    override fun getUserId(): Int {
        return SpUtils.decodeInt(AppConstants.SpKey.USER_ID)
    }

    override fun clearLoginState() {
        SpUtils.clearAll()
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
                entity.getAllHistory().forEach { history ->
                    if (history.history == keyword) {
                        history.delete()
                    }
                }
            }
            val user = UserEntity(getUserId(), getLoginName())
            val searchHistoryEntity =
                SearchHistoryEntity(keyword, System.currentTimeMillis(), user)
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
}