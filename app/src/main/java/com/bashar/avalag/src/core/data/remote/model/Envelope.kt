package com.bashar.avalag.src.core.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Matches backend envelope:
 * {
 *   "status": "success",
 *   "message": "...",
 *   "data": { ... }
 * }
 */
data class ApiEnvelope<T>(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null,
)
