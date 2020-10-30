package com.czl.module_main.viewmodel

import android.content.ClipData.Item
import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.entity.HomeArticleBean
import com.czl.lib_base.data.entity.HomeBannerBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.lib_base.util.ToastHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.binding.command.BindingConsumer
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList


/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class HomeViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val bannerCompleteEvent: SingleLiveEvent<List<HomeBannerBean>?> = SingleLiveEvent()
        val rvLoadCompleteEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    //给RecyclerView添加ItemBinding
    val itemBinding: ItemBinding<HomeItemViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.main_item_home)

    //给RecyclerView添加ObservableList
    val observableList: ObservableList<HomeItemViewModel> = ObservableArrayList()

    var list: DiffObservableList<HomeArticleBean.Data> =
        DiffObservableList(object : DiffUtil.ItemCallback<HomeArticleBean.Data>() {
            override fun areItemsTheSame(
                oldItem: HomeArticleBean.Data,
                newItem: HomeArticleBean.Data
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HomeArticleBean.Data,
                newItem: HomeArticleBean.Data
            ): Boolean {
                return oldItem.title == newItem.title
            }
        })

    val onTabSelectedListener: BindingCommand<Int> = BindingCommand(BindingConsumer{

    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getBanner(model)
        getArticle(model)
    })

    private fun getArticle(model: DataRepository) {
        model.getHomeArticle()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<HomeArticleBean>>() {
                override fun onResult(t: BaseBean<HomeArticleBean>) {
//                    observableList.clear()
                    uc.rvLoadCompleteEvent.call()
                    if (t.errorCode == 0) {
                        if (observableList.isNotEmpty()) {
                            list.update(t.data!!.datas, list.calculateDiff(t.data!!.datas))
                            return
                        }
                        for (data in t.data!!.datas) {
                            observableList.add(HomeItemViewModel(this@HomeViewModel, data))
                        }
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.rvLoadCompleteEvent.call()
                    ToastHelper.showErrorToast(msg)
                }
            })
    }

    private fun getBanner(model: DataRepository) {
        model.getBannerData()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<HomeBannerBean>>>() {
                override fun onResult(t: BaseBean<List<HomeBannerBean>>) {
                    if (t.errorCode == 0) {
                        uc.bannerCompleteEvent.postValue(t.data)
                    } else {
                        uc.bannerCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.bannerCompleteEvent.postValue(null)
                    ToastHelper.showErrorToast(msg)
                }
            })
    }
}