package com.czl.module_project.adapter

import android.graphics.Bitmap
import android.os.Bundle
import android.util.SparseArray
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.module_project.R
import com.czl.module_project.databinding.ProjectItemGridBinding
import com.czl.module_project.ui.fragment.ContentFragment


/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description 瀑布流适配器
 */
class ProjectItemGridAdapter(private val contentFragment: ContentFragment) :
    BaseQuickAdapter<ProjectBean.Data, BaseDataBindingHolder<ProjectItemGridBinding>>(R.layout.project_item_grid) {

    // 存储图片的高度
    private val sizeSparseArray: SparseArray<Int?> = SparseArray()

    override fun convert(
        holder: BaseDataBindingHolder<ProjectItemGridBinding>,
        item: ProjectBean.Data
    ) {
        holder.dataBinding?.apply {
            tvTitle.text = item.title
            adapter = this@ProjectItemGridAdapter
            this.item = item
            executePendingBindings()
        }
    }

    val onItemClickCommand: BindingCommand<Any?> = BindingCommand(BindingConsumer {
        if (it is ProjectBean.Data) {
            contentFragment.startContainerActivity(
                AppConstants.Router.Web.F_WEB,
                Bundle().apply { putString(AppConstants.BundleKey.WEB_URL, it.link) })
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<ProjectBean.Data>() {
        override fun areItemsTheSame(
            oldItem: ProjectBean.Data,
            newItem: ProjectBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProjectBean.Data,
            newItem: ProjectBean.Data
        ): Boolean {
            return oldItem == newItem
        }
    }
}