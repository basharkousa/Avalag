package com.bashar.avalag.src.features.auth.di

import com.bashar.avalag.src.features.auth.data.local.AuthLocalDataSource
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource
import com.bashar.avalag.src.features.auth.domain.usecases.ClearTokenUseCase
import com.bashar.avalag.src.features.auth.domain.usecases.GetTokenUseCase
import com.bashar.avalag.src.features.auth.domain.usecases.SaveTokenUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthLocalModule {

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        impl: AuthLocalDataSource
    ): IAuthLocalDataSource

    companion object {
        @Provides
        fun provideGetTokenUseCase(local: IAuthLocalDataSource) = GetTokenUseCase(local)

        @Provides
        fun provideSaveTokenUseCase(local: IAuthLocalDataSource) = SaveTokenUseCase(local)

        @Provides
        fun provideClearTokenUseCase(local: IAuthLocalDataSource) = ClearTokenUseCase(local)
    }
}