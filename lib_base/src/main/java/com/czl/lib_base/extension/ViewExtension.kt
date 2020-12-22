package com.czl.lib_base.extension

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.czl.lib_base.util.RxThreadHelper
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认1500毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 800): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(it as T)
    }
}

fun <T:View> T.longClick(block: (T) -> Unit) = setOnLongClickListener{
    block(it as T)
    true
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认800毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 800, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

@SuppressLint("CheckResult")
fun <T : EditText> T.textChangeDebounce(time: Long = 500, block: (String) -> Unit) {
    textChanges().skip(1).debounce(time, TimeUnit.MILLISECONDS)
        .compose(RxThreadHelper.rxSchedulerHelper(this))
        .map { it.toString() }
        .subscribe { block(it) }
}

@SuppressLint("CheckResult")
fun <T : EditText> T.textChangeSubscribe(block: (String) -> Unit): Disposable {
    return textChanges()
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.toString() }
        .subscribe(block)
}

/*解决TextView自动换行问题*/
fun <T : TextView> T.autoSpilt(): String {
    val rawText: String = text.toString() //原始文本
    val tvPaint: Paint = paint //paint，包含字体等信息
    val tvWidth: Float = width - paddingLeft - paddingRight.toFloat() //控件可用宽度
    //将原始文本按行拆分
    val rawTextLines =
        rawText.replace("\r".toRegex(), "").split("\n".toRegex()).toTypedArray()
    val sbNewText = StringBuilder()
    for (rawTextLine in rawTextLines) {
        if (tvPaint.measureText(rawTextLine) <= tvWidth) {
            //如果整行宽度在控件可用宽度之内，就不处理了
            sbNewText.append(rawTextLine)
        } else {
            //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
            var lineWidth = 0f
            var cnt = 0
            while (cnt != rawTextLine.length) {
                val ch = rawTextLine[cnt]
                lineWidth += tvPaint.measureText(ch.toString())
                if (lineWidth <= tvWidth) {
                    sbNewText.append(ch)
                } else {
                    sbNewText.append("\n")
                    lineWidth = 0f
                    --cnt
                }
                ++cnt
            }
        }
        sbNewText.append("\n")
    }
    //把结尾多余的\n去掉
    if (!rawText.endsWith("\n")) {
        sbNewText.deleteCharAt(sbNewText.length - 1)
    }
    return sbNewText.toString()
}