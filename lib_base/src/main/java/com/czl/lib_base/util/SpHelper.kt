package com.czl.lib_base.util

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * Created by Alwyn on 2020/10/10.
 * 缓存封装类
 */
object SpHelper {
    private var mv: MMKV = MMKV.defaultMMKV()

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param value
     */
    fun encode(key: String?, value: Any) {
        when (value) {
            is String -> {
                mv.encode(key, value)
            }
            is Int -> {
                mv.encode(key, value)
            }
            is Boolean -> {
                mv.encode(key, value)
            }
            is Float -> {
                mv.encode(key, value)
            }
            is Long -> {
                mv.encode(key, value)
            }
            is Double -> {
                mv.encode(key, value)
            }
            is ByteArray -> {
                mv.encode(key, value)
            }
            else -> {
                mv.encode(key, value.toString())
            }
        }
    }

    fun encodeSet(key: String?, sets: Set<String>) {
        mv.encode(key, sets)
    }

    fun encodeParcelable(key: String?, obj: Parcelable?) {
        mv.encode(key, obj)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    fun decodeInt(key: String?): Int {
        return mv.decodeInt(key, 0)
    }

    fun decodeDouble(key: String?): Double {
        return mv.decodeDouble(key, 0.00)
    }

    fun decodeLong(key: String?): Long {
        return mv.decodeLong(key, 0L)
    }

    fun decodeBoolean(key: String?): Boolean {
        return mv.decodeBool(key, false)
    }

    fun decodeBoolean(key: String?, def: Boolean): Boolean {
        return mv.decodeBool(key, def)
    }

    fun decodeFloat(key: String?): Float {
        return mv.decodeFloat(key, 0f)
    }

    fun decodeBytes(key: String?): ByteArray {
        return mv.decodeBytes(key)
    }

    fun decodeString(key: String?): String {
        return mv.decodeString(key, "")
    }

    fun decodeStringSet(key: String?): Set<String> {
        return mv.decodeStringSet(key, linkedSetOf())
    }

    fun <T : Parcelable> decodeParcelable(key: String?, tClass: Class<T>): Parcelable? {
        return mv.decodeParcelable(key, tClass)
    }

    /**
     * 清除所有key
     */
    fun clearAll() {
        mv.clearAll()
    }

    /**
     * 获取所有key
     */
    fun getAllKeys(): List<String> {
        return mv.allKeys().toList()
    }

    /**
     * 是否包含某个key
     */
    fun containKey(key: String): Boolean {
        return mv.containsKey(key)
    }

    /**
     * 移除指定key的value
     */
    fun removeValueForKey(key: String) {
        mv.removeValueForKey(key)
    }

    /**
     * 移除指定key集合的value
     */
    fun removeValuesForKeys(keys: Array<String>) {
        mv.removeValuesForKeys(keys)
    }
}