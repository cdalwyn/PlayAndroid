package com.czl.lib_base.data.bean

import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2021/1/29
 * @Description 进入App第一帧缓存数据 避免每次进入app重新请求网络耗时耗流量 过期时长在AppConst类中控制
 */
data class HomeBannerCache(val homeBannerBeans: List<HomeBannerBean>) : Serializable
data class HomeArticleCache(val homeArticleBeans: List<HomeArticleBean.Data>) : Serializable
data class HomeSearchKeywordCache(val searchHotKeyBean: List<SearchHotKeyBean>) : Serializable
data class HomeSquareCache(val squareCache: List<SquareListBean.Data>) : Serializable
data class HomeProjectSortCache(val sortCache: List<ProjectSortBean>) : Serializable
data class HomeProjectContentCache(val contentCache: List<ProjectBean.Data>) : Serializable