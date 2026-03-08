package com.bashar.avalag.src.features.auth.domain.usecases

import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource

class SaveTokenUseCase(
    private val local: IAuthLocalDataSource
) {
    suspend operator fun invoke(token: String) = local.saveToken(token)
}