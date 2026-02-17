package com.bashar.avalag.src.core.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class NetworkErrorLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Timber.tag("API").d("--> ${request.method} ${request.url}")

        val response = chain.proceed(request)
        val peek = response.peekBody(Long.MAX_VALUE).string() // safe, does not consume original
        Timber.tag("API_RESPONSE").d("<-- ${response.code} ${request.url}\n$peek")
        return try {
            chain.proceed(request)
        } catch (t: Throwable) {
            Timber.tag("NetworkError").e(
                t,
                "HTTP failed: ${request.method} ${request.url} (${t::class.java.simpleName}: ${t.message})"
            )
            throw t
        }
    }
}
