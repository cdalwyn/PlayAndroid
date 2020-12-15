package com.czl.module_main.widget

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.util.DialogHelper
import com.czl.module_main.R
import com.czl.module_main.databinding.MainPopDrawerlayoutBinding
import com.czl.module_main.ui.fragment.HomeFragment
import com.lxj.xpopup.core.DrawerPopupView

/**
 * @author Alwyn
 * @Date 2020/11/4
 * @Description
 */
@SuppressLint("ViewConstructor")
class HomeDrawerPop(private val fragment: HomeFragment) :
    DrawerPopupView(fragment.requireContext()) {

    var binding: MainPopDrawerlayoutBinding? = null
    val onLogoutClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        DialogHelper.showBaseDialog(context, "注销", "是否确定退出登录？") {
            fragment.viewModel.logout()
        }
    })
    val onOpenCollectCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        fragment.startContainerActivity(AppConstants.Router.User.F_USER_COLLECT)
        dismiss()
    })

    val onOpenScoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        fragment.startContainerActivity(AppConstants.Router.User.F_USER_SCORE)
        dismiss()
    })

    val onOpenShareCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        fragment.startContainerActivity(AppConstants.Router.User.F_USER_SHARE)
        dismiss()
    })

    val onShowLoginPopClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (TextUtils.isEmpty(fragment.viewModel.model.getUserData()?.publicName)) {
            fragment.loginPopView.show()
        }
    })

    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_drawerlayout
    }

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(drawerContentContainer.findViewById(R.id.ll_drawer))
        binding?.apply {
            user = fragment.viewModel.model.getUserData()
            pop = this@HomeDrawerPop
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }

}