package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.*
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.DialogHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentSettingBinding
import com.czl.module_user.viewmodel.UserSettingVm

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_SETTING)
class UserSettingFragment : BaseFragment<UserFragmentSettingBinding, UserSettingVm>() {

    override fun initContentView(): Int {
        return R.layout.user_fragment_setting
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.apply {
            setTvCacheSize()
            tvTitle.set("设置")
            logoutVisible.set(!model.getLoginName().isNullOrBlank())
            historyVisible.set(model.getReadHistoryState())
        }
        LogUtils.i("checked=${viewModel.model.getFollowSysUiModeFlag()}")
        binding.swSys.isChecked = viewModel.model.getFollowSysUiModeFlag()
        // 跟随系统模式关闭时 判断黑夜模式状态
        if (!binding.swSys.isChecked) binding.swNight.isChecked = viewModel.model.getUiMode()
    }

    override fun initViewObservable() {
        viewModel.uc.switchUiModeEvent.observe(this, { checked ->
            if (checked) {
                // 当前app状态与选中模式不同才进行模式变化 通过渐变动画避免模式改变而引起的闪屏
                if (!DayModeUtil.isNightMode(requireContext())) {
                    DayModeUtil.setNightMode()
                    restart()
                }
            } else {
                if (DayModeUtil.isNightMode(requireContext())) {
                    DayModeUtil.setLightMode()
                    restart()
                }
            }
        })
        viewModel.uc.switchSysModeEvent.observe(this, { checked ->
            // 跟随系统模式关闭后 需要给下面的黑夜模式开关进行判断并设置当前app模式
            if (!checked) {
                viewModel.model.saveUiMode(DayModeUtil.isNightMode(requireContext()))
                binding.swNight.isChecked = DayModeUtil.isNightMode(requireContext())
                if (DayModeUtil.isNightMode(requireContext())) {
                    DayModeUtil.setNightMode()
                } else {
                    DayModeUtil.setLightMode()
                }
            }
            if (viewModel.model.getFollowSysUiModeFlag() && checked) {
                return@observe
            }
            viewModel.model.saveFollowSysModeFlag(checked)
            if (checked) {
                DayModeUtil.autoModeBySys()
                restart()
            }
        })
        viewModel.uc.confirmLogoutEvent.observe(this, {
            DialogHelper.showBaseDialog(requireContext(), "注销", "是否确定退出登录？") {
                viewModel.logout()
            }
        })
    }

    private fun restart() {
        back()
        startContainerActivity(AppConstants.Router.User.F_USER_SETTING)
        activity?.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
    }
}