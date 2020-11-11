package com.czl.module_project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.module_project.R
import com.czl.module_project.databinding.ProjectFragmentContentBinding

/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description
 */
class ProjectItemGridAdapter :BaseQuickAdapter<ProjectBean.Data,BaseDataBindingHolder<ProjectFragmentContentBinding>>(R.layout.project_item_grid) {
    override fun convert(
        holder: BaseDataBindingHolder<ProjectFragmentContentBinding>,
        item: ProjectBean.Data
    ) {
        // todo 瀑布流图片宽高设置
    }
}