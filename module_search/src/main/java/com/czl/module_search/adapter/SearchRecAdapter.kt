package com.czl.module_search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.data.db.SearchHistoryEntity
import com.czl.lib_base.util.DialogHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_search.R
import com.czl.module_search.databinding.SearchRecChildBinding
import com.czl.module_search.ui.fragment.SearchFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import io.reactivex.Flowable

/**
 * @author Alwyn
 * @Date 2020/12/23
 * @Description
 */
class SearchRecAdapter(
    private val mFragment: SearchFragment,
    private val hotKeyList: ArrayList<String>?,
    private val histories: Flowable<List<SearchHistoryEntity>>
) :
    BaseQuickAdapter<String, BaseDataBindingHolder<SearchRecChildBinding>>(R.layout.search_rec_child) {

    override fun convert(holder: BaseDataBindingHolder<SearchRecChildBinding>, item: String) {
        holder.dataBinding?.apply {
            title = item
            adapter = this@SearchRecAdapter
            val mAdapter = SearchRecChildAdapter(mFragment)
            mAdapter.setDiffCallback(mAdapter.diffConfig)
            ryChild.apply {
                layoutManager = FlexboxLayoutManager(
                    context,
                    FlexDirection.ROW,
                    FlexWrap.WRAP
                ).apply { justifyContent = JustifyContent.FLEX_START }
                adapter = mAdapter
            }
            when (holder.layoutPosition) {
                0 -> {
                    mAdapter.setDiffNewData(hotKeyList)
                }
                1 -> {
                    mFragment.viewModel.addSubscribe(histories.compose(RxThreadHelper.rxFlowSchedulerHelper())
                        .subscribe { list ->
                            if (list.isEmpty()){
                                getViewByPosition(1,R.id.ll_root)?.visibility = View.GONE
                            }
                            mAdapter.setDiffNewData(list.map { it.history } as MutableList<String>)
                        })
                }
            }
            executePendingBindings()
        }
    }

    val onClearClick: BindingCommand<Void> = BindingCommand(BindingAction {
        DialogHelper.showBaseDialog(context,"提示","是否清空所有搜索记录？"){
            mFragment.viewModel.model.deleteAllSearchHistory()
                .compose(RxThreadHelper.rxSchedulerHelper(mFragment.viewModel))
                .subscribe { count ->
                    if (count > 0) {
                        val ryChild = getViewByPosition(1, R.id.ry_child) as RecyclerView
                        (ryChild.adapter as SearchRecChildAdapter).setDiffNewData(null)
                        getViewByPosition(1,R.id.ll_root)?.visibility = View.GONE
                    }
                }
        }
    })


//    override fun convert(
//        holder: BaseDataBindingHolder<SearchRecChildBinding>,
//        item: String,
//        payloads: List<Any>
//    ) {
//        if (payloads.isEmpty()) {
//            return convert(holder, item)
//        }
//        super.convert(holder, item, payloads)
//        // 局部刷新只隐藏历史记录 热门搜索不刷新
//        getViewByPosition(1,R.id.ll_root)?.visibility = View.GONE
//    }
}