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
class HomeDrawerPop(private val fragment: HomeFragment) : DrawerPopupView(fragment.requireContext()),KoinComponent {

    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_drawerlayout
    }

    override fun onCreate() {
        super.onCreate()
        val dataResp:DataRepository = get()
        findViewById<ImageView>(R.id.iv_icon).loadCircleImageRes(R.mipmap.ic_launcher)
        findViewById<TextView>(R.id.tv_name).text = dataResp.getLoginName()
        findViewById<CommonItemSettingView>(R.id.btn_exit).setOnClickListener {
            fragment.viewModel.logout()
        }
    }

}