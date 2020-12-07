package com.czl.lib_base.data.db

import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import org.litepal.extension.find
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Alwyn
 * @Date 2020/11/10
 * @Description
 */
data class UserEntity(
    @Column(unique = true)
    var uid: Int,
    var username: String?,
    var historyEntities: ArrayList<SearchHistoryEntity> = ArrayList(),
    var browseEntities: ArrayList<WebHistoryEntity> = ArrayList()
) : LitePalSupport() {
    var id: Long = 0
    fun getRecentHistory(): List<SearchHistoryEntity> {
        val allHistory = LitePal.select("history").where("userentity_id =?", id.toString())
            .find<SearchHistoryEntity>().reversed()
        if (allHistory.isNullOrEmpty()) return arrayListOf()
        return if (allHistory.size >= 5) allHistory.subList(0, 5) else allHistory
    }

    fun getAllHistory(): List<SearchHistoryEntity> {
        return LitePal.where("userentity_id =?", id.toString()).find()
    }

    fun getAllWebHistory(): List<WebHistoryEntity> {
        return LitePal.where("userentity_id=?", id.toString()).order("browseDate desc").find()
    }
}