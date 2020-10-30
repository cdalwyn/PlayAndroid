package com.czl.lib_base.data

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.entity.*
import com.czl.lib_base.data.source.HttpDataSource
import com.czl.lib_base.data.source.LocalDataSource
import io.reactivex.Observable
import me.goldze.mvvmhabit.base.BaseModel

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description 数据中心（本地+在线） 外部通过Koin依赖注入调用
 * 对于缓存或者在线数据的增删查改统一通过该数据仓库调用
 */
class DataRepository constructor(
    private val mLocalDataSource: LocalDataSource,
    private val mHttpDataSource: HttpDataSource
) : BaseModel(), LocalDataSource, HttpDataSource {

    //    companion object {
//        @Volatile
//        private var INSTANCE: DataRepository? = null
//        fun getInstance(
//            localDataSource: LocalDataSource,
//            httpDataSource: HttpDataSource
//        ): DataRepository? {
//            if (INSTANCE == null) {
//                synchronized(DataRepository::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = DataRepository(localDataSource, httpDataSource)
//                    }
//                }
//            }
//            return INSTANCE
//        }
//    }

    override fun getMainArticle(page: String): Observable<BaseBean<ArticleBean>> {
        return mHttpDataSource.getMainArticle(page)
    }

    override fun getCollectArticle(page: String): Observable<BaseBean<CollectArticle>> {
        return mHttpDataSource.getCollectArticle(page)
    }

    override fun getBannerData(): Observable<BaseBean<List<HomeBannerBean>>> {
        return mHttpDataSource.getBannerData()
    }

    override fun getHomeArticle(page: String): Observable<BaseBean<HomeArticleBean>> {
        return mHttpDataSource.getHomeArticle(page)
    }

    override fun getLocalData(): String {
        return mLocalDataSource.getLocalData()
    }

    override fun getLoginName(): String? {
        return mLocalDataSource.getLoginName()
    }

    override fun saveLoginName(name: String?) {
        mLocalDataSource.saveLoginName(name)
    }

    override fun clearLoginState() {
        mLocalDataSource.clearLoginState()
    }

    override fun userLogin(account: String, pwd: String): Observable<BaseBean<UserBean>> {
        return mHttpDataSource.userLogin(account, pwd)
    }


}