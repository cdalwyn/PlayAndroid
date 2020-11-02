package com.czl.lib_base.data.source

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.entity.*
import io.reactivex.Observable
import retrofit2.http.Path

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface HttpDataSource {
    fun userLogin(account: String, pwd: String): Observable<BaseBean<UserBean>>
    fun getMainArticle(page: String = "0"): Observable<BaseBean<ArticleBean>>
    fun getCollectArticle(page: String = "0"): Observable<BaseBean<CollectArticle>>
    fun getBannerData(): Observable<BaseBean<List<HomeBannerBean>>>
    fun getHomeArticle(page: String = "0"): Observable<BaseBean<HomeArticleBean>>
    fun getHomeProject(page: String = "0"): Observable<BaseBean<HomeProjectBean>>
    fun collectArticle(articleId: Int): Observable<BaseBean<Any?>>
}