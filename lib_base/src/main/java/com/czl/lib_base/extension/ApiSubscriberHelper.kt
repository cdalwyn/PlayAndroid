package com.czl.lib_base.extension

import android.net.ParseException
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.util.ToastHelper.showErrorToast
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.core.BasePopupView
import io.reactivex.observers.DisposableObserver
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Alwyn
 * @Date 2020/10/10
 * @Description RxJava 处理Api异常
 * 不自动处理状态页的不传构造即可
 */
abstract class ApiSubscriberHelper<T>(private val loadService: LoadService<BaseBean<*>?>? = null) :
    DisposableObserver<T>(), KoinComponent {

    private val loginPopMap: ConcurrentHashMap<Int,BasePopupView> by inject(named("login_map"))
    private val loginPopView:BasePopupView by inject(named("token_login"))

    override fun onNext(t: T) {
        if (t is BaseBean<*>) {
            loadService?.showWithConvertor(t)
        }
        if (t is BaseBean<*> && t.errorCode != 0) {
            showErrorToast(t.errorMsg)
            if (t.errorCode == -1001) {
                LogUtils.e("当前用户未登录或者登录已失效")
                if (loginPopMap.isEmpty()){
                    // 利用ConcurrentHashMap线程安全特性避免并发请求下弹出多次
                    loginPopMap[0] = loginPopView
                    loginPopMap[0]?.show()
                }else{
                    val basePopupView = loginPopMap[0]
                    basePopupView?.show()
                }
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