package com.czl.lib_base.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*

/**
 * @author Alwyn
 * @Date 2020/4/20
 * @Description 生成二维码
 */
object QRCodeUtil {
    /**
     * 生成简单二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @return BitMap
     */
    fun createQRCodeBitmap(
        content: String?, width: Int, height: Int,
        character_set: String?, error_correction_level: String?,
        margin: String?, color_black: Int, color_white: Int
    ): Bitmap? {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null
        }
        // 宽和高>=0
        return if (width < 0 || height < 0) {
            null
        } else try {
            /** 1.设置二维码相关配置  */
            val hints = Hashtable<EncodeHintType, String?>()
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints[EncodeHintType.CHARACTER_SET] = character_set
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = color_black //黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 向二维码中间添加logo图片(图片合成)
     *
     * @param srcBitmap   原图片（生成的简单二维码图片）
     * @param logoBitmap  logo图片
     * @param logoPercent 百分比 (用于调整logo图片在原图片中的显示大小, 取值范围[0,1] )
     * @return
     */
    private fun addLogo(srcBitmap: Bitmap?, logoBitmap: Bitmap?, logoPercent: Float): Bitmap? {
        var logoPercent = logoPercent
        if (srcBitmap == null) {
            return null
        }
        if (logoBitmap == null) {
            return srcBitmap
        }
        //传值不合法时使用0.2F
        if (logoPercent < 0f || logoPercent > 1f) {
            logoPercent = 0.2f
        }
        /** 1. 获取原图片和Logo图片各自的宽、高值  */
        val srcWidth = srcBitmap.width
        val srcHeight = srcBitmap.height
        val logoWidth = logoBitmap.width
        val logoHeight = logoBitmap.height
        /** 2. 计算画布缩放的宽高比  */
        val scaleWidth = srcWidth * logoPercent / logoWidth
        val scaleHeight = srcHeight * logoPercent / logoHeight
        /** 3. 使用Canvas绘制,合成图片  */
        val bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(srcBitmap, 0f, 0f, null)
        canvas.scale(scaleWidth, scaleHeight, (srcWidth / 2).toFloat(), (srcHeight / 2).toFloat())
        canvas.drawBitmap(
            logoBitmap,
            (srcWidth / 2 - logoWidth / 2).toFloat(),
            (srcHeight / 2 - logoHeight / 2).toFloat(),
            null
        )
        return bitmap
    }

    /**
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @param logoBitmap             logo图片
     * @param logoPercent            logo所占百分比
     * @return
     */
    fun createQRCodeBitmap(
        content: String?, width: Int, height: Int, character_set: String?,
        error_correction_level: String?, margin: String?, color_black: Int,
        color_white: Int, logoBitmap: Bitmap?, logoPercent: Float
    ): Bitmap? {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null
        }
        // 宽和高>=0
        return if (width < 0 || height < 0) {
            null
        } else try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
            val hints = Hashtable<EncodeHintType, String?>()
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints[EncodeHintType.CHARACTER_SET] = character_set
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = color_black //黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            /** 5.为二维码添加logo图标  */
            if (logoBitmap != null) {
                addLogo(bitmap, logoBitmap, logoPercent)
            } else bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 生成自定义二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @param logoBitmap             logo图片（传null时不添加logo）
     * @param logoPercent            logo所占百分比
     * @param bitmap_black           用来代替黑色色块的图片（传null时不代替）
     * @return
     */
    fun createQRCodeBitmap(
        content: String?,
        width: Int,
        height: Int,
        character_set: String?,
        error_correction_level: String?,
        margin: String?,
        color_black: Int,
        color_white: Int,
        logoBitmap: Bitmap?,
        logoPercent: Float,
        bitmap_black: Bitmap?
    ): Bitmap? {
        // 字符串内容判空
        var bitmap_black = bitmap_black
        if (TextUtils.isEmpty(content)) {
            return null
        }
        // 宽和高>=0
        return if (width < 0 || height < 0) {
            null
        } else try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
            val hints = Hashtable<EncodeHintType, String?>()
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints[EncodeHintType.CHARACTER_SET] = character_set
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            if (bitmap_black != null) {
                //从当前位图按一定的比例创建一个新的位图
                bitmap_black = Bitmap.createScaledBitmap(bitmap_black, width, height, false)
            }
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix[x, y]) { // 黑色色块像素设置
                        if (bitmap_black != null) { //图片不为null，则将黑色色块换为新位图的像素。
                            pixels[y * width + x] = bitmap_black.getPixel(x, y)
                        } else {
                            pixels[y * width + x] = color_black
                        }
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            /** 5.为二维码添加logo图标  */
            if (logoBitmap != null) {
                addLogo(bitmap, logoBitmap, logoPercent)
            } else bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * //     * @param logoBitmap             logo图片
     * //     * @param logoPercent            logo所占百分比
     * @return
     */
    fun createQRCode(
        content: String?, width: Int, height: Int, character_set: String?,
        error_correction_level: String?, margin: String?, color_black: Int, color_white: Int
    ): Bitmap? {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null
        }
        // 宽和高>=0
        return if (width < 0 || height < 0) {
            null
        } else try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
            val hints = Hashtable<EncodeHintType, String?>()
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints[EncodeHintType.CHARACTER_SET] = character_set
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = color_black //黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            /** 5.为二维码添加logo图标  */

//            if (logoBitmap != null) {
//                return ImageTextUtils.INSTANCE.drawTextToBitmap(bitmap, desc, topTitle);
//            }
            bitmap
            //            return TextUtils.isEmpty(desc) && TextUtils.isEmpty(topTitle) ? bitmap :
//                    ImageTextUtils.INSTANCE.drawTextToBitmap(bitmap, desc, topTitle);
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * view转bitmap
     * 这里的view只能是已经显示在界面上的
     */
    fun viewConvert2Bitmap(v: View): Bitmap {
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h)
        v.draw(c)
        return bmp
    }
    /**
     * 生成图片  加上title的图片
     * @param title
     * @return
     */
    //    public static Bitmap createImage(Bitmap image, String title) {
    //        int picWidth = 400;//生成图片的宽度
    //        int picHeight = 400;//生成图片的高度
    //        int titleTextSize = 30;
    //        int contentTextSize = 22;
    //        int textColor = Color.BLACK;
    //        int qrWidth = 300;
    //        int qrHeight = 300;
    //        int paddingTop = 152;
    //        int paddingMiddle = 40;
    //        int paddingBottom = 24;
    //
    //        //最终生成的图片
    //        Bitmap result = Bitmap.createBitmap(picWidth, picHeight, Bitmap.Config.ARGB_8888);
    //
    //        Paint paint = new Paint();
    //        paint.setColor(Color.WHITE);
    //        Canvas canvas = new Canvas(result);
    //
    //        //先画一整块白色矩形块
    //        canvas.drawRect(0, 0, picWidth, picHeight, paint);
    //
    //        //画title文字
    //        Rect bounds = new Rect();
    //        paint.setColor(textColor);
    //        paint.setTextSize(titleTextSize);
    //        //获取文字的字宽高，以便将文字与图片中心对齐
    //        paint.getTextBounds(title, 0, title.length(), bounds);
    //        canvas.drawText(title, picWidth / 2 - bounds.width() / 2, paddingTop + bounds.height() / 2, paint);
    //
    //        //画白色矩形块
    //        int qrTop = paddingTop + titleTextSize + paddingMiddle;//二维码的顶部高度
    //
    //        paint.setColor(Color.BLACK);
    //        canvas.drawBitmap(image, (picWidth - qrWidth) / 2, qrTop, paint);
    //
    //        //画文字
    //        paint.setColor(Color.BLACK);
    //        paint.setTextSize(contentTextSize);
    //        int lineTextCount = (int) ((picWidth - 50) / contentTextSize);
    //        int line = (int) (Math.ceil(Double.valueOf(content.length()) / Double.valueOf(lineTextCount)));
    //        int textTop = qrTop + qrHeight + paddingBottom;//地址的顶部高度
    //
    //        for (int i = 0; i < line; i++) {
    //            String s;
    //            if (i == line - 1) {
    //                s = content.substring(i * lineTextCount, content.length());
    //            } else {
    //                s = content.substring(i * lineTextCount, (i + 1) * lineTextCount);
    //            }
    //            paint.getTextBounds(content, 0, s.length(), bounds);
    //
    //            canvas.drawText(s, picWidth / 2 - bounds.width() / 2, textTop + i * contentTextSize + i * 5 + bounds.height() / 2, paint);
    //        }
    //
    //        canvas.save();
    //        canvas.restore();
    //
    //        return result;
    //    }
}