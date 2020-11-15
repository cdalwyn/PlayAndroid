package com.czl.lib_base.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.czl.lib_base.R
import com.czl.lib_base.adapter.ViewPagerFmAdapter
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseRxActivity
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.databinding.PopLoginBinding
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import com.lxj.xpopup.core.BottomPopupView
import me.yokeyword.fragmentation.SupportFragment
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
class LoginPopView(val activity: BaseActivity<*, *>) : BottomPopupView(activity), KoinComponent {

    private var dataBinding: PopLoginBinding? = null
    private val dataResp: DataRepository by inject()

    val registerFlag: ObservableInt = ObservableInt(0)
    val tvLoginAccount = ObservableField("")
    val tvLoginPwd = ObservableField("")

    // 登录
    val onLoginClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.let {
            dataResp.userLogin(it.etAccount.text.toString().trim(), it.etPwd.text.toString().trim())
                .compose(RxThreadHelper.rxSchedulerHelper(activity.viewModel))
                .doOnSubscribe { activity.viewModel.showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<UserBean>>() {
                    override fun onResult(t: BaseBean<UserBean>) {
                        activity.viewModel.dismissLoading()
                        if (t.errorCode == 0) {
                            t.data?.let { data ->
                                dataResp.saveUserData(data)
                            }
                            LiveBusCenter.postLoginSuccessEvent()
                            dismiss()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        activity.viewModel.dismissLoading()
                        activity.showErrorToast(msg)
                    }

                })
        }
    })

    // 切换注册UI
    val onRegisterClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        registerFlag.set(1)
    })

    // 返回登录UI
    val toLoginClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        registerFlag.set(0)
    })

    // 注册
    val registerClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.let {
            dataResp.register(
                it.etRegAccount.text.toString().trim(),
                it.etRegPwd.text.toString().trim(),
                it.etRegRePwd.text.toString().trim()
            )
                .compose(RxThreadHelper.rxSchedulerHelper(activity.viewModel))
                .doOnSubscribe { activity.viewModel.showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                    override fun onResult(t: BaseBean<*>) {
                        activity.viewModel.dismissLoading()
                        if (t.errorCode==0){
                            activity.showSuccessToast("注册成功")
                            tvLoginAccount.set(it.etRegAccount.text.toString().trim())
                            tvLoginPwd.set(it.etRegPwd.text.toString().trim())
                            it.etRegAccount.setText("")
                            it.etRegPwd.setText("")
                            it.etRegRePwd.setText("")
                            registerFlag.set(0)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        activity.viewModel.dismissLoading()
                        activity.showErrorToast(msg)
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