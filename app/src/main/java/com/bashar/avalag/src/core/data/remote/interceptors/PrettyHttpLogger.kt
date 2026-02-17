package com.bashar.avalag.src.core.data.remote.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class PrettyHttpLogger(
    private val tag: String = "NET",
    private val maxBodyChars: Int = 200_000, // avoid huge logs
    private val redactHeaders: Set<String> = setOf("Authorization", "Cookie", "Set-Cookie")
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()

        logRequest(request)

        val response = try {
            chain.proceed(request)
        } catch (t: Throwable) {
            Log.e(tag, "❌ HTTP FAILED: ${t.message}", t)
            throw t
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        logResponse(response, request, tookMs)

        return response
    }

    private fun logRequest(request: Request) {
        val url = request.url.toString()
        val method = request.method

        Log.d(tag, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d(tag, "➡️  REQUEST  $method $url")

        // Headers
        if (request.headers.size > 0) {
            Log.d(tag, "Headers:")
            for (name in request.headers.names()) {
                val value = if (redactHeaders.contains(name)) "██REDACTED██" else request.header(name).orEmpty()
                Log.d(tag, "  $name: $value")
            }
        }

        // Body
        val body = request.body
        if (body == null) {
            Log.d(tag, "Body: <empty>")
            return
        }

        val contentType = body.contentType()?.toString().orEmpty()
        Log.d(tag, "Content-Type: $contentType")

        when {
            contentType.contains("application/json", ignoreCase = true) -> {
                val raw = bodyToString(body)
                Log.d(tag, "Body(JSON):\n${prettyJson(raw)}")
            }

            contentType.contains("application/x-www-form-urlencoded", ignoreCase = true) -> {
                val raw = bodyToString(body)
                Log.d(tag, "Body(Form): $raw")
            }

            contentType.contains("multipart/form-data", ignoreCase = true) -> {
                // Multipart bodies can be huge/binary; avoid dumping bytes
                Log.d(tag, "Body(Multipart): <multipart form-data (not dumped)>")
            }

            else -> {
                val raw = bodyToString(body)
                Log.d(tag, "Body:\n${trimIfNeeded(raw)}")
            }
        }
    }

    private fun logResponse(response: Response, request: Request, tookMs: Long) {
        val code = response.code
        val msg = response.message
        val url = request.url.toString()

        Log.d(tag, "⬅️  RESPONSE $code ($msg)  (${tookMs}ms)  $url")

        // Headers
        if (response.headers.size > 0) {
            Log.d(tag, "Headers:")
            for (name in response.headers.names()) {
                val value = if (redactHeaders.contains(name)) "██REDACTED██" else response.header(name).orEmpty()
                Log.d(tag, "  $name: $value")
            }
        }

        // Body (safe, does not consume original)
        val contentType: MediaType? = response.body?.contentType()
        val isJson = contentType?.subtype?.contains("json", ignoreCase = true) == true

        val peek = response.peekBody(maxBodyChars.toLong()).string()
        if (peek.isBlank()) {
            Log.d(tag, "Body: <empty>")
        } else {
            Log.d(tag, if (isJson) "Body(JSON):\n${prettyJson(peek)}" else "Body:\n${trimIfNeeded(peek)}")
        }

        Log.d(tag, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    }

    private fun bodyToString(body: RequestBody): String {
        return try {
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = body.contentType()?.charset(Charset.forName("UTF-8")) ?: Charsets.UTF_8
            buffer.readString(charset)
        } catch (e: Exception) {
            "<could not read request body: ${e.message}>"
        }
    }

    private fun prettyJson(raw: String): String {
        return try {
            val trimmed = raw.trim()
            when {
                trimmed.startsWith("{") -> JSONObject(trimmed).toString(2)
                trimmed.startsWith("[") -> JSONArray(trimmed).toString(2)
                else -> trimIfNeeded(raw)
            }
        } catch (_: Exception) {
            trimIfNeeded(raw)
        }
    }

    private fun trimIfNeeded(text: String): String {
        val t = text.trim()
        return if (t.length > maxBodyChars) t.take(maxBodyChars) + "\n…(trimmed)" else t
    }
}
