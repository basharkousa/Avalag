package com.bashar.avalag.src.features.auth.domain.repositories


interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
}