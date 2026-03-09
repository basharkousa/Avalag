package com.bashar.avalag.src.features.splash.di

import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo
import com.bashar.avalag.src.features.splash.domain.usecases.IsFirstLaunchUseCase
import com.bashar.avalag.src.features.splash.domain.usecases.SetFirstLaunchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SplashModule {



    @Provides
    @Singleton
    fun provideIsFirstLaunchUseCase(repo: ISettingRepo): IsFirstLaunchUseCase {
        return IsFirstLaunchUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideSetFirstLaunchUseCase(repo: ISettingRepo): SetFirstLaunchUseCase {
        return SetFirstLaunchUseCase(repo)
    }



}