package com.czl.module_project.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_project.BR
import com.czl.module_project.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description
 */
class ContentViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    var currentPage = 0
    var cid = 0

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val refreshCompleteEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    // 添加首页热门博文ItemBinding
    val projectItemBinding: ItemBinding<ProjectGridItemVm> =
        ItemBinding.of(BR.viewModel, R.layout.project_item_grid)

    val itemsProject: ObservableList<ProjectGridItemVm> = ObservableArrayList()

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 0
        getProjectDataByCid(cid)
    })

    fun getProjectDataByCid(cid: Int) {
        this.cid = cid
        model.getProjectByCid(currentPage.toString(), cid.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<ProjectBean>>() {
                override fun onResult(t: BaseBean<ProjectBean>) {
                    uc.refreshCompleteEvent.call()
                    if (t.errorCode == 0) {
                        if (currentPage == 0) {
                            itemsProject.clear()
                        }
                        for (data in t.data!!.datas) {
                            itemsProject.add(ProjectGridItemVm(this@ContentViewModel, data))
                        }
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.refreshCompleteEvent.call()
                }

            })
    }
}