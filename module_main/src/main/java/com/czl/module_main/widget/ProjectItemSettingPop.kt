package com.czl.module_main.widget

import android.content.Context
import android.widget.ImageView
import com.czl.lib_base.extension.loadCircleImage
import com.czl.lib_base.extension.loadCircleImageRes
import com.czl.module_main.R
import com.lxj.xpopup.core.HorizontalAttachPopupView

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
class ProjectItemSettingPop(context: Context) : HorizontalAttachPopupView(context) {


    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_setting_attach
    }


}