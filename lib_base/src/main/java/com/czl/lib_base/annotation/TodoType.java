package com.czl.lib_base.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
@IntDef({TodoType.ALL,TodoType.WORK,TodoType.STUDY,TodoType.LIFE,TodoType.PAY,TodoType.PLAY,TodoType.FAMILY})
@Retention(RetentionPolicy.SOURCE)
public @interface TodoType {
    int ALL = 0;
    int WORK = 1;
    int STUDY = 2;
    int LIFE = 3;
    int PAY = 4;
    int PLAY = 5;
    int FAMILY = 6;
}
