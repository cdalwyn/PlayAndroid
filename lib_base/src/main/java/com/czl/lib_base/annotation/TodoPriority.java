package com.czl.lib_base.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
@IntDef({TodoPriority.HIGH,TodoPriority.NORMAL,TodoPriority.LOW})
@Retention(RetentionPolicy.SOURCE)
public @interface TodoPriority {
    int HIGH = 1;
    int NORMAL = 2;
    int LOW = 3;
}
