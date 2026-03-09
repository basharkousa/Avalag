package com.bashar.avalag.src.features.splash.domain.usecases

import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo
import javax.inject.Inject

class SetFirstLaunchUseCase @Inject constructor(val rep: ISettingRepo) {
    suspend operator fun invoke(value: Boolean) = rep.setFirstLaunch(value)
}