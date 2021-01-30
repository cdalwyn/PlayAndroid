package com.czl.module_main.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.CacheDiskUtils
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
import com.czl.lib_base.data.bean.SearchHotKeyBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import io.reactivex.Observable
import java.io.Serializable


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
        val bannerCompleteEvent: SingleLiveEvent<List<HomeBannerBean>> = SingleLiveEvent()
        val drawerOpenEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val searchConfirmEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val searchItemClickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val searchItemDeleteEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val moveTopEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val loadArticleCompleteEvent: SingleLiveEvent<HomeArticleBean?> = SingleLiveEvent()
        val loadProjectCompleteEvent: SingleLiveEvent<ProjectBean> = SingleLiveEvent()
        val tabSelectedEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val loadSearchHotKeyEvent: SingleLiveEvent<List<SearchHotKeyBean>> = SingleLiveEvent()
        val searchIconClickEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        currentArticlePage = -1
        currentProjectPage = -1
        getBanner()
        getSearchHotKeyword()
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
        uc.searchConfirmEvent.postValue(keyword)
    })

    val onSearchIconCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.searchIconClickEvent.call()
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

    fun getSearchHotKeyword() {
        model.getSearchHotKey()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<SearchHotKeyBean>>>() {
                override fun onResult(t: BaseBean<List<SearchHotKeyBean>>) {
                    if (t.errorCode == 0) {
                        uc.loadSearchHotKeyEvent.postValue(t.data)
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
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
     fun getArticle() {
        if (currentArticlePage == -1) {
            // 当前是第一页的时候合并首页置顶文章
            val mainObservable = model.getHomeArticle((currentArticlePage + 1).toString())
            val topObservable = model.getHomeTopArticle()
            Observable.zip(mainObservable, topObservable,
                { t1, t2 ->
                    t2.data?.let {
                        it.forEach { data->data.topFlag=true }
                        t1.data?.datas?.addAll(0,it)
                    }
                    t1
                }).compose(RxThreadHelper.rxSchedulerHelper(this))
                .subscribe(object :ApiSubscriberHelper<BaseBean<HomeArticleBean>>(){
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
            return
        }
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
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<HomeBannerBean>>>(loadService) {
                override fun onResult(t: BaseBean<List<HomeBannerBean>>) {
                    if (t.errorCode == 0) {
                        uc.bannerCompleteEvent.postValue(t.data)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }

    fun <T:Serializable>getCacheData(key:String):List<T>{
        return model.getCacheListData(key)?: emptyList()
    }


}