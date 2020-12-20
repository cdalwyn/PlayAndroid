package com.czl.module_square.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.pojo.GroupItem
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareFragmentSystemBinding
import com.czl.module_square.viewmodel.SystemTreeVm
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder
import com.kunminx.linkage.bean.BaseGroupedItem
import com.kunminx.linkage.bean.DefaultGroupedItem
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig
import com.lihang.ShadowLayout

/**
 * @author Alwyn
 * @Date 2020/11/29
 * @Description 体系
 */
@Route(path = AppConstants.Router.Square.F_SYSTEM)
class SystemTreeFragment : BaseFragment<SquareFragmentSystemBinding, SystemTreeVm>() {

    val dataArrayMap:SparseArray<GroupItem> = SparseArray()

    override fun initContentView(): Int {
        return R.layout.square_fragment_system
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("体系")
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        viewModel.loadCompletedEvent.observe(this, { datas ->
            binding.smartCommon.finishRefresh()
            if (datas == null) {
                return@observe
            }
            val list = ArrayList<DefaultGroupedItem>()
            datas.forEach {
                list.add(DefaultGroupedItem(true, it.name))
                val items = ArrayList<GroupItem.ItemInfo>()
                for (child in it.children) {
                    items.add(GroupItem.ItemInfo(child.id,child.name))
                    // content=id 便于点击查询
                    list.add(DefaultGroupedItem(DefaultGroupedItem.ItemInfo(child.name, it.name,it.id.toString())))
                }
                dataArrayMap.put(it.id, GroupItem(it.name,items))
            }
            binding.ryLink.apply {
                init(list, PrimaryAdapterConfig(context), SecondGroupAdapterConfig())
//                isGridMode = true
            }
        })
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }

    inner class PrimaryAdapterConfig(private var mContext: Context?) :
        ILinkagePrimaryAdapterConfig {
        override fun setContext(context: Context?) {
            mContext = context
        }

        override fun getLayoutId(): Int {
            return R.layout.default_adapter_linkage_primary
        }

        override fun getGroupTitleViewId(): Int {
            return R.id.tv_group
        }

        override fun getRootViewId(): Int {
            return R.id.layout_group
        }

        override fun onBindViewHolder(
            holder: LinkagePrimaryViewHolder?,
            selected: Boolean,
            title: String?
        ) {
            val tvGroup = holder?.mGroupTitle as TextView
            tvGroup.apply {
                text = title
                setBackgroundColor(
                    ContextCompat.getColor(
                        mContext!!,
                        if (selected) R.color.md_theme_red else R.color.white
                    )
                )
                setTextColor(
                    ContextCompat.getColor(
                        mContext!!,
                        if (selected) R.color.white else R.color.black
                    )
                )
                ellipsize = if (selected) TextUtils.TruncateAt.MARQUEE else TextUtils.TruncateAt.END
                isFocusable = selected
                isFocusableInTouchMode = selected
                marqueeRepeatLimit = if (selected) -1 else 0
            }
        }

        override fun onItemClick(holder: LinkagePrimaryViewHolder?, view: View?, title: String?) {

        }

    }

    inner class SecondGroupAdapterConfig :
        ILinkageSecondaryAdapterConfig<DefaultGroupedItem.ItemInfo> {
        override fun setContext(context: Context?) {

        }

        override fun getGridLayoutId(): Int {
            return R.layout.square_item_sys_group
        }

        override fun getLinearLayoutId(): Int {
            return R.layout.square_item_sys_group
        }

        override fun getHeaderLayoutId(): Int {
            return R.layout.square_item_header
        }

        override fun getFooterLayoutId(): Int {
            return R.layout.default_adapter_linkage_secondary_footer
        }

        override fun getHeaderTextViewId(): Int {
            return R.id.tv_header
        }

        override fun getSpanCountOfGridMode(): Int {
            return 2
        }

        override fun onBindViewHolder(
            holder: LinkageSecondaryViewHolder,
            item: BaseGroupedItem<DefaultGroupedItem.ItemInfo>
        ) {
            holder.getView<TextView>(R.id.tv_desc)?.apply {
                text = item.info.title
            }
            val groupItem = dataArrayMap[item.info.content.toInt()]
            holder.getView<ShadowLayout>(R.id.btn_item).setOnClickListener {
                viewModel.startContainerActivity(AppConstants.Router.Square.F_SYS_DETAIL,Bundle().apply {
                    putInt(AppConstants.BundleKey.SYSTEM_DETAIL_POSITION,groupItem.list.indexOfFirst { it.title==item.info.title })
                    putSerializable(AppConstants.BundleKey.SYSTEM_DETAIL, groupItem)
                })
            }
        }

        override fun onBindHeaderViewHolder(
            holder: LinkageSecondaryHeaderViewHolder,
            item: BaseGroupedItem<DefaultGroupedItem.ItemInfo>
        ) {
            holder.apply {
                getView<TextView>(R.id.tv_header).text = item.header
            }
        }

        override fun onBindFooterViewHolder(
            holder: LinkageSecondaryFooterViewHolder,
            item: BaseGroupedItem<DefaultGroupedItem.ItemInfo>?
        ) {
            holder.getView<TextView>(R.id.tv_secondary_footer).apply {
                text = "--已经到底了--"
                textSize = 14f
                gravity = Gravity.CENTER
            }
        }

    }
}