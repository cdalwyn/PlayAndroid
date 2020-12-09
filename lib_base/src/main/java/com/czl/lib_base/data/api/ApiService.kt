package com.czl.lib_base.data.api

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.bean.*
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
    fun getCollectArticle(@Path("page") page: String): Observable<BaseBean<CollectArticleBean>>

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
    fun getHomeProject(@Path("page") page: String): Observable<BaseBean<ProjectBean>>

    /**
     * 收藏
     */
    @POST("lg/collect/{articleId}/json")
    fun collectArticle(@Path("articleId") articleId: Int): Observable<BaseBean<Any?>>

    /**
     * 取消收藏-1 文章列表
     */
    @POST("lg/uncollect_originId/{articleId}/json")
    fun unCollectArticle(@Path("articleId") articleId: Int): Observable<BaseBean<Any?>>

    /**
     * 取消收藏-2 我的收藏
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun unCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int
    ): Observable<BaseBean<Any?>>

    /**
     * 收藏网址
     */
    @POST("lg/collect/addtool/json")
    @FormUrlEncoded
    fun collectWebsite(
        @Field("name") name: String,
        @Field("link") link: String
    ): Observable<BaseBean<Any?>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun searchByKeyword(
        @Path("page") page: String,
        @Field("k") keyword: String
    ): Observable<BaseBean<SearchDataBean>>

    /**
     * 退出登录
     */
    @GET("user/logout/json")
    fun logout(): Observable<BaseBean<Any?>>

    /**
     * 注册
     */
    @POST("user/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): Observable<BaseBean<Any?>>

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    fun getSearchHotKey(): Observable<BaseBean<List<SearchHotKeyBean>>>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    fun getProjectSort(): Observable<BaseBean<List<ProjectSortBean>>>

    /**
     * 根据分类id获取项目列表
     */
    @GET("project/list/{page}/json")
    fun getProjectByCid(
        @Path("page") page: String,
        @Query("cid") cid: String
    ): Observable<BaseBean<ProjectBean>>

    /**
     * 获取个人积分
     */
    @GET("lg/coin/userinfo/json")
    fun getUserScore(): Observable<BaseBean<UserScoreBean>>

    /**
     * 获取个人分享
     */
    @GET("user/lg/private_articles/{page}/json")
    fun getUserShareData(@Path("page") page: String): Observable<BaseBean<UserShareBean>>

    /**
     * 获取个人积分详情
     */
    @GET("lg/coin/list/{page}/json")
    fun getUserScoreDetail(@Path("page") page: String): Observable<BaseBean<UserScoreDetailBean>>

    /**
     * 获取积分排行榜
     */
    @GET("coin/rank/{page}/json")
    fun getScoreRank(@Path("page") page: String): Observable<BaseBean<UserRankBean>>

    /**
     * 获取用户收藏网站
     */
    @GET("lg/collect/usertools/json")
    fun getUserCollectWebsite(): Observable<BaseBean<List<CollectWebsiteBean>>>

    /**
     * 删除用户收藏网站
     */
    @POST("lg/collect/deletetool/json")
    @FormUrlEncoded
    fun deleteUserCollectWeb(@Field("id") id: String): Observable<BaseBean<Any?>>

    /**
     * 获取广场列表
     */
    @GET("user_article/list/{page}/json")
    fun getSquareList(@Path("page") page: Int): Observable<BaseBean<SquareListBean>>

    /**
     * 获取体系列表
     */
    @GET("tree/json")
    fun getSystemTreeData(): Observable<BaseBean<List<SystemTreeBean>>>

    /**
     * 获取导航列表
     */
    @GET("navi/json")
    fun getNavData(): Observable<BaseBean<List<NavigationBean>>>

    /**
     * 根据体系Cid查找所有文章
     */
    @GET("article/list/{page}/json")
    fun getArticlesByCid(
        @Path("page") page: Int,
        @Query("cid") cid: String
    ): Observable<BaseBean<SystemDetailBean>>

    /**
     * 分享文章
     */
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    fun shareArticleToSquare(
        @Field("title") title: String,
        @Field("link") link: String
    ): Observable<BaseBean<Any?>>

    /**
     * 删除文章
     */
    @POST("lg/user_article/delete/{id}/json")
    fun deleteArticleById(@Path("id") id: Int):Observable<BaseBean<Any?>>
}