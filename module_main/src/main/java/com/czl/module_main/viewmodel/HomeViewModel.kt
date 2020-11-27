package com.czl.module_main.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.data.bean.HomeBannerBean
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import io.reactivex.Observable


/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class HomeViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentArticlePage: Int = 0
    var currentProjectPage = 0

    val uc = UiChangeEvent()

    val tabSelectedPosition: ObservableInt = ObservableInt(0)

    inner class UiChangeEvent {
        val bannerCompleteEvent: SingleLiveEvent<List<HomeBannerBean>?> = SingleLiveEvent()
        val drawerOpenEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val searchConfirmEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val searchItemClickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val searchItemDeleteEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val moveTopEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val loadArticleCompleteEvent: SingleLiveEvent<HomeArticleBean> = SingleLiveEvent()
        val loadProjectCompleteEvent: SingleLiveEvent<ProjectBean> = SingleLiveEvent()
        val tabSelectedEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    }


    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        currentArticlePage = -1
        currentProjectPage = -1
        getBanner()
        when (tabSelectedPosition.get()) {
            0 -> getArticle()
            1 -> getProject()
        }
    })


    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        when (tabSelectedPosition.get()) {
            0 -> {
                getArticle()
            }
            1 -> {
                getProject()
            }
        }
    })

    val onTabSelectedCommand: BindingCommand<Int> = BindingCommand(BindingConsumer {
        tabSelectedPosition.set(it)
        uc.tabSelectedEvent.postValue(it)
    })

    /*置顶*/
    val fabOnClickListener: View.OnClickListener = View.OnClickListener {
        uc.moveTopEvent.postValue(tabSelectedPosition.get())
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
     * 获取热门项目列表
     */
    fun getProject() {
        model.getHomeProject((currentProjectPage + 1).toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<ProjectBean>>() {
                override fun onResult(t: BaseBean<ProjectBean>) {
                    if (t.errorCode == 0) {
                        currentProjectPage++
                        uc.loadProjectCompleteEvent.postValue(t.data)
                    } else {
                        uc.loadProjectCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadProjectCompleteEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }

    /**
     * 获取热门博文列表
     */
    private fun getArticle() {
        model.getHomeArticle((currentArticlePage + 1).toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<HomeArticleBean>>() {
                override fun onResult(t: BaseBean<HomeArticleBean>) {
                    if (t.errorCode == 0) {
                        currentArticlePage++
                        uc.loadArticleCompleteEvent.postValue(t.data)
                    } else {
                        uc.loadArticleCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadArticleCompleteEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }


    fun getBanner() {
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