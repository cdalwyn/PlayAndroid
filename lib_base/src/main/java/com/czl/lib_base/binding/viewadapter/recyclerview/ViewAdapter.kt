package com.czl.lib_base.binding.viewadapter.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.viewadapter.recyclerview.LayoutManagers.LayoutManagerFactory
import com.czl.lib_base.binding.viewadapter.recyclerview.LineManagers.LineManagerFactory
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

object ViewAdapter {
    @JvmStatic
    @BindingAdapter("lineManager")
    fun setLineManager(recyclerView: RecyclerView, lineManagerFactory: LineManagerFactory) {
        recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView))
    }

    @JvmStatic
    @BindingAdapter("layoutManager")
    fun setLayoutManager(recyclerView: RecyclerView, layoutManagerFactory: LayoutManagerFactory) {
        recyclerView.layoutManager = layoutManagerFactory.create(recyclerView)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["onScrollChangeCommand", "onScrollStateChangedCommand"],
        requireAll = false
    )
    fun onScrollChangeCommand(
        recyclerView: RecyclerView,
        onScrollChangeCommand: BindingCommand<ScrollDataWrapper?>?,
        onScrollStateChangedCommand: BindingCommand<Int?>?
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var state = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollChangeCommand?.execute(
                    ScrollDataWrapper(
                        dx.toFloat(), dy.toFloat(), state
                    )
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state = newState
                onScrollStateChangedCommand?.execute(newState)
            }
        })
    }

    @JvmStatic
    @BindingAdapter("onLoadMoreCommand")
    fun onLoadMoreCommand(recyclerView: RecyclerView, onLoadMoreCommand: BindingCommand<Int?>) {
        val listener: RecyclerView.OnScrollListener = OnScrollListener(onLoadMoreCommand)
        recyclerView.addOnScrollListener(listener)
    }

    @JvmStatic
    @BindingAdapter("itemAnimator")
    fun setItemAnimator(recyclerView: RecyclerView, animator: ItemAnimator?) {
        recyclerView.itemAnimator = animator
    }

    class OnScrollListener(onLoadMoreCommand: BindingCommand<Int?>) :
        RecyclerView.OnScrollListener() {
        private val methodInvoke = PublishSubject.create<Int>()
        private val onLoadMoreCommand: BindingCommand<Int?>?
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(recyclerView.adapter!!.itemCount)
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        init {
            this.onLoadMoreCommand = onLoadMoreCommand
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe { integer -> onLoadMoreCommand.execute(integer) }
        }
    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float, var state: Int)
}