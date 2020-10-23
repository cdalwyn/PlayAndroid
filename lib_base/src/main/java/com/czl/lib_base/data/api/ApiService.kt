package com.czl.lib_base.base.api

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.mvvm.entity.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface ApiService {

    @POST("user/login")
    @FormUrlEncoded
    fun pwdLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<BaseBean<UserBean>>

    @GET("article/list/{page}/json")
    fun getMainArticle(@Path("page") page: String): Observable<BaseBean<ArticleBean>>

    @GET("lg/collect/list/{page}/json")
    fun getCollectArticle(@Path("page") page: String): Observable<BaseBean<CollectArticle>>
}