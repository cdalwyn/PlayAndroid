package com.czl.module_main.widget

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import com.czl.module_main.R
import com.czl.module_main.databinding.MainPopSettingAttachBinding
import com.lxj.xpopup.core.HorizontalAttachPopupView

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
class ProjectItemSettingPop(context: Context) : HorizontalAttachPopupView(context) {

    var binding: MainPopSettingAttachBinding? = null

    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_setting_attach
    }

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
    }

    override fun onDestroy() {
        binding?.unbind()
        super.onDestroy()
    }
}