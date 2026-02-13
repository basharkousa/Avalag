package com.bashar.avalag.src.core.utils

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

object ApiErrorParser {

    fun getMessage(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> parseHttpException(throwable)
            is IOException -> "Network connection error"
            else -> throwable.localizedMessage ?: "Unexpected error occurred"
        }
    }

    private fun parseHttpException(e: HttpException): String {
        return try {
            val errorJson = e.response()?.errorBody()?.string()
            if (!errorJson.isNullOrEmpty()) {
                val json = JSONObject(errorJson)
                // Most common patterns:
                // { "message": "..." }
                // { "error": "..." }
                json.optString("message")
                    .ifBlank { json.optString("error") }
                    .ifBlank { "Unknown server error" }
            } else {
                "Unknown server error"
            }
        } catch (ex: Exception) {
            Timber.tag("ApiErrorParser").e(ex, "Error parsing HTTP error")
            "Unknown error"
        }
    }
}
