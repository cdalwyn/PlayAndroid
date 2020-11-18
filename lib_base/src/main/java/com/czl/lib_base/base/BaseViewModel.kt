package com.czl.lib_base.base

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.event.callback.UIChangeLiveData
import com.czl.lib_base.util.ToastHelper
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.koin.core.component.KoinComponent
import java.lang.ref.WeakReference
import kotlin.collections.HashMap

/**
 * Created by Alwyn on 2020/10/10.
 * 由于ViewModel工厂通过反射动态实例化，无需再手动构造实例
 * 所以子类继承构造指定为 application: MyApplication, model: DataRepository 泛型为<DataRepository>。
 */
open class BaseViewModel<M : BaseModel>(application: MyApplication, val model: M) :
    AndroidViewModel(application), IBaseViewModel, Consumer<Disposable?> {

    val uC: UIChangeLiveData = UIChangeLiveData()

    // 标题栏标题
    val tvTitle = ObservableField("")

    // 标题栏右图标id
    val ivToolbarIconRes = ObservableInt(0)

    // 标题栏返回箭头的显示隐藏 1 显示 0 隐藏
    val btnBackVisibility = ObservableField("1")

    /**
     * 标题栏右图标点击事件 VM层重写setToolbarRightClick()
     */
    var ivToolbarIconOnClick = BindingCommand<Void>(BindingAction { setToolbarRightClick() })

    //弱引用持有
    private lateinit var lifecycle: WeakReference<LifecycleProvider<*>>

    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    open fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    // 子类重写
    open fun setToolbarRightClick() {

    }

    val refreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        refreshCommand()
    })
    val loadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        loadMoreCommand()
    })
    val scrollToTopCommand: BindingCommand<Void> = BindingCommand(BindingAction { uC.scrollTopEvent.call() })

    /**
     * 通用recyclerview刷新
     */
    open fun refreshCommand() {}

    /**
     * 通用recyclerview加载更多
     */
    open fun loadMoreCommand() {}

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    val lifecycleProvider: LifecycleProvider<*>?
        get() = lifecycle.get()


    fun showLoading(title: String? = "请稍后...") {
        uC.showLoadingEvent.postValue(title)
    }

    fun dismissLoading() {
        uC.dismissDialogEvent.call()
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
        val params: HashMap<String, Any> = HashMap()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uC.startActivityEvent.postValue(params)
    }

    fun startFragment(routh: String, bundle: Bundle? = null) {
        val params: HashMap<String, Any> = HashMap()
        params[ParameterField.ROUTE_PATH] = routh
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uC.startFragmentEvent.postValue(params)
    }

    /**
     * 跳转容器页面
     * @param routePath Fragment路由地址
     * @param bundle    跳转所携带的信息
     */
    fun startContainerActivity(
        routePath: String,
        bundle: Bundle? = null
    ) {
        val params: MutableMap<String, Any> = HashMap()
        params[ParameterField.ROUTE_PATH] = routePath
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uC.startContainerActivityEvent.postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        uC.finishEvent.call()
    }

    /**
     * 返回上一层
     */
    fun onBackPressed() {
        uC.onBackPressedEvent.call()
    }

    fun showErrorToast(msg: String?) {
        ToastHelper.showErrorToast(msg)
    }

    fun showNormalToast(msg: String?) {
        ToastHelper.showNormalToast(msg)
    }

    fun showSuccessToast(msg: String?) {
        ToastHelper.showSuccessToast(msg)
    }


    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {}
    override fun onCreate() {}
    override fun onDestroy() {}
    override fun onStart() {}
    override fun onStop() {}
    override fun onResume() {}
    override fun onPause() {}
//    override fun registerRxBus() {
//        addSubscribe(
//            RxBus.getDefault()
//                .toObservable(TokenExpiredEvent::class.java)
//                .subscribe {
//                    val dataRepository: DataRepository = get()
//                    dataRepository.saveLocalToken("")
//                    startActivity(MainActivity::class.java)
//                    AppManager.getAppManager().finishAllActivity()
//                }
//        )
//    }

    override fun onCleared() {
        super.onCleared()
        model.onCleared()
        //ViewModel销毁时会执行，同时取消所有异步任务
        mCompositeDisposable.clear()

    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        disposable?.let { addSubscribe(it) }
    }

    object ParameterField {
        @JvmField
        var CLASS = "CLASS"

        @JvmField
        var ROUTE_PATH = "ROUTE_PATH"

        @JvmField
        var BUNDLE = "BUNDLE"

    }
}