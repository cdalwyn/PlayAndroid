package com.czl.lib_base.base.source.impl

import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.base.source.LocalDataSource
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

        val data = (1..50)
        val list: MutableList<String> = mutableListOf()
        for (i in data) {
            list.add(i.toString())
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