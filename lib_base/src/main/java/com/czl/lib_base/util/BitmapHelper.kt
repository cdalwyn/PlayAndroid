package com.czl.lib_base.util

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * @author Alwyn
 * @Date 2021/6/27
 * @Description
 */
object BitmapHelper {
    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    fun scaleBitmap(origin: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        if (origin == null) {
            return null
        }
        val height = origin.height
        val width = origin.width
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight) // 使用后乘
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (!origin.isRecycled) {
            origin.recycle()
        }
        return newBM
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    fun scaleBitmap(origin: Bitmap?, ratio: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.preScale(ratio, ratio)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        var cropWidth = Math.min(w, h) // 裁切后所取的正方形区域边长
        cropWidth /= 2
        val cropHeight = (cropWidth / 1.2).toInt()
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false)
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    fun rotateBitmap(origin: Bitmap?, alpha: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.setRotate(alpha)
        // 围绕原地进行旋转
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    fun skewBitmap(origin: Bitmap?): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.postSkew(-0.6f, -0.3f)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }
}