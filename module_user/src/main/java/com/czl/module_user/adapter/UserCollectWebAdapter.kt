package com.czl.module_user.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemWebsiteBinding
import com.czl.module_user.ui.fragment.CollectWebsiteFragment


/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class UserCollectWebAdapter(val mFragment: CollectWebsiteFragment) :
    BaseQuickAdapter<CollectWebsiteBean, BaseDataBindingHolder<UserItemWebsiteBinding>>(R.layout.user_item_website) {

    override fun convert(
        holder: BaseDataBindingHolder<UserItemWebsiteBinding>,
        item: CollectWebsiteBean
    ) {

        holder.dataBinding?.apply {
            data = item
            adapter = this@UserCollectWebAdapter
            menu = holder.dataBinding?.menuLayout
            executePendingBindings()
        }
    }

    val diffConfig = object : DiffUtil.ItemCallback<CollectWebsiteBean>() {
        override fun areItemsTheSame(
            oldItem: CollectWebsiteBean,
            newItem: CollectWebsiteBean
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: CollectWebsiteBean,
            newItem: CollectWebsiteBean
        ): Boolean {
            return oldItem.link == newItem.link
        }
    }

    val onCopyLinkClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is CollectWebsiteBean) {
            val clipboardManager = mFragment.requireActivity()
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Label", it.link)
            clipboardManager.primaryClip = clipData
            mFragment.showSuccessToast("复制成功")
        }
    })

    val onDeleteClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is CollectWebsiteBean) {
            mFragment.viewModel.model.deleteUserCollectWeb(it.id.toString())
                .compose(RxThreadHelper.rxSchedulerHelper(mFragment.viewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        if (t.errorCode == 0) {
                            remove(it)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mFragment.showErrorToast(msg)
                    }
                })
        }
    })

    val onOpenBrowserClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is CollectWebsiteBean) {
            val uri = Uri.parse(it.link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mFragment.startActivity(intent)
        }
    })
}


