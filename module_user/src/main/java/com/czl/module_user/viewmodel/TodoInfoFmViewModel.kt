package com.czl.module_user.viewmodel

import android.view.View
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.module_user.ui.fragment.SecondFragment


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class TodoInfoFmViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    override fun setToolbarRightClick() {

    }
}