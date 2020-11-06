package com.czl.module_search.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.data.entity.HomeProjectBean
import com.czl.lib_base.data.entity.SearchDataBean
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel

/**
 * @author Alwyn
 * @Date 2020/11/6
 * @Description
 */
class SearchItemViewModel(searchViewModel: SearchViewModel):ItemViewModel<SearchViewModel>(searchViewModel) {
    var entity: ObservableField<SearchDataBean.Data> = ObservableField()
    val tvShare = "分享者："
    val tvAuthor = "作者："
    constructor(searchViewModel: SearchViewModel, data: SearchDataBean.Data) : this(searchViewModel) {
        entity.set(data)
    }
}