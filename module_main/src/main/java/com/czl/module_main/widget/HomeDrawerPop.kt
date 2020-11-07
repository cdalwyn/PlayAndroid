package com.czl.module_main.widget

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.extension.loadCircleImageRes
import com.czl.lib_base.widget.CommonItemSettingView
import com.czl.module_main.R
import com.czl.module_main.ui.fragment.HomeFragment
import com.lxj.xpopup.core.DrawerPopupView
import org.koin.core.KoinComponent
import org.koin.core.get

/**
 * @author Alwyn
 * @Date 2020/11/4
 * @Description
 */
@SuppressLint("ViewConstructor")
class HomeDrawerPop(private val fragment: HomeFragment) :
    DrawerPopupView(fragment.requireContext()), KoinComponent {

    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_drawerlayout
    }

    override fun onCreate() {
        super.onCreate()
        findViewById<ImageView>(R.id.iv_icon).loadCircleImageRes(R.mipmap.ic_launcher)
        val tvName = findViewById<TextView>(R.id.tv_name)
        val btnExit = findViewById<CommonItemSettingView>(R.id.btn_exit)
        val loginName = fragment.viewModel.model.getLoginName()
        if (loginName.isNullOrBlank()) {
            tvName.text = "未登录"
            btnExit.setTitle("前往登录")
        } else {
            tvName.text = loginName
            btnExit.setOnClickListener {
                fragment.viewModel.logout()
            }
        }
    }

}