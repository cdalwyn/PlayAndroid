package com.czl.module_user.ui.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.extension.SingleMediaScanner
import com.czl.lib_base.extension.loadBlurImageRes
import com.czl.lib_base.util.PermissionUtil
import com.czl.lib_base.util.saveBitmapToPicturesPath
import com.czl.lib_base.util.savePictureToAlbum
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentSecAboutBinding
import com.czl.module_user.viewmodel.SecAboutViewModel

/**
 * @author Alwyn
 * @Date 2021/1/21
 * @Description
 */
class SecAboutFragment : BaseFragment<UserFragmentSecAboutBinding, SecAboutViewModel>() {

    companion object {
        fun getInstance(): SecAboutFragment = SecAboutFragment()
    }

    override fun initContentView(): Int {
        return R.layout.user_fragment_sec_about
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        view?.setBackgroundColor(Color.TRANSPARENT)
        val layoutParams = binding.llWx.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            BarUtils.getStatusBarHeight() + SizeUtils.dp2px(60f),
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        binding.llWx.layoutParams = layoutParams
    }

    override fun initViewObservable() {
        viewModel.saveWxImgEvent.observe(this, {
            PermissionUtil.reqStorage(fragment = this, callback = { allGranted, _, _ ->
                if (allGranted) {
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.wx_pay)
                    saveBitmapToPicturesPath(context, bitmap, "alwyn_wechat_pay_code")
                    bitmap.recycle()
                    showSuccessToast("保存成功")
                }
            })
        })
        viewModel.saveAliImgEvent.observe(this, {
            PermissionUtil.reqStorage(fragment = this, callback = { allGranted, _, _ ->
                if (allGranted) {
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ali_pay)
                    saveBitmapToPicturesPath(context, bitmap, "alwyn_Ali_pay_code")
                    bitmap.recycle()
                    showSuccessToast("保存成功")
                }
            })
        })
    }
}