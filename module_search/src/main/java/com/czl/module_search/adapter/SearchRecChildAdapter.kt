package com.czl.module_search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.module_search.R
import com.czl.module_search.databinding.SearchItemChildBinding

/**
 * @author Alwyn
 * @Date 2020/12/23
 * @Description
 */
class SearchRecChildAdapter:BaseQuickAdapter<String,BaseDataBindingHolder<SearchItemChildBinding>>(R.layout.search_item_child) {
    override fun convert(holder: BaseDataBindingHolder<SearchItemChildBinding>, item: String) {
        holder.dataBinding?.apply {
            name = item
            executePendingBindings()
        }
    }

    val diffConfig = object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }
}