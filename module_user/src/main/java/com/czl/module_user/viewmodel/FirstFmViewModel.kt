package com.czl.module_user.viewmodel

import android.view.View
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.module_user.ui.fragment.SecondFragment

import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class FirstFmViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val startSecFmOnClick: View.OnClickListener = View.OnClickListener {
        startFragment(SecondFragment.newInstance())
    }

    override fun setToolbarRightClickListener(): () -> Unit {
        return {
            ToastUtils.showShort("这是FirstFragment标题栏图标点击事件")
        }
    }
}