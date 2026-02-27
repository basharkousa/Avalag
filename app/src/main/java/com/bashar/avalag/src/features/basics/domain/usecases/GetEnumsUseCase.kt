package com.bashar.avalag.src.features.basics.domain.usecases

import com.bashar.avalag.src.features.basics.domain.model.AppEnums
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import javax.inject.Inject

class GetEnumsUseCase @Inject constructor(
    private val repo: IBasicsRepo
) {
    suspend operator fun invoke(): AppEnums = repo.getEnums()
}