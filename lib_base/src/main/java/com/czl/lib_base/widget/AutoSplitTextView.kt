package com.czl.lib_base.widget

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

class AutoSplitTextView : AppCompatTextView {
    private var mEnabled = true

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    )

    fun setAutoSplitEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(
                heightMeasureSpec
            ) == MeasureSpec.EXACTLY && width > 0 && height > 0 && mEnabled
        ) {
            val newText = autoSplitText(this)
            if (!TextUtils.isEmpty(newText)) {
                text = newText
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun autoSplitText(tv: TextView): String {
        val rawText = tv.text.toString() //原始文本
        val tvPaint: Paint = tv.paint //paint，包含字体等信息
        val tvWidth = (tv.width - tv.paddingLeft - tv.paddingRight).toFloat() //控件可用宽度

        //将原始文本按行拆分
        val rawTextLines = rawText.replace("\r".toRegex(), "").split("\n".toRegex()).toTypedArray()
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
}