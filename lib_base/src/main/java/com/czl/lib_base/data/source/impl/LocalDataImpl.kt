package com.czl.lib_base.data.source.impl

import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.source.LocalDataSource
import com.czl.lib_base.util.SpUtils

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

        val data:IntRange = 1..50
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
        SpUtils.encode(AppConstants.SpKey.LOGIN_NAME,name)
    }

    override fun clearLoginState() {
        SpUtils.clearAll()
    }
}