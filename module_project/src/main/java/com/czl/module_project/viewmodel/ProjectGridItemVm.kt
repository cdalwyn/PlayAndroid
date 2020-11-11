package com.czl.module_project.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel


/**
 * @author Alwyn
 * @Date 2020/10/30
 * @Description
 */
class ProjectGridItemVm(contentVm: ContentViewModel) :
    ItemViewModel<ContentViewModel>(contentVm) {
    var entity: ObservableField<ProjectBean.Data> = ObservableField()

    constructor(contentVm: ContentViewModel, data: ProjectBean.Data) : this(contentVm) {
        entity.set(data)
    }
}