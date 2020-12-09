package com.czl.lib_base.widget

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.databinding.PopShareArticleBinding
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Alwyn
 * @Date 2020/12/9
 * @Description
 */
@SuppressLint("ViewConstructor")
class ShareArticlePopView(val activity: BaseActivity<*, *>) : BottomPopupView(activity) {
    private var dataBinding: PopShareArticleBinding? = null
    override fun getImplLayoutId(): Int {
        return R.layout.pop_share_article
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@ShareArticlePopView
            clRoot.background = XPopupUtils.createDrawable(
                ContextCompat.getColor(context, R.color.white),
                30f,
                30f,
                0f,
                0f
            )
            initInputState()
            executePendingBindings()
        }
    }

    private fun PopShareArticleBinding.initInputState() {
        val accountSubject = PublishSubject.create<String>()
        val pwdSubject = PublishSubject.create<String>()
        etTitle.addTextChangedListener(EditTextMonitor(accountSubject))
        etLink.addTextChangedListener(EditTextMonitor(pwdSubject))
        activity.viewModel.addSubscribe(
            Observable.combineLatest(
                accountSubject,
                pwdSubject,
                { account: String, pwd: String -> account.isNotEmpty() && pwd.isNotEmpty() })
                .subscribe { flag ->
                    sdlShare.setLayoutBackground(
                        ContextCompat.getColor(
                            activity,
                            if (flag) R.color.md_theme_red else R.color.white
                        )
                    )
                    btnShare.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            if (!flag) R.color.md_theme_red else R.color.white
                        )
                    )
                    btnShare.isEnabled = flag
                })
    }

    val onBtnShareClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.apply {
            activity.dataRepository.shareArticleToSquare(
                etTitle.text.toString().trim(),
                etLink.text.toString().trim()
            )
                .compose(RxThreadHelper.rxSchedulerHelper(activity.viewModel))
                .doOnSubscribe { activity.viewModel.showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        activity.viewModel.dismissLoading()
                        if (t.errorCode == 0) {
                            activity.showSuccessToast("分享成功")
                            dismiss()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        activity.viewModel.showErrorToast(msg)
                    }
                })
        }
    })
}