package com.czl.lib_base.data.db

import org.litepal.crud.LitePalSupport

/**
 * @author Alwyn
 * @Date 2020/11/9
 * @Description
 */
class SearchHistoryEntity(val history: String, val searchDate: Long, val userEntity: UserEntity) :
    LitePalSupport() {
    val id: Long = 0
}