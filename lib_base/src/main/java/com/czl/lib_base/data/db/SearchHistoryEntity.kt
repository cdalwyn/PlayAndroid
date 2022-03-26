package com.czl.lib_base.data.db

import org.litepal.crud.LitePalSupport
import java.util.*

/**
 * @author Alwyn
 * @Date 2020/11/9
 * @Description
 */
data class SearchHistoryEntity(
    val history: String,
    val searchDate: Date,
    var userEntity: UserEntity
) : LitePalSupport()