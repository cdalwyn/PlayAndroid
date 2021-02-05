package com.czl.lib_base.util

import android.view.View
import com.czl.lib_base.base.BaseViewModel
import com.trello.rxlifecycle3.android.RxLifecycleAndroid
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxThreadHelper {
    fun <T> rxSchedulerHelper(viewModel: BaseViewModel<*>?): ObservableTransformer<T, T> {    //compose简化线程
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) //                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider()))
                .doOnSubscribe(viewModel)
        } //请求与ViewModel周期同步;
    }

    fun <T> rxSchedulerHelper(view: View?): ObservableTransformer<T, T> {    //compose简化线程
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .compose(RxLifecycleAndroid.bindView(view!!))
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> rxFlowSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer { observable: Flowable<T> ->
            observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> rxSchedulerHelper(): ObservableTransformer<T, T> {    //compose简化线程
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}