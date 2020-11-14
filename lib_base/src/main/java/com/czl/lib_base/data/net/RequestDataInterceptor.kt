package com.czl.lib_base.data.net

import com.czl.lib_base.data.DataRepository
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * @author Alwyn
 * @Date 2020/8/4
 * @Description 请求参数的拦截器
 */
@KoinApiExtension
class RequestDataInterceptor : Interceptor, KoinComponent {
    private val dataRepository by inject<DataRepository>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val method = request.method()
        if ("POST" == method) {
            val body = request.body()
            val builder = request.url().newBuilder()
            // 将原body写入缓存
            val buffer = Buffer()
            body?.writeTo(buffer)
            val reqBuilder = request.newBuilder()
            // 未加密的请求参数
            val requestData = URLDecoder.decode(buffer.readString(StandardCharsets.UTF_8),"utf-8")
            // 新的请求体
            val newRequestBody: RequestBody
            try {
                // 可在这对请求参数加密后重新发起请求
                newRequestBody = RequestBody.create(body?.contentType(), requestData)
                val newRequest = reqBuilder
                    .post(newRequestBody)
                    .url(builder.build())
                    .build()
                return chain.proceed(newRequest)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return chain.proceed(request)
    }

    private fun bodyToString(request: RequestBody?): String {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}