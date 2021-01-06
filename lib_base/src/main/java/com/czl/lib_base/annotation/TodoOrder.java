package com.czl.lib_base.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
@IntDef({TodoOrder.finishAsc,TodoOrder.finishDesc,TodoOrder.createAsc,TodoOrder.createDesc})
@Retention(RetentionPolicy.SOURCE)
public @interface TodoOrder {
    // 完成日期顺序
    int finishAsc = 1;
    // 倒序
    int finishDesc = 2;
    // 创建日期顺序
    int createAsc = 3;
    int createDesc = 4;
}
