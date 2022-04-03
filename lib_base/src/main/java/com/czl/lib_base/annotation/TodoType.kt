package com.czl.lib_base.annotation

import androidx.annotation.IntDef

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
@IntDef(
    TodoType.ALL,
    TodoType.WORK,
    TodoType.STUDY,
    TodoType.LIFE,
    TodoType.PAY,
    TodoType.PLAY,
    TodoType.FAMILY
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TodoType {
    companion object {
        const val ALL = 0
        const val WORK = 1
        const val STUDY = 2
        const val LIFE = 3
        const val PAY = 4
        const val PLAY = 5
        const val FAMILY = 6
    }
}