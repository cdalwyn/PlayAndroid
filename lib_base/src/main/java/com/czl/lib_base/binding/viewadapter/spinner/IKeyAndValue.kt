package com.czl.lib_base.binding.viewadapter.spinner

/**
 * 下拉Spinner控件的键值对, 实现该接口,返回key,value值, 在xml绑定List<IKeyAndValue>
 */
interface IKeyAndValue {
    val key: String?
    val value: String?
}