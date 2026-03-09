package com.bashar.avalag.src.features.splash.domain.usecases

import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo
import javax.inject.Inject

class IsFirstLaunchUseCase @Inject constructor(val rep: ISettingRepo){
    suspend operator fun invoke(): Boolean = rep.isFirstLaunch()
}