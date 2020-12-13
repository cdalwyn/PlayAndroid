package com.czl.lib_base.data.source

import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.data.db.SearchHistoryEntity
import com.czl.lib_base.data.db.WebHistoryEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
interface LocalDataSource {
    fun getLocalData(): String
    fun getLoginName(): String?
    fun saveUserData(userBean: UserBean)
    fun getUserData(): UserBean?
    fun getUserId(): Int
    fun clearLoginState()
    fun saveUserSearchHistory(keyword: String): Flowable<Boolean>
    fun getSearchHistoryByUid(): Flowable<List<SearchHistoryEntity>>
    fun deleteSearchHistory(history: String): Disposable
    fun saveUserBrowseHistory(title: String, link: String)
    fun getUserBrowseHistoryByUid(): Flowable<List<WebHistoryEntity>>
    fun deleteBrowseHistory(title: String, link: String): Observable<Int>
    fun deleteAllWebHistory(): Observable<Int>
}