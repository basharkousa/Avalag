package com.bashar.avalag.src.features.auth.domain.repositories

interface IAuthLocalDataSource {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}