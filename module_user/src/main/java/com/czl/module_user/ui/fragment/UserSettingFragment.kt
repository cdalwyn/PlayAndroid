package com.czl.module_user.ui.fragment

import androidx.appcompat.app.AppCompatDelegate
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.SpHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentSettingBinding
import com.czl.module_user.viewmodel.UserSettingVm
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

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
        viewModel.tvTitle.set("设置")
        binding.swNight.isChecked = DayModeUtil.isNightMode(requireContext())
        binding.swSys.isChecked = viewModel.model.getFollowSysUiModeFlag()
    }

    override fun initViewObservable() {
        viewModel.uc.switchUiModeEvent.observe(this, { checked ->
            if (checked) {
                DayModeUtil.setNightMode(requireContext())
                if (!DayModeUtil.isNightMode(requireContext())) {
                    back()
                    startContainerActivity(AppConstants.Router.User.F_USER_SETTING)
                    activity?.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
                }
            } else {
                DayModeUtil.setLightMode(requireContext())
                if (DayModeUtil.isNightMode(requireContext())) {
                    back()
                    startContainerActivity(AppConstants.Router.User.F_USER_SETTING)
                    activity?.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
                }
            }
        })
        viewModel.uc.switchSysModeEvent.observe(this, { checked ->
            if (checked) {
                DayModeUtil.autoModeBySys()
                back()
                startContainerActivity(AppConstants.Router.User.F_USER_SETTING)
                activity?.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
            }else{
                binding.swNight.isChecked = DayModeUtil.isNightMode(requireContext())
                viewModel.model.saveUiMode(checked)
            }
        })
    }
}