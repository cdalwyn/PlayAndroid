package com.czl.lib_base.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.afollestad.date.DatePicker
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.datetime.DateTimeCallback
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.TimeUtils
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseFragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Alwyn
 * @Date 2020/12/1
 * @Description
 */
object DialogHelper {
    fun showBaseDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context).asConfirm(title, content, OnConfirmListener(func)).show()
    }

    fun showLoadingDialog(context: Context, title: String? = "加载中"): BasePopupView {
        return XPopup.Builder(context).asLoading(title, R.layout.common_loading_dialog).show()
    }

    fun showNoCancelDialog(
        context: Context,
        title: String,
        content: String,
        func: () -> Unit
    ): BasePopupView {
        return XPopup.Builder(context)
            .asConfirm(title, content, "取消", "确定", OnConfirmListener(func), null, true).show()
    }

    fun showDateDialog(
        activity: BaseActivity<*, *>,
        dateStr: String? = null,
        dateTimeCallback: DateTimeCallback
    ) {
        MaterialDialog(activity)
            .show {
                noAutoDismiss()
                getActionButton(WhichButton.POSITIVE).updateTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.md_theme_red
                    )
                )
                getActionButton(WhichButton.NEGATIVE).updateTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.md_grey
                    )
                )
                neutralButton(text = "回到今天",click = object :DialogCallback{
                    override fun invoke(dialog: MaterialDialog) {
                        dialog.findViewById<DatePicker>(R.id.datetimeDatePicker).setDate(Calendar.getInstance().apply { time = Date() })
                    }
                })
                negativeButton { dismiss() }
                datePicker(
                    currentDate = if (dateStr.isNullOrEmpty()) null else Calendar.getInstance()
                        .apply {
                               time = TimeUtils.string2Date(dateStr,"yyyy-MM-dd")
                        }, dateCallback = dateTimeCallback
                )
                lifecycleOwner(activity)
            }
    }
}