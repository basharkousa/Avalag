package com.bashar.avalag.src.features.basics.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BasicsInfoDto(
    @SerializedName("promotions")
    val promotions: List<PromotionDto>? = null,
    @SerializedName("countries")
    val countries: List<CountryDto>? = null
)

data class PromotionDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("image") val image: String? = null
)

data class CountryDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("currency") val currency: String? = null
)