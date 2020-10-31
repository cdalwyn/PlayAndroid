package com.czl.lib_base.extension;

import android.annotation.SuppressLint;
import android.net.ParseException;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.NetworkUtils;
import com.czl.lib_base.base.BaseBean;
import com.czl.lib_base.util.ToastHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * @author Alwyn
 * @Date 2020/10/10
 * @Description RxJava 处理Api异常
 */
public abstract class ApiSubscriberHelper<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        if (t instanceof BaseBean && ((BaseBean) t).getErrorCode() != 0) {
            ToastHelper.INSTANCE.showErrorToast(((BaseBean) t).getErrorMsg());
        }
        onResult(t);
    }


    @Override
    public void onComplete() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onError(Throwable throwable) {
        if (!NetworkUtils.isConnected() || throwable instanceof ConnectTimeoutException) {
            onFailed("连接失败，请检查网络后再试");
        } else if (throwable instanceof RuntimeException) {
            onFailed(throwable.getMessage());
        } else if (throwable instanceof SocketTimeoutException) {
            onFailed("连接超时，请重试");
        } else if (throwable instanceof IllegalStateException) {
            onFailed(throwable.getMessage());
        } else if (throwable instanceof HttpException) {
            onFailed("网络异常，请重试");
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof JsonSyntaxException
                || throwable instanceof ParseException
        ) {
            onFailed("数据解析异常，请稍候再试");
        } else if (throwable instanceof UnknownHostException) {
            onFailed("连接失败，请检查网络后再试");
        } else {
            onFailed(throwable.getMessage());
        }
    }

    protected abstract void onResult(T t);

    protected abstract void onFailed(@Nullable String msg);
}