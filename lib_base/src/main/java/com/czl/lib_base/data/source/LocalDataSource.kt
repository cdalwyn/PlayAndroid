package com.czl.lib_base.data.source

import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.data.db.SearchHistoryEntity
import io.reactivex.Flowable
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
    fun getUserId(): Int
    fun clearLoginState()
    fun saveUserSearchHistory(keyword: String):Flowable<Boolean>
    fun getSearchHistoryByUid():Flowable<List<SearchHistoryEntity>>

    fun deleteSearchHistory(history: String):Disposable
}