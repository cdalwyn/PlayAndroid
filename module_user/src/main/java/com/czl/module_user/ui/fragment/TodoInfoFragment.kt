package com.czl.module_user.ui.fragment

import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.TodoBean
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.DialogHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentTodoInfoBinding
import com.czl.module_user.viewmodel.TodoInfoFmViewModel
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding3.widget.checked
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Alwyn
 * @Date 2021/1/12
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_TODO_INFO)
class TodoInfoFragment : BaseFragment<UserFragmentTodoInfoBinding, TodoInfoFmViewModel>() {
    var todoInfo: TodoBean.Data? = null
    override fun initContentView(): Int {
        return R.layout.user_fragment_todo_info
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun isThemeRedStatusBar(): Boolean {
        return true
    }

    override fun initFitThemeStatusBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(false, 0.2f)
            .statusBarColor(com.czl.lib_base.R.color.md_theme_red)
            .fitsSystemWindows(true)
            .init()
    }

    override fun initData() {
        todoInfo = arguments?.getParcelable(AppConstants.BundleKey.TODO_INFO_DATA)
        binding.data = todoInfo
        // todo 状态需要保存后返回 由于status未能序列化
        showNormalToast("status=${todoInfo?.status}")
    }

    override fun initViewObservable() {
        viewModel.uc.pickDateEvent.observe(this, {
            DialogHelper.showDateDialog(requireActivity() as BaseActivity<*, *>) { dialog: MaterialDialog, datetime: Calendar ->
                binding.tvDate.text =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(datetime.time)
            }
        })
    }

    override fun back() {
        updateBackData()
        super.back()
    }

    private fun updateBackData() {
        todoInfo?.apply {
            title = binding.etTitle.text.toString()
            content = binding.etContent.text.toString()
            dateStr = binding.tvDate.text.toString()
            type = viewModel.todoType
            priority = viewModel.todoPriority
        }
        activity?.setResult(200, Intent().apply {
            putExtra(AppConstants.BundleKey.TODO_INFO_DATA, todoInfo)
        })
    }

    override fun onBackPressedSupport(): Boolean {
        updateBackData()
        return super.onBackPressedSupport()
    }

}