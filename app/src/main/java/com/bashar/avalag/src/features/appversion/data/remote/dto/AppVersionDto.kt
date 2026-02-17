package com.bashar.avalag.src.features.appversion.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * data: {
 *   "update_status": "mandatory",
 *   "link": "https://www.apple.com/"
 * }
 */
data class AppVersionDto(
    @SerializedName("update_status") val updateStatus: String? = null,
    @SerializedName("link") val link: String? = null,
)
