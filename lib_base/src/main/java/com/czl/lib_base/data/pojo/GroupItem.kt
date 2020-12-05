package com.czl.lib_base.data.pojo

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
data class GroupItem(val group: String?, val list: List<ItemInfo>) : Serializable {
    data class ItemInfo(val cid:Int,val title:String) : Serializable
}