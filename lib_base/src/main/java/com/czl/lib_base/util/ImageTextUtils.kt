package com.czl.lib_base.util

import android.graphics.*
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import kotlin.math.ceil


/**
 * @desciption: 图片加上文字
 */
object ImageTextUtils {

    /**传递进来的源图片*/
    private var mBitmapSource: Bitmap? = null

    /**图片的配文*/
    private var mText: String? = null

    /**图片加上配文后生成的新图片*/
    private var mNewBitmap: Bitmap? = null

    /**配文的颜色*/
    private var mTextColor: Int = Color.BLACK

    /**配文的字体大小*/
    private var mTextSize: Float = 24f

    /**图片的宽度*/
    private var mBitmapWidth: Int = 0

    /**图片的高度*/
    private var mBitmapHeight: Int = 0

    /**画图片的画笔*/
    private var mBitmapPaint: Paint? = null

    /**画文字的画笔*/
    private var mTextPaint: Paint? = null

    /**配文与图片间的距离*/
    private var mPadding: Float = SizeUtils.dp2px(1F).toFloat()

    /**配文行与行之间的距离*/
    private var mLinePadding: Float = SizeUtils.dp2px(10F).toFloat()

    init {
        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    fun drawTextToBitmap(
        bitmapSource: Bitmap,
        desc: String,
        topTitle: String
    ): Bitmap? {
//        val bitmapSource = BitmapFactory.decodeResource(context.resources, imgId)
        mBitmapWidth = bitmapSource.width
        mBitmapHeight = bitmapSource.height
        // 1. 一行可以显示文字的个数
        val lineTextCount = ((mBitmapWidth - 20) / mTextSize).toInt()
        // 2. 一共要把文字分为几行
        val line = ceil(desc.length.toDouble() / lineTextCount.toDouble()).toInt()

        val titleLine = ceil(topTitle.length.toDouble() / lineTextCount.toDouble()).toInt()
        //新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
        //宽度就是图片宽度，高度是图片高度+配文与图片间的间距+文字大小*文字行数+文字间的行间距*文字行数；
//        val totalHeight = mBitmapHeight + mPadding + mTextSize * line + mLinePadding * line
        // 3. 图片总高度
        val totalHeight = mBitmapHeight + mPadding + mLinePadding * (line + titleLine)
        // 最后生成的bitmap
        mNewBitmap = Bitmap.createBitmap(
            mBitmapWidth,
            totalHeight.toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(mNewBitmap!!)
        mTextPaint?.color = Color.WHITE
        // 4. 先画整张图的背景白板 防止文字是透明背景，在有些情况下保存到本地后看不出来
        canvas.drawRect(0f, 0f, mBitmapWidth.toFloat(), totalHeight, mTextPaint!!)
        // 5. 画二维码 y轴从 标题的高度 开始画
        canvas.drawBitmap(bitmapSource, 0f, mLinePadding * titleLine, mBitmapPaint)
        // 6. 在图片下边画一个白色矩形块用来放描述 y轴从 二维码高度+标题高度 开始画
        canvas.drawRect(
            0f, mBitmapHeight.toFloat() + mLinePadding * titleLine, mBitmapWidth.toFloat(),
            totalHeight, mTextPaint!!
        )
        // 改变画笔颜色
        mTextPaint?.color = mTextColor
        mTextPaint?.textSize = mTextSize
        // 7. 开始循环画标题
        val bounds = Rect()
        for (x in 0 until titleLine) {
            val str: String = if (x == titleLine - 1) {
                topTitle.substring(x * lineTextCount, topTitle.length)
            } else {//不是最后一行
                topTitle.substring(x * lineTextCount, (x + 1) * lineTextCount)
            }
            mTextPaint?.getTextBounds(str, 0, str.length, bounds)
            // x轴标题呈居中
            canvas.drawText(
                str, (mBitmapWidth / 2 - bounds.width() / 2).toFloat(),
                25f + x * mLinePadding + bounds.height() / 2, mTextPaint!!
            )
        }
        // 8. 开始画描述
        val boundsRect = Rect()
        //开启循环直到画完所有行的文字
        for (i in 0 until line) {
            val str: String = if (i == line - 1) {
                desc.substring(i * lineTextCount, desc.length)
            } else {//不是最后一行
                desc.substring(i * lineTextCount, (i + 1) * lineTextCount)
            }
            //如果是最后一行，则结束位置就是文字的长度，别下标越界哦
            //获取文字的字宽高以便把文字与图片中心对齐
            mTextPaint?.getTextBounds(str, 0, str.length, boundsRect)
            //画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(
                str,
                (mBitmapWidth / 2 - boundsRect.width() / 2).toFloat(),
                mBitmapHeight + mPadding + i * mLinePadding + boundsRect.height() / 2 + mLinePadding * titleLine,
                mTextPaint!!
            )
        }
        canvas.save()
        canvas.restore()
        bitmapSource.recycle()
        return mNewBitmap
    }


//    private fun dp2px(value: Int): Int {
//        val v = context.resources.displayMetrics.density
//        return (v * value + 0.5f).toInt()
//    }

//    private fun sp2px(value: Int): Int {
//        val v = context.resources.displayMetrics.scaledDensity
//        return (v * value + 0.5f).toInt()
//    }
}