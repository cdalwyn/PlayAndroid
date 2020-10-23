package com.czl.lib_base.mvvm.viewmodel

import android.app.Application
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.DataRepository
import com.czl.lib_base.base.MyApplication
import me.goldze.mvvmhabit.base.BaseModel

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class CommonViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application,model) {
}