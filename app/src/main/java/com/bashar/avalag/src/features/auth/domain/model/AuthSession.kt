package com.bashar.avalag.src.features.auth.domain.model

data class AuthSession(
    val token: String,
    val user: AuthUser,
)

data class AuthUser(
    val id: Int,
    val qr: String?,
    val name: String?,
    val key: String?,
    val phone: String?,
    val image: String?,
    val email: String?,
    val accountStatus: AccountStatus?,
)

data class AccountStatus(
    val value: String?,
    val desc: String?,
)