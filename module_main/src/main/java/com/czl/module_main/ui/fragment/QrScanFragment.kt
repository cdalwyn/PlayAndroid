package com.czl.module_main.ui.fragment

import android.content.Intent
import android.os.Bundle
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.MyGlideEngine
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.PermissionUtil
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.databinding.MainFragmentQrScanBinding
import com.czl.module_main.viewmodel.QrScanViewModel
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType

/**
 * @author Alwyn
 * @Date 2021/1/21
 * @Description
 */
@Route(path = AppConstants.Router.Main.F_QR_SCAN)
class QrScanFragment : BaseFragment<MainFragmentQrScanBinding, QrScanViewModel>(),
    QRCodeView.Delegate {

    companion object {
        const val ALBUM_REQUEST_CODE = 108
    }

    override fun initContentView(): Int {
        return R.layout.main_fragment_qr_scan
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("扫一扫")
        viewModel.toolbarRightText.set("相册")
        binding.scanView.setDelegate(this)
    }

    override fun initViewObservable() {
        viewModel.uc.flashLightEvent.observe(this, {
            if (viewModel.isOpenFlag.get()) {
                binding.scanView.openFlashlight()
            } else {
                binding.scanView.closeFlashlight()
            }
        })
        viewModel.uc.openAlbumEvent.observe(this, {
            PermissionUtil.reqStorage(fragment = this, callback = { allGranted, _, _ ->
                if (allGranted) {
                    Matisse.from(this)
                        .choose(MimeType.ofImage(), false)
                        .theme(if (DayModeUtil.isNightMode(requireContext())) R.style.Matisse_Dracula else R.style.Matisse_Zhihu)
                        .maxSelectable(1)
                        .countable(false)
                        .imageEngine(MyGlideEngine())
                        .thumbnailScale(0.8f)
                        .capture(false)
                        .originalEnable(false)
                        .forResult(ALBUM_REQUEST_CODE)
                }
            })
        })
    }

    override fun onScanQRCodeSuccess(result: String?) {
        if (result.isNullOrBlank()) {
            return
        }
        if (RegexUtils.getMatches(AppConstants.Constants.REGEX_URL, result).isEmpty()) {
            SnackbarUtils.with(binding.ivFlashlight)
                .setMessage("非二维码链接，请重试")
                .setAction("确定") { SnackbarUtils.dismiss() }
                .show()
            ThreadUtils.runOnUiThreadDelayed({
                binding.scanView.startSpot()
            }, 1500)
            return
        }
        viewModel.startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
            putString(AppConstants.BundleKey.WEB_URL, result)
        })
        viewModel.finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 环境变暗了
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ALBUM_REQUEST_CODE && data != null) {
            val list = Matisse.obtainPathResult(data)
            if (list.isNotEmpty()) binding.scanView.decodeQRCode(list[0])
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        showErrorToast("打开相机失败")
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        binding.scanView.startSpot()
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        binding.scanView.stopSpot()
    }

    override fun onStop() {
        super.onStop()
        binding.scanView.stopSpot()
    }

    override fun onStart() {
        super.onStart()
        binding.scanView.startSpot()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.scanView.onDestroy()
    }
}