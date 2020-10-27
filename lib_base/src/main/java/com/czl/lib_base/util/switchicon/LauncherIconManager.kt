package com.czl.lib_base.util.switchicon

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

object LauncherIconManager {

    /** 切换图标任务Map */
    private val taskMap: LinkedHashMap<String, SwitchIconTask> = LinkedHashMap()

    /**
     * 注册以监听应用运行状态
     */
    fun register(application: Application) {
        RunningStateRegister.register(application, object: RunningStateRegister.StateCallback{
            override fun onForeground() {
            }

            override fun onBackground() {
                proofreadingInOrder(application)
            }
        })
    }

    /**
     * 依次校对预设时间
     * @param context 上下文
     */
    fun proofreadingInOrder(context: Context) {
        for (task in taskMap.values) {
            if (proofreading(context, task)) break
        }
    }

    /**
     * 校对预设时间/过期时间
     * @param context 上下文
     * @return true 已过预设时间      false 未达预设时间或已过期
     */
    private fun proofreading(context: Context, task: SwitchIconTask) =
        when {
            isPassedOutDateTime(task) -> {
                disableComponent(context, ActivityUtil.getLauncherActivityName(context)!!)
                enableComponent(context, task.launcherComponentClassName)
                false
            }
            isPassedPresetTime(task) -> {
                disableComponent(context, ActivityUtil.getLauncherActivityName(context)!!)
                enableComponent(context, task.aliasComponentClassName)
                true
            }
            else -> false
        }

    /**
     * 是否已超过预设时间
     * @param task 任务
     */
    private fun isPassedPresetTime(task: SwitchIconTask) =
        System.currentTimeMillis() > task.presetTime

    /**
     * 是否已超过过期时间
     * @param task 任务
     *
     */
    private fun isPassedOutDateTime(task: SwitchIconTask) =
        System.currentTimeMillis() > task.outDateTime

    /**
     * 添加图标切换任务
     * @param newTasks 新任务，可以传多个
     */
    fun addNewTask(vararg newTasks: SwitchIconTask) {
        for (newTask in newTasks) {
            // 防止重复添加任务
            if (taskMap.containsKey(newTask.aliasComponentClassName)) return

            // 校验任务的预设时间和过期时间
            for (queuedTask in taskMap.values) {
                if (newTask.presetTime > newTask.outDateTime) throw IllegalArgumentException("非法的任务预设时间${newTask.presetTime}, 不能晚于过期时间")
                if (newTask.presetTime <= queuedTask.outDateTime) throw IllegalArgumentException("非法的任务预设时间${newTask.presetTime}, 不能早于已添加任务的过期时间")
            }

            taskMap[newTask.aliasComponentClassName] = newTask
        }
    }

    /**
     * 启用组件
     * @param context 上下文
     * @param className 组件类名
     */
    private fun enableComponent(context: Context, className: String) {
        val componentName = ComponentName(context, className)

        if (isComponentEnabled(context, componentName)) return //已经启用

        context.packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * 禁用组件
     * @param context 上下文
     * @param className 组件类名
     */
    private fun disableComponent(context: Context, className: String) {
        val componentName = ComponentName(context, className)

        if (isComponentDisabled(context, componentName)) return  // 已经禁用

        context.packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * 组件是否处于可用状态
     * @param context 上下文
     * @param componentName 组件名
     */
    @JvmStatic
    fun isComponentEnabled(context: Context, componentName: ComponentName): Boolean {
        val state: Int = context.packageManager.getComponentEnabledSetting(componentName)
        return PackageManager.COMPONENT_ENABLED_STATE_ENABLED == state
    }

    /**
     * 组件是否处于禁用状态
     * @param context 上下文
     * @param componentName 组件名
     */
    @JvmStatic
    fun isComponentDisabled(context: Context, componentName: ComponentName): Boolean {
        val state: Int = context.packageManager.getComponentEnabledSetting(componentName)
        return PackageManager.COMPONENT_ENABLED_STATE_DISABLED == state
    }


}