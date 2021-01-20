package com.czl.lib_base.binding.viewadapter.mswitch

import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.czl.lib_base.binding.command.BindingCommand

object ViewAdapter {
    /**
     * 设置开关状态
     *
     * @param mSwitch Switch控件
     */
    @JvmStatic
    @BindingAdapter("switchState")
    fun setSwitchState(mSwitch: SwitchCompat, isChecked: Boolean) {
        mSwitch.isChecked = isChecked
    }

    /**
     * Switch的状态改变监听
     *
     * @param mSwitch        Switch控件
     * @param changeListener 事件绑定命令
     */
    @JvmStatic
    @BindingAdapter("onSwitchCheckedCommand")
    fun onCheckedChangeCommand(mSwitch: SwitchCompat, changeListener: BindingCommand<Boolean>?) {
        if (changeListener != null) {
            mSwitch.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                changeListener.execute(
                    isChecked
                )
            }
        }
    }
}