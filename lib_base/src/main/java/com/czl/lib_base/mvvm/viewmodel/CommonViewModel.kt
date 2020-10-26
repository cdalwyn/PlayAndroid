package com.czl.lib_base.mvvm.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class CommonViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application,model) {
}