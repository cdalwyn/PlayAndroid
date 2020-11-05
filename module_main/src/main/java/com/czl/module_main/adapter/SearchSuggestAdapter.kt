package com.czl.module_main.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.czl.lib_base.data.entity.HomeBannerBean
import com.czl.lib_base.data.entity.SearchDataBean
import com.czl.lib_base.data.entity.SearchHotKeyBean
import com.czl.module_main.R
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter


/**
 * @author Alwyn
 * @Date 2020/11/5
 * @Description
 */
class SearchSuggestAdapter(inflater: LayoutInflater) :
    SuggestionsAdapter<SearchHotKeyBean, SearchSuggestAdapter.SuggestionHolder>(inflater) {

    open class SuggestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
    }

    private var mDiffer: AsyncListDiffer<SearchHotKeyBean>

    init {
        val diffCallback: DiffUtil.ItemCallback<SearchHotKeyBean> =
            object : DiffUtil.ItemCallback<SearchHotKeyBean>() {
                override fun areItemsTheSame(
                    oldItem: SearchHotKeyBean,
                    newItem: SearchHotKeyBean
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SearchHotKeyBean,
                    newItem: SearchHotKeyBean
                ): Boolean {
                    return TextUtils.equals(oldItem.name, newItem.name)
                }
            }
        mDiffer = AsyncListDiffer(this, diffCallback)
    }

    private fun submitList(data: List<SearchHotKeyBean>?) {
        mDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        return SuggestionHolder(
            layoutInflater.inflate(
                R.layout.main_item_suggestion,
                parent,
                false
            )
        )
    }

    override fun getSingleViewHeight(): Int {
        return 40
    }

    override fun onBindSuggestionHolder(
        suggestion: SearchHotKeyBean,
        holder: SuggestionHolder,
        position: Int
    ) {
        holder.title.text = suggestion.name
    }
}