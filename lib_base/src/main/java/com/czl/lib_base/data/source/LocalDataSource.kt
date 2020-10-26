package com.czl.lib_base.data.source

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
interface LocalDataSource {
    fun getLocalData(): String
    fun getLoginName(): String?
    fun saveLoginName(name:String?)
    fun clearLoginState()
}