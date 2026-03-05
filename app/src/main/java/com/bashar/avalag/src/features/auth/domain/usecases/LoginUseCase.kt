package com.bashar.avalag.src.features.auth.domain.usecases

import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo

class LoginUseCase(
    private val repo: IAuthRepo,
) {
    suspend operator fun invoke(username: String, key: String, password: String, fcm: String): AuthSession {
        return repo.login(username, key, password, fcm)
    }
}