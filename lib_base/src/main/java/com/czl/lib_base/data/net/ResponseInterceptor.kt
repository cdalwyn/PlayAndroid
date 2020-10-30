package com.czl.lib_base.data.net

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.event.LiveBusCenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.blankj.utilcode.util.LogUtils
import me.goldze.mvvmhabit.utils.ToastUtils
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http.HttpHeaders
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author Alwyn
 * @Date 2020/8/4
 * @Description 对服务器返回结果处理解密 token失效等
 */
class ResponseInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        val responseBody = response.body()
        val url = request.url().toString()
        if (HttpHeaders.hasBody(response)) {
            if (responseBody == null) return response
            //            String url = request.url().toString();
            val mediaType = responseBody.contentType()
            when (response.code()) {
                503, 504, 500, 404, 403 -> {
                    ToastUtils.showShort("服务器出错")
                }
                200 -> {
                    val source = responseBody.source()
                    source.request(Long.MAX_VALUE)
                    val buffer = source.buffer()
                    val charset = StandardCharsets.UTF_8
                    //                    charset = mediaType.charset(charset);
                    if (charset != null) {
                        // 服务器返回结果
                        val bodyString = buffer.clone().readString(charset)
                        val baseBean = Gson().fromJson<BaseBean<*>>(
                            bodyString,
                            object : TypeToken<BaseBean<*>?>() {}.type
                        )
//                        LogUtils.i("Interceptor Response=" + baseBean.toString() + ",code=" + baseBean.errorCode)
                        when (baseBean.errorCode) {
                            -1001 -> {
                                // 请先登录
                                LiveBusCenter.postTokenExpiredEvent(baseBean.errorMsg)
                            }
                        }
                        val newRespBody = ResponseBody.create(mediaType, bodyString)
                        response = response.newBuilder().body(newRespBody).build()
                    }
                }
            }
        }
        return response
    }

    private fun isPlaintext(mediaType: MediaType?): Boolean {
        if (mediaType == null) return false
        if (mediaType.type() != null && mediaType.type() == "text") {
            return true
        }
        var subtype = mediaType.subtype()
        if (subtype != null) {
            subtype = subtype.toLowerCase(Locale.getDefault())
            return subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains(
                "xml"
            ) || subtype.contains("html")
        }
        return false
    }
}