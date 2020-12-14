package com.czl.lib_base.util;

import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Alwyn on 2020/10/10.
 * 缓存封装类
 */
public class SpHelper {

    static {
        mv = MMKV.defaultMMKV();
    }

    private static MMKV mv;

    private SpHelper() {
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void encode(String key, Object object) {
        if (object instanceof String) {
            mv.encode(key, (String) object);
        } else if (object instanceof Integer) {
            mv.encode(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mv.encode(key, (Boolean) object);
        } else if (object instanceof Float) {
            mv.encode(key, (Float) object);
        } else if (object instanceof Long) {
            mv.encode(key, (Long) object);
        } else if (object instanceof Double) {
            mv.encode(key, (Double) object);
        } else if (object instanceof byte[]) {
            mv.encode(key, (byte[]) object);
        } else {
            mv.encode(key, object.toString());
        }
    }

    public static void encodeSet(String key, Set<String> sets) {
        mv.encode(key, sets);
    }

    public static void encodeParcelable(String key, Parcelable obj) { mv.encode(key, obj); }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Integer decodeInt(String key) {
        return mv.decodeInt(key, 0);
    }

    public static Double decodeDouble(String key) {
        return mv.decodeDouble(key, 0.00);
    }

    public static Long decodeLong(String key) {
        return mv.decodeLong(key, 0L);
    }

    public static Boolean decodeBoolean(String key) {
        return mv.decodeBool(key, false);
    }

    public static Float decodeFloat(String key) {
        return mv.decodeFloat(key, 0F);
    }

    public static byte[] decodeBytes(String key) {
        return mv.decodeBytes(key);
    }

    public static String decodeString(String key) {
        return mv.decodeString(key, "");
    }

    public static Set<String> decodeStringSet(String key) {
        return mv.decodeStringSet(key, Collections.<String>emptySet());
    }

    public static Parcelable decodeParcelable(String key) {
        return mv.decodeParcelable(key, null);
    }

    /**
     * 移除某个key对
     *
     * @param key
     */
    public static void removeKey(String key) {
        mv.removeValueForKey(key);
    }

    /**
     * 清除所有key
     */
    public static void clearAll() {
        mv.clearAll();
    }

}
