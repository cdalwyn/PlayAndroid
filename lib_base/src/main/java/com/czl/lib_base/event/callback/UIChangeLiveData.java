package com.czl.lib_base.event.callback;

import android.util.SparseArray;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.czl.lib_base.bus.event.SingleLiveEvent;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UIChangeLiveData extends SingleLiveEvent {
    public SingleLiveEvent<String> showLoadingEvent;
    public SingleLiveEvent<Void> dismissDialogEvent;
    public SingleLiveEvent<Map<String, Object>> startActivityEvent;
    public SingleLiveEvent<HashMap<String,Object>> startFragmentEvent;
    public SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;
    public SingleLiveEvent<Void> finishEvent;
    public SingleLiveEvent<Void> onBackPressedEvent;
    public SingleLiveEvent<Void> scrollTopEvent;
    public SingleLiveEvent<SparseArray<String>> showSharePopEvent;
    public SingleLiveEvent<SparseArray<String>> getShowSharePopEvent() {
        return showSharePopEvent = createLiveData(showSharePopEvent);
    }
    public SingleLiveEvent<Void> getScrollTopEvent() {
        return scrollTopEvent = createLiveData(scrollTopEvent);
    }
    public SingleLiveEvent<String> getShowLoadingEvent() {
        return showLoadingEvent = createLiveData(showLoadingEvent);
    }

    public SingleLiveEvent<Void> getDismissDialogEvent() {
        return dismissDialogEvent = createLiveData(dismissDialogEvent);
    }

    public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
        return startActivityEvent = createLiveData(startActivityEvent);
    }

    public SingleLiveEvent<HashMap<String,Object>> getStartFragmentEvent() {
        return startFragmentEvent = createLiveData(startFragmentEvent);
    }

    public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
        return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
    }

    public SingleLiveEvent<Void> getFinishEvent() {
        return finishEvent = createLiveData(finishEvent);
    }

    public SingleLiveEvent<Void> getOnBackPressedEvent() {
        return onBackPressedEvent = createLiveData(onBackPressedEvent);
    }

    private <T> SingleLiveEvent<T> createLiveData(SingleLiveEvent<T> liveData) {
        if (liveData == null) {
            liveData = new SingleLiveEvent<>();
        }
        return liveData;
    }

    @Override
    public void observe(@NotNull LifecycleOwner owner, @NotNull Observer observer) {
        super.observe(owner, observer);
    }
}