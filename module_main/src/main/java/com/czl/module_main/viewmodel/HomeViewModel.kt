package com.czl.module_main.viewmodel

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.data.bean.HomeBannerBean
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding


/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class HomeViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    var tabSelectedPosition = ObservableField(0)
    var currentArticlePage = 0
    var currentProjectPage = 0

    inner class UiChangeEvent {
        val bannerCompleteEvent: SingleLiveEvent<List<HomeBannerBean>?> = SingleLiveEvent()
        val loadCompleteEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
        val moveTopEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val drawerOpenEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val searchConfirmEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val searchItemClickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val searchItemDeleteEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val firstLoadProjectEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val tabSelectedEvent: SingleLiveEvent<Int> = SingleLiveEvent()


        // 0 刷新完成（成功获取） 1 正在刷新 2刷新完成（无数据）
        val refreshStateEvent: SingleLiveEvent<Int> = SingleLiveEvent()
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

    /*打开抽屉*/
    val onDrawerClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.drawerOpenEvent.call()
    })

    /*确认搜索*/
    val onSearchConfirmCommand: BindingCommand<String> = BindingCommand(BindingConsumer { keyword ->
        if (keyword.isBlank()) {
            showNormalToast("搜索内容不能为空喔~")
            return@BindingConsumer
        }
        // 保存到数据库
        addSubscribe(model.saveUserSearchHistory(keyword).subscribe())
        uc.searchConfirmEvent.postValue(keyword)
    })

    /*搜索的历史记录Item点击事件*/
    val onSearchItemClick: SuggestionsAdapter.OnItemViewClickListener =
        object : SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemDeleteListener(position: Int, v: View?) {
                uc.searchItemDeleteEvent.postValue(position)
            }

            override fun OnItemClickListener(position: Int, v: View?) {
                uc.searchItemClickEvent.postValue(position)
            }

        }


    /*置顶*/
    val fabOnClickListener: View.OnClickListener = View.OnClickListener {
        uc.moveTopEvent.postValue(tabSelectedPosition.get())
    }

    val onTabSelectedListener: BindingCommand<Int> = BindingCommand(BindingConsumer { position ->
        tabSelectedPosition.set(position)
        uc.tabSelectedEvent.postValue(position)
        when (position) {
            0 -> if (observableArticles.isEmpty()) getArticle(model)
            1 -> if (observableProjects.isEmpty()) {
                uc.firstLoadProjectEvent.call()
                getProject(model)
            }
        }
    })

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.refreshStateEvent.postValue(1)
        currentArticlePage = 0
        currentProjectPage = 0
        getBanner(model)
        when (tabSelectedPosition.get()) {
            0 -> getArticle(model)
            1 -> getProject(model)
        }
    })


    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        when (tabSelectedPosition.get()) {
            0 -> {
                currentArticlePage += 1
                getArticle(model)
            }
            1 -> {
                currentProjectPage += 1
                getProject(model)
            }
        }
    })

    /**
     * 获取热门项目列表
     */
    private fun getProject(model: DataRepository) {
        model.getHomeProject(currentProjectPage.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<ProjectBean>>() {
                override fun onResult(t: BaseBean<ProjectBean>) {
                    if (t.errorCode == 0) {
                        uc.refreshStateEvent.postValue(0)
                        t.data?.let {
                            if (currentProjectPage == 0) {
                                observableProjects.clear()
                                for (data in it.datas) {
                                    observableProjects.add(
                                        HomeProjectItemVm(
                                            this@HomeViewModel,
                                            data
                                        )
                                    )
                                }
                                return
                            }
                            // 加载更多
                            if (currentProjectPage != 0) {
                                uc.loadCompleteEvent.postValue(it.over)
                            }
                            for (data in it.datas) {
                                observableProjects.add(
                                    HomeProjectItemVm(
                                        this@HomeViewModel,
                                        data
                                    )
                                )
                            }

                        }
                    } else {
                        if (currentProjectPage > 0) currentProjectPage -= 1
                        uc.refreshStateEvent.postValue(2)
                        uc.loadCompleteEvent.postValue(false)
                    }
                }

                override fun onFailed(msg: String?) {
                    if (currentProjectPage > 0) currentProjectPage -= 1
                    uc.refreshStateEvent.postValue(2)
                    uc.loadCompleteEvent.postValue(false)
                    showErrorToast(msg)
                }
            })
    }

    /**
     * 退出登录
     */
    fun logout() {
        model.logout()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    if (t.errorCode == 0) {
                        model.clearLoginState()
                        LiveBusCenter.postLogoutEvent()
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }

    /**
     * 收藏
     */
    fun collectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.collectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }

    fun unCollectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.unCollectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }

    /**
     * 获取热门博文列表
     */
    private fun getArticle(model: DataRepository) {
        model.getHomeArticle(currentArticlePage.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<HomeArticleBean>>() {
                override fun onResult(t: BaseBean<HomeArticleBean>) {
                    if (t.errorCode == 0) {
                        uc.refreshStateEvent.postValue(0)
                        t.data?.let {
                            // 刷新
                            if (currentArticlePage == 0) {
                                observableArticles.clear()
                                for (data in it.datas) {
                                    observableArticles.add(
                                        HomeArticleItemVm(
                                            this@HomeViewModel,
                                            data
                                        )
                                    )
                                }
                                return
                            }
                            if (currentArticlePage != 0) {
                                uc.loadCompleteEvent.postValue(it.over)
                            }
                            for (data in it.datas) {
                                observableArticles.add(
                                    HomeArticleItemVm(
                                        this@HomeViewModel,
                                        data
                                    )
                                )
                            }

                        }
                    } else {
                        if (currentArticlePage > 0) currentArticlePage -= 1
                        uc.refreshStateEvent.postValue(2)
                        uc.loadCompleteEvent.postValue(false)
                    }
                }

                override fun onFailed(msg: String?) {
                    if (currentArticlePage > 0) currentArticlePage -= 1
                    uc.refreshStateEvent.postValue(2)
                    uc.loadCompleteEvent.postValue(false)
                    showErrorToast(msg)
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
                    showErrorToast(msg)
                }
            })
    }


}