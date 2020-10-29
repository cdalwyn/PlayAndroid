package com.czl.lib_base.base

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.czl.lib_base.event.callback.UIChangeLiveData
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.goldze.mvvmhabit.base.BaseModel
import me.goldze.mvvmhabit.base.IBaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.yokeyword.fragmentation.SupportFragment
import org.koin.core.KoinComponent
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Alwyn on 2020/10/10.
 * 由于ViewModel工厂通过反射动态实例化，无需再手动构造实例
 * 所以子类继承构造指定为 application: MyApplication, model: DataRepository 泛型为<DataRepository>。
 */
open class BaseViewModel<M : BaseModel?>(application: MyApplication, protected var model: M? = null) :
    AndroidViewModel(application), IBaseViewModel, Consumer<Disposable?>, KoinComponent {

    val uC: UIChangeLiveData = UIChangeLiveData()

    // 标题栏标题
    val tvTitle = ObservableField("")

    // 标题栏右图标id
    var ivToolbarIconRes = 0

    // 标题栏返回箭头的显示隐藏 1 显示 0 隐藏
    val btnBackVisibility = ObservableField("1")

    /**
     * 标题栏右图标点击事件 可在UI层调用viewModel.ivToolbarIconOnClick重新定义
     * 或者VM层重写setToolbarRightClickListener()
     */
    var ivToolbarIconOnClick = getToolbarIconClickListener()

    //弱引用持有
    private lateinit var lifecycle: WeakReference<LifecycleProvider<*>>

    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    open fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    private fun getToolbarIconClickListener(): BindingCommand<Any> {
        return BindingCommand(BindingAction(setToolbarRightClickListener()))
    }

    // 子类重写
    open fun setToolbarRightClickListener(): () -> Unit = {}

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
        val params: MutableMap<String, Any> = HashMap()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uC.startActivityEvent.postValue(params)
    }

    fun startFragment(fragment: SupportFragment, bundle: Bundle? = null) {
        if (bundle != null) {
            fragment.arguments = bundle
        }
        uC.startFragmentEvent.postValue(fragment)
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
        model?.onCleared()
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