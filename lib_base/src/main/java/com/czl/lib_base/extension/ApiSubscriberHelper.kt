package com.czl.lib_base.extension

import android.net.ParseException
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.event.TokenExpiredEvent
import com.czl.lib_base.util.ToastHelper.showErrorToast
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.core.BasePopupView
import io.reactivex.observers.DisposableObserver
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author Alwyn
 * @Date 2020/10/10
 * @Description RxJava 处理Api异常
 * 不自动处理状态页的不传构造即可
 */
abstract class ApiSubscriberHelper<T>(private val loadService: LoadService<BaseBean<*>?>? = null) : DisposableObserver<T>(),KoinComponent {


    override fun onNext(t: T) {
        if (t is BaseBean<*>) {
            loadService?.showWithConvertor(t)
        }
        if (t is BaseBean<*> && t.errorCode != 0) {
            showErrorToast(t.errorMsg)
            if (t.errorCode == -1001) {
                LogUtils.e("当前用户未登录或者登录已失效")
                // Todo Token失效弹窗
            }
        }
        onResult(t)
    }

    override fun onComplete() {}

    override fun onError(throwable: Throwable) {
        loadService?.showWithConvertor(null)
        if (throwable is ConnectException || throwable is ConnectTimeoutException || throwable is UnknownHostException) {
            onFailed("连接失败，请检查网络后再试")
        } else if (throwable is RuntimeException) {
            onFailed(throwable.message)
        } else if (throwable is SocketTimeoutException) {
            onFailed("连接超时，请重试")
        } else if (throwable is IllegalStateException) {
            onFailed(throwable.message)
        } else if (throwable is HttpException) {
            onFailed("网络异常，请重试")
        } else if (throwable is JsonParseException
            || throwable is JSONException
            || throwable is JsonSyntaxException
            || throwable is ParseException
        ) {
            onFailed("数据解析异常，请稍候再试")
        } else {
            onFailed(throwable.message)
        }
    }

    protected abstract fun onResult(t: T)
    protected abstract fun onFailed(msg: String?)
}