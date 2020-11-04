package com.czl.lib_base.data.source.impl

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.api.ApiService
import com.czl.lib_base.data.entity.*
import com.czl.lib_base.data.source.HttpDataSource
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
class HttpDataImpl(private val apiService: ApiService) : HttpDataSource {

//    companion object {
//        @Volatile
//        private var INSTANCE: HttpDataImpl? = null
//        fun getInstance(demoApiService: DemoApiService): HttpDataImpl? {
//            if (INSTANCE == null) {
//                synchronized(HttpDataImpl::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = HttpDataImpl(demoApiService)
//                    }
//                }
//            }
//            return INSTANCE
//        }
//    }

    override fun userLogin(account: String, pwd: String): Observable<BaseBean<UserBean>> {
        return apiService.pwdLogin(account,pwd)
    }


    override fun getMainArticle(page: String): Observable<BaseBean<ArticleBean>> {
        return apiService.getMainArticle(page)
    }

    override fun getCollectArticle(page: String): Observable<BaseBean<CollectArticle>> {
        return apiService.getCollectArticle(page)
    }

    override fun getBannerData(): Observable<BaseBean<List<HomeBannerBean>>> {
        return apiService.getBannerData()
    }

    override fun getHomeArticle(page: String): Observable<BaseBean<HomeArticleBean>> {
        return apiService.getHomeArticle(page)
    }

    override fun getHomeProject(page: String): Observable<BaseBean<HomeProjectBean>> {
        return apiService.getHomeProject(page)
    }

    override fun collectArticle(articleId: Int): Observable<BaseBean<Any?>> {
        return apiService.collectArticle(articleId)
    }

    override fun unCollectArticle(articleId: Int): Observable<BaseBean<Any?>> {
        return apiService.unCollectArticle(articleId)
    }

    override fun logout(): Observable<BaseBean<Any?>> {
        return apiService.logout()
    }

    override fun register(
        username: String,
        password: String,
        repassword: String
    ): Observable<BaseBean<Any?>> {
        return apiService.register(username,password,repassword)
    }

}