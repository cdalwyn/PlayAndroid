package com.czl.lib_base.data.db

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

/**
 * @author Alwyn
 * @Date 2020/11/9
 * @Description
 */
data class SearchHistoryEntity(
    val history: String,
    val searchDate: Long,
    var userEntity: UserEntity
) : LitePalSupport()