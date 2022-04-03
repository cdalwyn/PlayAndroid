package com.czl.lib_base.annotation

import androidx.annotation.IntDef

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
@IntDef(TodoOrder.finishAsc, TodoOrder.finishDesc, TodoOrder.createAsc, TodoOrder.createDesc)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TodoOrder {
    companion object {
        // 完成日期顺序
        const val finishAsc = 1
        // 倒序
        const val finishDesc = 2
        // 创建日期顺序
        const val createAsc = 3
        const val createDesc = 4
    }
}