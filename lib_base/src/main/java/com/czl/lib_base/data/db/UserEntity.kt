package com.czl.lib_base.data.db

import org.litepal.crud.LitePalSupport

/**
 * @author Alwyn
 * @Date 2020/11/9
 * @Description
 */
class UserEntity(val uid: Int, val username: String, val historyEntity: List<SearchHistoryEntity> = arrayListOf()) :
    LitePalSupport() {
    val id: Long = 0
}