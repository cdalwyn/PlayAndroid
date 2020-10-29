package com.czl.module_project.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_project.BR
import com.czl.module_project.R
import com.czl.module_project.databinding.ProjectFragmentProjectBinding
import com.czl.module_project.viewmodel.ProjectViewModel


@Route(path = AppConstants.Router.Project.F_PROJECT)
class ProjectFragment : BaseFragment<ProjectFragmentProjectBinding, ProjectViewModel>() {
    override fun initContentView(): Int {
        return R.layout.project_fragment_project
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    override fun useBaseLayout(): Boolean {
        return false
    }

}