package com.czl.lib_base.util.switchicon

/**
 * 切换图标任务
 */
data class SwitchIconTask (val launcherComponentClassName: String,  // 启动器组件类名
                           val aliasComponentClassName: String,  // 别名组件类名
                           val presetTime: Long,            // 预设时间
                           val outDateTime: Long)           // 过期时间