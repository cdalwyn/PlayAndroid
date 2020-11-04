package com.czl.lib_base.data.api

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.entity.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface ApiService {

    /**
     * 登录
     */
    @POST("user/login")
    @FormUrlEncoded
    fun pwdLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<BaseBean<UserBean>>

    @GET("article/list/{page}/json")
    fun getMainArticle(@Path("page") page: String): Observable<BaseBean<ArticleBean>>

    /**
     * 我的收藏列表
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectArticle(@Path("page") page: String): Observable<BaseBean<CollectArticle>>

    /**
     * 首页轮播图
     */
    @GET("banner/json")
    fun getBannerData(): Observable<BaseBean<List<HomeBannerBean>>>

    /**
     * 首页热门博文列表
     */
    @GET("article/list/{page}/json")
    fun getHomeArticle(@Path("page") page: String): Observable<BaseBean<HomeArticleBean>>

    /**
     * 首页热门项目列表
     */
    @GET("article/listproject/{page}/json")
    fun getHomeProject(@Path("page") page: String): Observable<BaseBean<HomeProjectBean>>

    /**
     * 收藏
     */
    @POST("lg/collect/{articleId}/json")
    fun collectArticle(@Path("articleId") articleId: Int): Observable<BaseBean<Any?>>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{articleId}/json")
    fun unCollectArticle(@Path("articleId") articleId: Int): Observable<BaseBean<Any?>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun searchByKeyword(
        @Path("page") page: String,
        @Field("k") keyword: String
    ): Observable<BaseBean<SearchDataBean>>

    @GET("user/logout/json")
    fun logout(): Observable<BaseBean<Any?>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ):Observable<BaseBean<Any?>>
}