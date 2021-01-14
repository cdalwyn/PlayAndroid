package com.czl.lib_base.data.source

import com.czl.lib_base.annotation.TodoOrder
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.bean.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.Path

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface HttpDataSource {
    fun userLogin(account: String, pwd: String): Observable<BaseBean<UserBean>>
    fun getMainArticle(page: String = "0"): Observable<BaseBean<ArticleBean>>
    fun getCollectArticle(page: String = "0"): Observable<BaseBean<CollectArticleBean>>
    fun getBannerData(): Observable<BaseBean<List<HomeBannerBean>>>
    fun getHomeArticle(page: String = "0"): Observable<BaseBean<HomeArticleBean>>
    fun getHomeProject(page: String = "0"): Observable<BaseBean<ProjectBean>>
    fun collectArticle(articleId: Int): Observable<BaseBean<Any?>>
    fun unCollectArticle(articleId: Int): Observable<BaseBean<Any?>>
    fun logout(): Observable<BaseBean<Any?>>
    fun register(username: String, password: String, repassword: String): Observable<BaseBean<Any?>>
    fun searchByKeyword(page: String = "0", keyword: String): Observable<BaseBean<SearchDataBean>>
    fun getSearchHotKey(): Observable<BaseBean<List<SearchHotKeyBean>>>
    fun getProjectSort(): Observable<BaseBean<List<ProjectSortBean>>>
    fun getProjectByCid(page: String = "1", cid: String): Observable<BaseBean<ProjectBean>>
    fun getUserShareData(page: String = "1"): Observable<BaseBean<UserShareBean>>
    fun getUserScoreDetail(page: String = "1"): Observable<BaseBean<UserScoreDetailBean>>
    fun getUserScore(): Observable<BaseBean<UserScoreBean>>
    fun getScoreRank(page: String = "1"): Observable<BaseBean<UserRankBean>>
    fun getUserCollectWebsite(): Observable<BaseBean<List<CollectWebsiteBean>>>
    fun deleteUserCollectWeb(id: String): Observable<BaseBean<Any?>>
    fun unCollectArticle(id: Int, originId: Int = -1): Observable<BaseBean<Any?>>
    fun collectWebsite(name: String, link: String): Observable<BaseBean<Any?>>
    fun getSquareList(page: Int = 0): Observable<BaseBean<SquareListBean>>
    fun getSystemTreeData(): Observable<BaseBean<List<SystemTreeBean>>>
    fun getNavData(): Observable<BaseBean<List<NavigationBean>>>
    fun getArticlesByCid(page: Int = 0, cid: String): Observable<BaseBean<SystemDetailBean>>
    fun shareArticleToSquare(title: String, link: String): Observable<BaseBean<Any?>>
    fun deleteArticleById(id: Int): Observable<BaseBean<Any?>>
    fun getShareUserDetail(uid: String, page: Int = 1): Observable<BaseBean<ShareUserDetailBean>>
    fun getHomeTopArticle(): Observable<BaseBean<List<HomeArticleBean.Data>>>
    fun getTodoList(status: Int=-1, @TodoType type: Int=0, @TodoPriority priority: Int=0, @TodoOrder orderby: Int=TodoOrder.createDesc, page: Int = 1): Observable<BaseBean<TodoBean>>
    fun addTodo(title: String, content: String, date: String, @TodoType type: Int, priority: Int):Observable<BaseBean<Any?>>
    fun deleteTodo(todoId: Int): Observable<BaseBean<Any?>>
    fun updateTodoState(todoId: Int, status: Int):Observable<BaseBean<Any?>>
    fun updateTodo(todoInfo: TodoBean.Data):Observable<BaseBean<Any?>>
}