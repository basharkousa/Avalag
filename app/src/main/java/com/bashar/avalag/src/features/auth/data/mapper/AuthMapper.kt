package com.bashar.avalag.src.features.auth.data.remote.mapper

import com.bashar.avalag.src.features.auth.data.remote.dto.AccountStatusDto
import com.bashar.avalag.src.features.auth.data.remote.dto.LoginDto
import com.bashar.avalag.src.features.auth.data.remote.dto.LoginUserDto
import com.bashar.avalag.src.features.auth.domain.model.AccountStatus
import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.model.AuthUser

fun LoginDto.toDomain(): AuthSession {
    val tokenValue = token ?: error("Missing token in login response")
    val userValue = user ?: error("Missing user in login response")

    return AuthSession(
        token = tokenValue,
        user = userValue.toDomain()
    )
}

fun LoginUserDto.toDomain(): AuthUser {
    return AuthUser(
        id = id ?: error("Missing user.id in login response"),
        qr = qr,
        name = name,
        key = key,
        phone = phone,
        image = image,
        email = email,
        accountStatus = accountStatus?.toDomain()
    )
}

fun AccountStatusDto.toDomain(): AccountStatus {
    return AccountStatus(
        value = value,
        desc = desc
    )
}