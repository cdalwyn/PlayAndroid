package com.czl.module_main.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.data.entity.ArticleBean
import com.czl.lib_base.data.entity.HomeArticleBean
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel


/**
 * @author Alwyn
 * @Date 2020/10/30
 * @Description
 */
class HomeItemViewModel(homeViewModel: HomeViewModel) :
    ItemViewModel<HomeViewModel>(homeViewModel) {
    var entity: ObservableField<HomeArticleBean.Data> = ObservableField()
    val tvShare = "分享者："
    val tvAuthor = "作者："

    constructor(homeViewModel: HomeViewModel, data: HomeArticleBean.Data) : this(homeViewModel) {
        entity.set(data)
    }
}