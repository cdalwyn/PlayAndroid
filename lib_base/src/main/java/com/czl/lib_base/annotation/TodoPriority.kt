package com.czl.lib_base.annotation

import androidx.annotation.IntDef

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
@IntDef(TodoPriority.HIGH, TodoPriority.NORMAL, TodoPriority.LOW)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TodoPriority {
    companion object {
        const val HIGH = 1
        const val NORMAL = 2
        const val LOW = 3
    }
}