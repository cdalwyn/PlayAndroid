package com.czl.lib_base.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.czl.lib_base.extension.SingleMediaScanner
import java.io.*


fun saveBitmapToExternalCachePath(bitmap: Bitmap, fileName: String) {
    val dir = PathUtils.getExternalAppCachePath() + "/download_tmp/"
    val dirFile = File(dir)
    if (!dirFile.exists()) {
        dirFile.mkdirs()
    }
    val file = File(dirFile, "$fileName.jpg")
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        out?.flush()
        out?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * 保存图片到 /Pictures/AppName
 */
fun saveBitmapToPicturesPath(context: Context?, bitmap: Bitmap, fileName: String) {
    try {
        val dir = PathUtils.getExternalPicturesPath() + "/${AppUtils.getAppName()}/"
        val dirFile = File(dir)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        val file = File(dirFile, "$fileName.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
        // 刷新相册
        SingleMediaScanner(context, file, object : SingleMediaScanner.ScanListener {
            override fun onScanFinish() {
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 获取 /Pictures/AppName 路径
 */
fun getBitmapDownloadPicturesPath(): String {
    return PathUtils.getExternalPicturesPath() + "/${AppUtils.getAppName()}/"
}

fun getBitmapDownloadPath(): String {
    val dir = PathUtils.getExternalAppCachePath() + "/download_tmp/"
    val dirFile = File(dir)
    if (!dirFile.exists()) {
        dirFile.mkdirs()
    }
    return dir
}

fun getBitmapDownloadFilesPath(): List<File> {
    return FileUtils.listFilesInDir(getBitmapDownloadPath())
}

/**
 * 保存图片到picture 目录，Android Q适配，最简单的做法就是保存到公共目录，不用SAF存储
 *
 * @param context
 * @param bitmap
 * @param fileName
 */
fun savePictureToAlbum(context: Context, bitmap: Bitmap, fileName: String): Boolean {
    val contentValues = ContentValues()
    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
    contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName)
    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    val uri =
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    var outputStream: OutputStream? = null
    try {
        outputStream = context.contentResolver.openOutputStream(uri!!)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream!!.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}