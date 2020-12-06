package com.czl.module_main.widget

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.extension.loadCircleImage
import com.czl.lib_base.extension.loadCircleImageRes
import com.czl.lib_base.util.PopDialogUtils
import com.czl.lib_base.widget.CommonItemSettingView
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
        PopDialogUtils.showBaseDialog(context,"注销","是否确定退出登录？"){
            fragment.viewModel.logout()
        }
    })
    val onOpenCollectCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        fragment.startContainerActivity(AppConstants.Router.User.F_USER_COLLECT)
        dismiss()
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