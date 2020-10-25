package com.czl.module_main.viewmodel

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.mvvm.entity.ArticleBean
import com.czl.lib_base.mvvm.entity.CollectArticle
import com.czl.lib_base.mvvm.ui.ContainerFmActivity
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.binding.command.BindingConsumer
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author Alwyn
 * @Date 2020/10/10
 * @Description
 * Android中 inject() get()等方法在Activity和Fragment中已经默认存在；当你在其他类中需要使用时，你需要让其他类实现KoinComponent接口
 */
class MainViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    // by inject() 懒汉式注入
    // val mDataImpl: LocalDataSource = get() 饿汉式注入
    // private val mDataResp by inject<DataRepository>()

    //给RecyclerView添加ObservableList
    val observableList: ObservableList<RvItemViewModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    val itemBinding: ItemBinding<RvItemViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.main_item_rv)

    // 获取本地数据显示的文本
    val text = ObservableField("")

    // 输入框
    val tvPost = ObservableField("0")

    //封装一个界面发生改变的观察者
    val uc = UIChangeObservable()

    class UIChangeObservable {
        // 显示开关观察者
        var pSwitchEvent = SingleLiveEvent<Boolean>()

        // 删除item的观察者
        var deleteItemLiveData: SingleLiveEvent<RvItemViewModel> = SingleLiveEvent()
    }

    // 获取本地数据
    val btnOnClick: BindingCommand<Any?> = BindingCommand(BindingAction {
        text.set(model.getLocalData())
    })

    // 清除本地数据的文本显示内容
    val clearOnClick: BindingCommand<Any?> = BindingCommand(BindingAction {
        text.set("")
    })

    // 显示本地数据的文本显示内容
    val checkOnClick: BindingCommand<Any?> =
        BindingCommand(BindingAction { ToastUtils.showShort(text.get()) })

    // 本地数据显示的开关
    val switchOnClick: BindingCommand<Boolean> = BindingCommand(BindingConsumer {
        uc.pSwitchEvent.setValue(it)
    })

    // 启动Fragment
    val startFmOnClick: View.OnClickListener = View.OnClickListener {
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.MAIN2FIRST,"这是MainModule路由携带过来的数据")
        startContainerActivity(AppConstants.Router.User.F_FIRST,bundle)
    }

    // 获取列表数据
    val btnRvOnClick: BindingCommand<Any?> = BindingCommand(BindingAction {
        model.getMainArticle(tvPost.get()!!)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object :
                ApiSubscriberHelper<BaseBean<ArticleBean>>() {
                override fun onResult(t: BaseBean<ArticleBean>) {
                    observableList.clear()
                    dismissLoading()
                    if (t.errorCode == 0) {
                        t.data?.apply {
                            for (item in this.datas) {
                                val rvItemViewModel = RvItemViewModel(this@MainViewModel, item)
                                observableList.add(rvItemViewModel)
                            }
                        }
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    ToastUtils.showShort(msg)
                }
            })
    })

    val btnCollectionClick: BindingCommand<Any> = BindingCommand(BindingAction {
        model.getCollectArticle()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<CollectArticle>>() {
                override fun onResult(t: BaseBean<CollectArticle>) {
                    dismissLoading()
                    if (t.errorCode == 0) {
                        ToastUtils.showShort("已收藏数量=${t.data?.total}")
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    ToastUtils.showShort(msg)
                }
            })
    })

    /**
     * 根据rvItemViewModel获取下标
     *
     * @return item的下标
     */
    fun getItemPosition(rvItemViewModel: RvItemViewModel?): Int {
        return observableList.indexOf(rvItemViewModel)
    }

    /**
     * 删除列表某个item
     */
    fun deleteItem(rvItemViewModel: RvItemViewModel?) {
        observableList.remove(rvItemViewModel)
    }


}