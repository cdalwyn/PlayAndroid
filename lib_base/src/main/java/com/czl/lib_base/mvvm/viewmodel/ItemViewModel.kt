package com.czl.lib_base.mvvm.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository

open class ItemViewModel<VM : BaseViewModel<*>?>(protected var viewModel: VM)