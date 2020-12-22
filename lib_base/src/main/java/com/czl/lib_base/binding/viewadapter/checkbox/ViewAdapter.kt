package com.czl.lib_base.binding.viewadapter.checkbox

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.czl.lib_base.binding.command.BindingCommand

object ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @JvmStatic
    @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
    fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean?>) {
        checkBox.setOnCheckedChangeListener { compoundButton, b -> bindingCommand.execute(b) }
    }
}