package com.czl.module_project.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.ProjectSortBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper

/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class ProjectViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val loadCompleteEvent: SingleLiveEvent<List<ProjectSortBean>> = SingleLiveEvent()
    fun getProjectSort(){
        model.getProjectSort()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<ProjectSortBean>>>(loadService) {
                override fun onResult(t: BaseBean<List<ProjectSortBean>>) {
                    if (t.errorCode == 0) {
                        loadCompleteEvent.postValue(t.data)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }
    fun getCacheSort():List<ProjectSortBean>{
        return model.getCacheListData(AppConstants.CacheKey.CACHE_PROJECT_SORT)?: emptyList()
    }
}