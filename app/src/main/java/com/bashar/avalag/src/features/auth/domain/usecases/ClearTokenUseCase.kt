package com.bashar.avalag.src.features.auth.domain.usecases

import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource

class ClearTokenUseCase(
    private val local: IAuthLocalDataSource
) {
    suspend operator fun invoke() = local.clearToken()
}