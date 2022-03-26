package com.czl.lib_base.data

import com.czl.lib_base.annotation.TodoOrder
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseModel
import com.czl.lib_base.data.bean.*
import com.czl.lib_base.data.db.SearchHistoryEntity
import com.czl.lib_base.data.db.WebHistoryEntity
import com.czl.lib_base.data.source.HttpDataSource
import com.czl.lib_base.data.source.LocalDataSource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.Serializable

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

    override fun getCollectArticle(page: String): Observable<BaseBean<CollectArticleBean>> {
        return mHttpDataSource.getCollectArticle(page)
    }

    override fun getBannerData(): Observable<BaseBean<List<HomeBannerBean>>> {
        return mHttpDataSource.getBannerData()
    }

    override fun getHomeArticle(page: String): Observable<BaseBean<HomeArticleBean>> {
        return mHttpDataSource.getHomeArticle(page)
    }

    override fun getHomeProject(page: String): Observable<BaseBean<ProjectBean>> {
        return mHttpDataSource.getHomeProject(page)
    }

    override fun getArticlesByUserName(
        page: Int,
        author: String
    ): Observable<BaseBean<ShareUserDetailBean.ShareArticles>> {
        return mHttpDataSource.getArticlesByUserName(page,author)
    }

    override fun collectArticle(articleId: Int): Observable<BaseBean<Any?>> {
        return mHttpDataSource.collectArticle(articleId)
    }

    override fun unCollectArticle(articleId: Int): Observable<BaseBean<Any?>> {
        return mHttpDataSource.unCollectArticle(articleId)
    }

    override fun unCollectArticle(id: Int, originId: Int): Observable<BaseBean<Any?>> {
        return mHttpDataSource.unCollectArticle(id, originId)
    }

    override fun logout(): Observable<BaseBean<Any?>> {
        return mHttpDataSource.logout()
    }

    override fun register(
        username: String,
        password: String,
        repassword: String
    ): Observable<BaseBean<Any?>> {
        return mHttpDataSource.register(username, password, repassword)
    }

    override fun searchByKeyword(
        page: String,
        keyword: String
    ): Observable<BaseBean<SearchDataBean>> {
        return mHttpDataSource.searchByKeyword(page, keyword)
    }

    override fun getSearchHotKey(): Observable<BaseBean<List<SearchHotKeyBean>>> {
        return mHttpDataSource.getSearchHotKey()
    }

    override fun getProjectSort(): Observable<BaseBean<List<ProjectSortBean>>> {
        return mHttpDataSource.getProjectSort()
    }

    override fun getProjectByCid(page: String, cid: String): Observable<BaseBean<ProjectBean>> {
        return mHttpDataSource.getProjectByCid(page, cid)
    }

    override fun getUserShareData(page: String): Observable<BaseBean<UserShareBean>> {
        return mHttpDataSource.getUserShareData(page)
    }

    override fun getUserScoreDetail(page: String): Observable<BaseBean<UserScoreDetailBean>> {
        return mHttpDataSource.getUserScoreDetail(page)
    }

    override fun getUserScore(): Observable<BaseBean<UserScoreBean>> {
        return mHttpDataSource.getUserScore()
    }

    override fun getScoreRank(page: String): Observable<BaseBean<UserRankBean>> {
        return mHttpDataSource.getScoreRank(page)
    }

    override fun getUserCollectWebsite(): Observable<BaseBean<List<CollectWebsiteBean>>> {
        return mHttpDataSource.getUserCollectWebsite()
    }

    override fun deleteUserCollectWeb(id: String): Observable<BaseBean<Any?>> {
        return mHttpDataSource.deleteUserCollectWeb(id)
    }

    override fun collectWebsite(name: String, link: String): Observable<BaseBean<Any?>> {
        return mHttpDataSource.collectWebsite(name, link)
    }

    override fun getSquareList(page: Int): Observable<BaseBean<SquareListBean>> {
        return mHttpDataSource.getSquareList(page)
    }

    override fun getSystemTreeData(): Observable<BaseBean<List<SystemTreeBean>>> {
        return mHttpDataSource.getSystemTreeData()
    }

    override fun getNavData(): Observable<BaseBean<List<NavigationBean>>> {
        return mHttpDataSource.getNavData()
    }

    override fun getArticlesByCid(page: Int, cid: String): Observable<BaseBean<SystemDetailBean>> {
        return mHttpDataSource.getArticlesByCid(page,cid)
    }

    override fun shareArticleToSquare(title: String, link: String): Observable<BaseBean<Any?>> {
        return mHttpDataSource.shareArticleToSquare(title,link)
    }

    override fun deleteArticleById(id: Int): Observable<BaseBean<Any?>> {
        return mHttpDataSource.deleteArticleById(id)
    }

    override fun getShareUserDetail(
        uid: String,
        page: Int
    ): Observable<BaseBean<ShareUserDetailBean>> {
        return mHttpDataSource.getShareUserDetail(uid,page)
    }

    override fun getHomeTopArticle(): Observable<BaseBean<List<HomeArticleBean.Data>>> {
        return mHttpDataSource.getHomeTopArticle()
    }

    override fun getTodoList(
        status: Int,
        @TodoType type: Int,
        priority: Int,
        @TodoOrder orderby: Int,
        page: Int
    ): Observable<BaseBean<TodoBean>> {
        return mHttpDataSource.getTodoList(status, type, priority, orderby, page)
    }

    override fun addTodo(
        title: String,
        content: String,
        date: String,
        @TodoType type: Int,
        priority: Int
    ): Observable<BaseBean<TodoBean.Data>> {
        return mHttpDataSource.addTodo(title, content, date, type, priority)
    }

    override fun deleteTodo(todoId: Int): Observable<BaseBean<Any?>> {
        return mHttpDataSource.deleteTodo(todoId)
    }

    override fun updateTodoState(todoId: Int, status: Int): Observable<BaseBean<Any?>> {
        return mHttpDataSource.updateTodoState(todoId, status)
    }

    override fun updateTodo(todoInfo: TodoBean.Data): Observable<BaseBean<TodoBean.Data>> {
        return mHttpDataSource.updateTodo(todoInfo)
    }

    override fun getLocalData(): String {
        return mLocalDataSource.getLocalData()
    }

    override fun getLoginName(): String? {
        return mLocalDataSource.getLoginName()
    }

    override fun saveUserData(userBean: UserBean) {
        mLocalDataSource.saveUserData(userBean)
    }

    override fun getUserData(): UserBean? {
        return mLocalDataSource.getUserData()
    }

    override fun getUserId(): Int {
        return mLocalDataSource.getUserId()
    }

    override fun clearLoginState() {
        mLocalDataSource.clearLoginState()
    }

    override fun saveUserSearchHistory(keyword: String): Flowable<Boolean> {
        return mLocalDataSource.saveUserSearchHistory(keyword)
    }

    override fun getSearchHistoryByUid(): Flowable<List<SearchHistoryEntity>> {
        return mLocalDataSource.getSearchHistoryByUid()
    }

    override fun deleteSearchHistory(history: String): Disposable {
        return mLocalDataSource.deleteSearchHistory(history)
    }

    override fun deleteAllSearchHistory(): Observable<Int> {
        return mLocalDataSource.deleteAllSearchHistory()
    }

    override fun saveUserBrowseHistory(title: String, link: String) {
        return mLocalDataSource.saveUserBrowseHistory(title,link)
    }

    override fun getUserBrowseHistoryByUid(): Flowable<List<WebHistoryEntity>> {
        return mLocalDataSource.getUserBrowseHistoryByUid()
    }

    override fun deleteBrowseHistory(title: String, link: String): Observable<Int> {
        return mLocalDataSource.deleteBrowseHistory(title,link)
    }

    override fun deleteAllWebHistory(): Observable<Int> {
        return mLocalDataSource.deleteAllWebHistory()
    }

    override fun saveFollowSysModeFlag(isFollow: Boolean) {
        return mLocalDataSource.saveFollowSysModeFlag(isFollow)
    }

    override fun getFollowSysUiModeFlag(): Boolean {
        return mLocalDataSource.getFollowSysUiModeFlag()
    }

    override fun saveUiMode(nightModeFlag: Boolean) {
        return mLocalDataSource.saveUiMode(nightModeFlag)
    }

    override fun getUiMode(): Boolean {
        return mLocalDataSource.getUiMode()
    }

    override fun saveReadHistoryState(visible: Boolean) {
        return mLocalDataSource.saveReadHistoryState(visible)
    }

    override fun getReadHistoryState(): Boolean {
        return mLocalDataSource.getReadHistoryState()
    }

    override fun <T : Serializable> saveCacheListData(list: List<T>) {
        return mLocalDataSource.saveCacheListData(list)
    }

    override fun <T : Serializable> getCacheListData(key:String): List<T>? {
        return mLocalDataSource.getCacheListData(key)
    }

    override fun userLogin(account: String, pwd: String): Observable<BaseBean<UserBean>> {
        return mHttpDataSource.userLogin(account, pwd)
    }

}