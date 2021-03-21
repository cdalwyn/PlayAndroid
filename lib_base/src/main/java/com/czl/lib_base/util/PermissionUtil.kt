package com.czl.lib_base.util

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback

/**
 * @author Alwyn
 * @Date 2020/10/21
 * @Description 权限控制管理类 回调在业务层处理
 */
object PermissionUtil {
    fun reqStorage(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        callback: RequestCallback
    ) {
        if (activity != null) {
            PermissionX.init(activity)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList, "应用需要获取权限存储临时数据", "确定", "取消"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "你需要手动设置授予必要的权限", "确定", "取消")
                }
                .request(callback)
            return
        }
        if (fragment != null) {
            PermissionX.init(fragment)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList, "应用需要获取权限存储临时数据", "确定", "取消"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "你需要手动设置授予必要的权限", "确定", "取消")
                }
                .request(callback)
        }
    }

    fun reqCamera(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        callback: RequestCallback
    ){
        if (activity != null) {
            PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList, "应用需要获取相机权限进行扫码功能", "确定", "取消"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "你需要手动设置授予必要的权限", "确定", "取消")
                }
                .request(callback)
            return
        }
        if (fragment != null) {
            PermissionX.init(fragment)
                .permissions(Manifest.permission.CAMERA)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList, "应用需要获取相机权限进行扫码功能", "确定", "取消"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "你需要手动设置授予必要的权限", "确定", "取消")
                }
                .request(callback)
        }
    }

    fun reqStorageAndAudio(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        callback: RequestCallback
    ){
        if (activity != null) {
            PermissionX.init(activity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList, "应用需要获取存储和录制音频权限进行意见反馈功能", "确定", "取消"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "你需要手动设置授予必要的权限", "确定", "取消")
                }
                .request(callback)
            return
        }
        if (fragment != null) {
            PermissionX.init(fragment)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList, "应用需要获取存储和录制音频权限进行意见反馈功能", "确定", "取消"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "你需要手动设置授予必要的权限", "确定", "取消")
                }
                .request(callback)
        }
    }
}