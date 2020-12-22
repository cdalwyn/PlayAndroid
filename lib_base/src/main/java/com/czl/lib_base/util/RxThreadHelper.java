package com.czl.lib_base.util;

import android.view.View;

import com.czl.lib_base.base.BaseViewModel;
import com.trello.rxlifecycle3.android.RxLifecycleAndroid;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxThreadHelper {
    private RxThreadHelper() {
    }

    public static <T> ObservableTransformer<T, T> rxSchedulerHelper(BaseViewModel viewModel) {    //compose简化线程
        return observable -> observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider()))
                .doOnSubscribe(viewModel)//请求与ViewModel周期同步;
                ;
    }

    public static <T> FlowableTransformer<T, T> rxFlowHelper(BaseViewModel viewModel) {    //compose简化线程
        return observable -> observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider()))
                .doOnSubscribe(viewModel)//请求与ViewModel周期同步;
                ;
    }

    public static <T> ObservableTransformer<T, T> rxSchedulerHelper(View view) {    //compose简化线程
        return observable -> observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .compose(RxLifecycleAndroid.bindView(view))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
