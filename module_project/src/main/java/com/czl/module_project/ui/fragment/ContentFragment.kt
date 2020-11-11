package com.czl.module_project.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.czl.lib_base.base.BaseFragment
import com.czl.module_project.BR
import com.czl.module_project.R
import com.czl.module_project.databinding.ProjectFragmentContentBinding
import com.czl.module_project.viewmodel.ContentViewModel

/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description
 */
class ContentFragment : BaseFragment<ProjectFragmentContentBinding, ContentViewModel>() {

    companion object {
        const val SORT_ID = "sort_id"
        fun getInstance(id: Int): ContentFragment {
            val bundle = Bundle()
            bundle.putInt(SORT_ID, id)
            val contentFragment = ContentFragment()
            contentFragment.arguments = bundle
            return contentFragment
        }
    }

    override fun initContentView(): Int {
        return R.layout.project_fragment_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun initData() {
        val sortId = arguments?.getInt(SORT_ID, 0)
        sortId?.let {
            binding.smartCommon.autoRefreshAnimationOnly()
            viewModel.getProjectDataByCid(it)
        }
    }

    override fun initViewObservable() {
        viewModel.uc.refreshCompleteEvent.observe(this, Observer {
            binding.smartCommon.finishRefresh(300)
        })
    }

}