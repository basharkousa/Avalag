package com.bashar.avalag.src.features.auth.domain.repositories

import com.bashar.avalag.src.features.auth.domain.model.AuthSession

interface IAuthRepo {
    suspend fun login(username: String, key: String, password: String, fcm: String): AuthSession
}