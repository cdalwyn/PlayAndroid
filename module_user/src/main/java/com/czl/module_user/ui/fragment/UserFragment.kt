package com.czl.module_user.ui.fragment

import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.util.DialogHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentUserBinding
import com.czl.module_user.viewmodel.UserViewModel
import com.lxj.xpopup.core.BasePopupView
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER)
class UserFragment : BaseFragment<UserFragmentUserBinding, UserViewModel>(),
    NetworkUtils.OnNetworkStatusChangedListener {
    private val loginPopView: BasePopupView by inject(named("login"))

    override fun initContentView(): Int {
        return R.layout.user_fragment_user
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        if (!NetworkUtils.isConnected()){
            showNetErrorBar()
        }
        binding.userData = viewModel.model.getUserData()
        binding.userData?.apply {
            viewModel.getUserCollectData()
            viewModel.getUserShareData()
        }
        viewModel.historyVisible.set(viewModel.model.getReadHistoryState())
        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    override fun initViewObservable() {
        LiveBusCenter.observeLogoutEvent(this) {
            binding.userData = null
            viewModel.apply {
                tvCollect.set("0")
                tvScore.set("0")
                tvShare.set("0")
            }
        }
        LiveBusCenter.observeLoginSuccessEvent(this) {
            binding.userData = viewModel.model.getUserData()
            viewModel.getUserCollectData()
            viewModel.getUserShareData()
        }
        LiveBusCenter.observeRefreshUserFmEvent(this) {
            viewModel.getUserCollectData()
        }
        viewModel.uc.showLoginPopEvent.observe(this, {
            loginPopView.show()
        })
        viewModel.uc.refreshEvent.observe(this, {
            binding.smartCommon.finishRefresh(1500)
        })
        LiveBusCenter.observeReadHistoryEvent(this) {
            viewModel.historyVisible.set(it.checked)
        }
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }

    override fun onDestroyView() {
        NetworkUtils.unregisterNetworkStatusChangedListener(this)
        super.onDestroyView()
    }

    override fun onDisconnected() {
        showNetErrorBar()
    }

    private fun showNetErrorBar() {
        SnackbarUtils.with(binding.smartCommon).setDuration(SnackbarUtils.LENGTH_INDEFINITE)
            .setBgColor(ContextCompat.getColor(requireContext(), R.color.md_theme_red))
            .setMessage("网络已断开，请重新连接").show(true)
    }

    override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        SnackbarUtils.dismiss()
        // 重新联上网并且第一次未加载成功 避免每次断网重连都请求一次消耗流量
        if (NetworkUtils.isConnected() && !viewModel.firstSuccessLoadFlag) {
            binding.smartCommon.autoRefresh()
        }
    }
}