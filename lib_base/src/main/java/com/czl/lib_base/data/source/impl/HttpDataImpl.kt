package com.czl.lib_base.data.source.impl

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.api.ApiService
import com.czl.lib_base.data.source.HttpDataSource
import com.czl.lib_base.data.entity.ArticleBean
import com.czl.lib_base.data.entity.CollectArticle
import com.czl.lib_base.data.entity.UserBean
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

}