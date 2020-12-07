package com.czl.lib_base.data.db

import org.litepal.crud.LitePalSupport
import java.sql.Date

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
data class WebHistoryEntity(
    val webTitle: String,
    val webLink:String,
    val browseDate: Int,
    var userEntity: UserEntity
) : LitePalSupport()