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
import com.czl.lib_base.widget.CommonItemSettingView
import com.czl.module_main.R
import com.czl.module_main.databinding.MainPopDrawerlayoutBinding
import com.czl.module_main.ui.fragment.HomeFragment
import com.lxj.xpopup.core.DrawerPopupView
import org.koin.core.KoinComponent
import org.koin.core.get

/**
 * @author Alwyn
 * @Date 2020/11/4
 * @Description
 */
class HomeDrawerPop(private val fragment: HomeFragment) :
    DrawerPopupView(fragment.requireContext()) {

    var binding: MainPopDrawerlayoutBinding? = null
    val onLogoutClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        fragment.viewModel.logout()
    })

    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_drawerlayout
    }

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(drawerContentContainer.findViewById(R.id.ll_drawer))
        binding?.apply {
            user = fragment.viewModel.model.getUserData()
            ivIcon.loadCircleImageRes(R.mipmap.ic_launcher)
            pop = this@HomeDrawerPop
        }
//        findViewById<ImageView>(R.id.iv_icon).loadCircleImageRes(R.mipmap.ic_launcher)
//        val tvName = findViewById<TextView>(R.id.tv_name)
//        val btnExit = findViewById<CommonItemSettingView>(R.id.btn_exit)
//        val loginName = fragment.viewModel.model.getLoginName()
//        if (loginName.isNullOrBlank()) {
//            tvName.text = "未登录"
//            btnExit.setTitle("前往登录")
//            btnExit.setOnClickListener {
//                fragment.startContainerActivity(AppConstants.Router.Login.F_LOGIN)
//                AppManager.getInstance().finishAllActivity()
//            }
//        } else {
//            tvName.text = loginName
//            btnExit.setOnClickListener {
//                fragment.viewModel.logout()
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }

}