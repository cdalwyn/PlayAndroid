package com.czl.lib_base.data.source.impl

import android.annotation.SuppressLint
import com.blankj.utilcode.util.CacheDiskUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.*
import com.czl.lib_base.data.db.SearchHistoryEntity
import com.czl.lib_base.data.db.UserEntity
import com.czl.lib_base.data.db.WebHistoryEntity
import com.czl.lib_base.data.source.LocalDataSource
import com.czl.lib_base.util.SpHelper
import com.google.gson.reflect.TypeToken
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
import org.litepal.extension.findFirst
import org.litepal.extension.runInTransaction
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

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

    override fun getLoginName(): String {
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
        return if (userJsonData.isBlank())
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
            it.onNext(LitePal.runInTransaction {
                var user = LitePal.where("uid = ?", getUserId().toString())
                    .findFirst<UserEntity>(isEager = true)
                if (user == null) {
                    user = UserEntity(getUserId(), getLoginName())
                }
                val searchHistoryEntity =
                    SearchHistoryEntity(keyword, Date(), user)
                user.historyEntities.add(searchHistoryEntity)
                user.saveOrUpdate("uid =?", user.uid.toString())
                        && searchHistoryEntity.saveOrUpdate(
                    "history = ? and userentity_id = ?",
                    keyword,
                    user.id.toString()
                )
            })
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

    override fun deleteAllSearchHistory(): Observable<Int> {
        return Observable.create { emitter ->
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            emitter.onNext(
                LitePal.deleteAll(
                    SearchHistoryEntity::class.java,
                    "userentity_id=?",
                    entity?.id.toString()
                )
            )
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
                LitePal.runInTransaction {
                    var userEntity = LitePal.where("uid = ?", getUserId().toString())
                        .findFirst<UserEntity>(isEager = true)
                    if (userEntity == null) {
                        userEntity = UserEntity(getUserId(), getLoginName())
                    }

                    val webHistoryEntity =
                        WebHistoryEntity(
                            title,
                            link,
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.getDefault()
                            ).format(Date()),
                            userEntity
                        )
                    userEntity.browseEntities.add(webHistoryEntity)
                    webHistoryEntity.saveOrUpdate(
                        "userentity_id = ? and weblink = ?",
                        userEntity.id.toString(),
                        link
                    ) && userEntity.saveOrUpdate("uid =?", userEntity.uid.toString())
                }
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
        return Observable.create { emitter ->
            val entity =
                LitePal.where("uid=?", getUserId().toString()).findFirst<UserEntity>()
            emitter.onNext(
                LitePal.deleteAll(
                    WebHistoryEntity::class.java,
                    "userentity_id=?",
                    entity?.id.toString()
                )
            )
        }
    }

    override fun saveFollowSysModeFlag(isFollow: Boolean) {
        SpHelper.encode(AppConstants.SpKey.SYS_UI_MODE, isFollow)
    }

    override fun getFollowSysUiModeFlag(): Boolean {
        return SpHelper.decodeBoolean(AppConstants.SpKey.SYS_UI_MODE)
    }

    override fun saveUiMode(nightModeFlag: Boolean) {
        SpHelper.encode(AppConstants.SpKey.USER_UI_MODE, nightModeFlag)
    }

    override fun getUiMode(): Boolean {
        return SpHelper.decodeBoolean(AppConstants.SpKey.USER_UI_MODE)
    }

    override fun saveReadHistoryState(visible: Boolean) {
        SpHelper.encode(AppConstants.SpKey.READ_HISTORY_STATE, visible)
    }

    override fun getReadHistoryState(): Boolean {
        return SpHelper.decodeBoolean(AppConstants.SpKey.READ_HISTORY_STATE, true)
    }

    override fun <T : Serializable> saveCacheListData(list: List<T>) {
        if (list.isNotEmpty()) {
            when (list[0]) {
                is HomeBannerBean -> {
                    CacheDiskUtils.getInstance().put(
                        AppConstants.CacheKey.CACHE_HOME_BANNER,
                        HomeBannerCache(list as List<HomeBannerBean>),
                        AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS
                    )
                }
                is HomeArticleBean.Data -> {
                    CacheDiskUtils.getInstance().put(
                        AppConstants.CacheKey.CACHE_HOME_ARTICLE,
                        HomeArticleCache(list as List<HomeArticleBean.Data>),
                        AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS
                    )
                }
                is SearchHotKeyBean -> {
                    CacheDiskUtils.getInstance().put(
                        AppConstants.CacheKey.CACHE_HOME_KEYWORD,
                        HomeSearchKeywordCache(list as List<SearchHotKeyBean>),
                        AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS
                    )
                }
                is SquareListBean.Data -> {
                    CacheDiskUtils.getInstance().put(
                        AppConstants.CacheKey.CACHE_SQUARE_LIST,
                        HomeSquareCache(list as List<SquareListBean.Data>),
                        AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS
                    )
                }
                is ProjectSortBean -> {
                    CacheDiskUtils.getInstance().put(
                        AppConstants.CacheKey.CACHE_PROJECT_SORT,
                        HomeProjectSortCache(list as List<ProjectSortBean>),
                        AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS
                    )
                }
                is ProjectBean.Data -> {
                    CacheDiskUtils.getInstance().put(
                        AppConstants.CacheKey.CACHE_PROJECT_CONTENT,
                        HomeProjectContentCache(list as List<ProjectBean.Data>),
                        AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS
                    )
                }
            }
        }
    }

    override fun <T : Serializable> getCacheListData(key: String): List<T>? {
        return when (val serializable = CacheDiskUtils.getInstance().getSerializable(key)) {
            is HomeBannerCache? -> {
                serializable?.homeBannerBeans as List<T>?
            }
            is HomeArticleCache? -> {
                serializable?.homeArticleBeans as List<T>?
            }
            is HomeSearchKeywordCache? -> {
                serializable?.searchHotKeyBean as List<T>?
            }
            is HomeSquareCache? -> {
                serializable?.squareCache as List<T>?
            }
            is HomeProjectSortCache? -> {
                serializable?.sortCache as List<T>?
            }
            is HomeProjectContentCache? -> {
                serializable?.contentCache as List<T>?
            }
            else -> emptyList()
        }
    }


}