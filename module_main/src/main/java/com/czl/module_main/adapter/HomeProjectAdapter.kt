package com.czl.module_main.adapter

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.extension.ImagePopLoader
import com.czl.lib_base.extension.loadImage
import com.czl.module_main.R
import com.czl.module_main.databinding.MainItemProjectBinding
import com.czl.module_main.ui.fragment.HomeFragment
import com.czl.module_main.ui.fragment.HomeProjectFragment
import com.czl.module_main.widget.ProjectItemSettingPop
import com.lxj.xpopup.XPopup

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description
 */
class HomeProjectAdapter(val mFragment: HomeFragment) :
    BaseQuickAdapter<ProjectBean.Data, BaseDataBindingHolder<MainItemProjectBinding>>(
        R.layout.main_item_project
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<MainItemProjectBinding>,
        item: ProjectBean.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@HomeProjectAdapter
            executePendingBindings()
        }
    }

    /*Item点击事件*/
    val onProjectItemClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is ProjectBean.Data) {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.WEB_URL, it.link)
            mFragment.startContainerActivity(AppConstants.Router.Base.F_WEB, bundle)
        }
    })

    /*Item的设置点击事件*/
    val onItemSettingClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is ProjectBean.Data) {
            XPopup.Builder(context)
                .hasShadowBg(false)
                .isDestroyOnDismiss(true)
                .atView(getViewByPosition(getItemPosition(it), R.id.iv_setting))
                .asCustom(ProjectItemSettingPop(mFragment, it))
                .show()
        }
    })

    /*Item的图片点击事件*/
    val onItemPicClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is ProjectBean.Data) {
            XPopup.Builder(context)
                .asImageViewer(
                    getViewByPosition(
                        getItemPosition(it),
                        R.id.iv_item_works_list
                    ) as ImageView, it.envelopePic, ImagePopLoader()
                )
                .show()
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
            return oldItem.title == newItem.title && oldItem.collect == newItem.collect
        }

    }
}
