package com.czl.lib_base.util;

import com.czl.lib_base.base.BaseViewModel;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.utils.RxUtils;

public class RxThreadHelper {
    private RxThreadHelper() {
    }

    public static <T> ObservableTransformer<T, T> rxSchedulerHelper(BaseViewModel viewModel) {    //compose简化线程
        return observable -> observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider()))
                ;//请求与ViewModel周期同步;
    }


}
