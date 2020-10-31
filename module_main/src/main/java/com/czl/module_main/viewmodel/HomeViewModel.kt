package com.czl.module_main.viewmodel

import android.util.SparseArray
import android.view.View
import androidx.core.util.set
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DiffUtil
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.entity.HomeArticleBean
import com.czl.lib_base.data.entity.HomeBannerBean
import com.czl.lib_base.data.entity.HomeProjectBean
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
    var tabSelectedPosition = ObservableField(0)

    inner class UiChangeEvent {
        val bannerCompleteEvent: SingleLiveEvent<List<HomeBannerBean>?> = SingleLiveEvent()
        val rvLoadCompleteEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val picShowEvent:SingleLiveEvent<SparseArray<Any>> = SingleLiveEvent()
        val settingShowEvent:SingleLiveEvent<SparseArray<Any?>> = SingleLiveEvent()
    }

    // 添加首页热门博文ItemBinding
    val articleItemBinding: ItemBinding<HomeArticleItemVm> =
        ItemBinding.of(BR.viewModel, R.layout.main_item_home)

    // 添加首页热门项目ItemBinding
    val projectItemBinding: ItemBinding<HomeProjectItemVm> =
        ItemBinding.of(BR.viewModel, R.layout.main_item_project)

    // 首页文章列表
    val observableArticles: ObservableList<HomeArticleItemVm> = ObservableArrayList()

    // 首页项目列表
    val observableProjects: ObservableList<HomeProjectItemVm> = ObservableArrayList()

    /**
     * 获取首页热门博文下标
     *
     * @return item的下标
     */
    fun getArticleItemPosition(itemViewModel: HomeArticleItemVm?): Int {
        return observableArticles.indexOf(itemViewModel)
    }

    /**
     * 获取首页热门博文下标
     *
     * @return item的下标
     */
    fun getProjectItemPosition(itemViewModel: HomeProjectItemVm?): Int {
        return observableProjects.indexOf(itemViewModel)
    }

    var articleList: DiffObservableList<HomeArticleBean.Data> =
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

    var projectList: DiffObservableList<HomeProjectBean.Data> =
        DiffObservableList(object : DiffUtil.ItemCallback<HomeProjectBean.Data>() {
            override fun areItemsTheSame(
                oldItem: HomeProjectBean.Data,
                newItem: HomeProjectBean.Data
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HomeProjectBean.Data,
                newItem: HomeProjectBean.Data
            ): Boolean {
                return oldItem.title == newItem.title
            }
        })

    val onTabSelectedListener: BindingCommand<Int> = BindingCommand(BindingConsumer { position ->
        tabSelectedPosition.set(position)
        when (position) {
            0 -> getArticle(model)
            1 -> getProject(model)
        }
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getBanner(model)
        when (tabSelectedPosition.get()) {
            0 -> getArticle(model)
            1 -> getProject(model)
        }
    })

    private fun getProject(model: DataRepository) {
        model.getHomeProject()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<HomeProjectBean>>() {
                override fun onResult(t: BaseBean<HomeProjectBean>) {
                    uc.rvLoadCompleteEvent.call()
                    if (t.errorCode == 0) {
                        t.data?.let {
                            if (observableProjects.isNotEmpty()) {
                                projectList.update(it.datas, projectList.calculateDiff(it.datas))
                                return
                            }
                            for (data in it.datas) {
                                observableProjects.add(HomeProjectItemVm(this@HomeViewModel, data))
                            }
                        }
                    }
                }

                override fun onFailed(msg: String?) {

                }

            })
    }

    private fun getArticle(model: DataRepository) {
        model.getHomeArticle()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<HomeArticleBean>>() {
                override fun onResult(t: BaseBean<HomeArticleBean>) {
                    uc.rvLoadCompleteEvent.call()
                    if (t.errorCode == 0) {
                        t.data?.let {
                            if (observableArticles.isNotEmpty()) {
                                articleList.update(it.datas, articleList.calculateDiff(it.datas))
                                return
                            }
                            for (data in it.datas) {
                                observableArticles.add(HomeArticleItemVm(this@HomeViewModel, data))
                            }
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

    fun showSettingAttachPop(id: Int?, sameId: Int?, view: View){
        val sparseArray = SparseArray<Any?>(3)
        sparseArray[0] = id
        sparseArray[1] = sameId
        sparseArray[2] = view
        uc.settingShowEvent.postValue(sparseArray)
    }

    fun showPicView(imageView: View, url: String){
        val sparseArray = SparseArray<Any>(2)
        sparseArray[0] = url
        sparseArray[1] = imageView
        uc.picShowEvent.postValue(sparseArray)
    }
}