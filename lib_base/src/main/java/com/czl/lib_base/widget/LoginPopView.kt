package com.czl.lib_base.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.databinding.DataBindingUtil
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.databinding.PopLoginBinding
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.lxj.xpopup.core.BottomPopupView
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author Alwyn
 * @Date 2020/11/14
 * @Description
 */
@SuppressLint("ViewConstructor")
@KoinApiExtension
class LoginPopView(context: BaseActivity<*, *>) : BottomPopupView(context), KoinComponent {

    private var dataBinding: PopLoginBinding? = null
    private val dataResp: DataRepository by inject()
    val onLoginClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.let {
            dataResp.userLogin(it.etAccount.text.toString(), it.etPwd.text.toString())
                .compose(RxThreadHelper.rxSchedulerHelper(context.viewModel))
                .doOnSubscribe { context.viewModel.showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<UserBean>>() {
                    override fun onResult(t: BaseBean<UserBean>) {
                        context.viewModel.dismissLoading()
                        if (t.errorCode == 0) {
                            t.data?.let { data ->
                                dataResp.saveUserData(data)
                            }
                            LiveBusCenter.postLoginSuccessEvent()
                            dismiss()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        context.viewModel.dismissLoading()
                        context.showErrorToast(msg)
                    }

                })
        }
    })

    override fun getImplLayoutId(): Int {
        return R.layout.pop_login
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.pop = this

    }

    override fun onDestroy() {
        super.onDestroy()
        dataBinding?.unbind()
    }
}