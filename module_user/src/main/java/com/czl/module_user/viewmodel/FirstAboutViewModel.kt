package com.czl.module_user.viewmodel

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class FirstAboutViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val onGithubClick: View.OnClickListener = View.OnClickListener {
        startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
            putString(AppConstants.BundleKey.WEB_URL,(it as TextView).text.toString())
        })
    }

    val onBlogClick:View.OnClickListener = View.OnClickListener {
        startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
            putString(AppConstants.BundleKey.WEB_URL,(it as TextView).text.toString())
        })
    }

}