package com.bashar.avalag.src.features.basics.domain.usecases

import com.bashar.avalag.src.features.basics.domain.model.BasicsInfo
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import jakarta.inject.Inject

class GetBasicsInfoUseCase @Inject constructor(
    private val repo: IBasicsRepo
) {
    suspend operator fun invoke(): BasicsInfo = repo.getBasicsInfo()
}