package com.czl.lib_base.data.source.impl

import com.blankj.utilcode.util.GsonUtils
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.source.LocalDataSource
import com.czl.lib_base.util.SpUtils
import com.google.gson.reflect.TypeToken

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
class LocalDataImpl : LocalDataSource {

//    companion object {
//        val INSTANCE: LocalDataImpl by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LocalDataImpl() }
//    }

    override fun getLocalData(): String {

        val data: IntRange = 1..50
        val list: MutableList<Int> = mutableListOf()
        for (i in data) {
            list.add(i)
        }
        return list.toString()
    }

    override fun getLoginName(): String? {
        return SpUtils.decodeString(AppConstants.SpKey.LOGIN_NAME)
    }

    override fun saveLoginName(name: String?) {
        SpUtils.encode(AppConstants.SpKey.LOGIN_NAME, name)
    }

    override fun saveUserId(id: Int) {
        SpUtils.encode(AppConstants.SpKey.USER_ID, id)
    }

    override fun getUserId(): Int {
        return SpUtils.decodeInt(AppConstants.SpKey.USER_ID)
    }

    override fun clearLoginState() {
        SpUtils.clearAll()
    }

    override fun saveSearchHistory(keyword: List<String>) {
        SpUtils.encode(AppConstants.SpKey.SEARCH_HISTORY, GsonUtils.toJson(keyword))
    }

    override fun getSearchHistory(): List<String> {
        return GsonUtils.fromJson(SpUtils.decodeString(AppConstants.SpKey.SEARCH_HISTORY),
            object : TypeToken<List<String>>() {}.type)?: emptyList()
    }
}