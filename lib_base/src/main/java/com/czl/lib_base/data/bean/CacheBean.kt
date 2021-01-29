package com.czl.lib_base.data.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2021/1/29
 * @Description
 */
data class HomeBannerCache(val homeBannerBeans: List<HomeBannerBean>):Serializable

data class HomeArticleCache(val homeArticleBeans: List<HomeArticleBean.Data>):Serializable

data class HomeSearchKeywordCache(val searchHotKeyBean: List<SearchHotKeyBean>):Serializable