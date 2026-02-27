package com.bashar.avalag.src.features.basics.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EnumsEnvelopeDto(
    @SerializedName("enums")
    val enums: Map<String, Map<String, String>>? = null
)