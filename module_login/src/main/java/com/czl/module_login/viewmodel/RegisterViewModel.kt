package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.data.entity.UserBean
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import me.goldze.mvvmhabit.utils.ToastUtils


/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
class RegisterViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {


}