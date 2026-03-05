package com.bashar.avalag.src.features.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("token") val token: String? = null,
    @SerializedName("user") val user: LoginUserDto? = null,
)

data class LoginUserDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("qr") val qr: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("key") val key: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("account_status") val accountStatus: AccountStatusDto? = null,
)

data class AccountStatusDto(
    @SerializedName("value") val value: String? = null,
    @SerializedName("desc") val desc: String? = null,
)