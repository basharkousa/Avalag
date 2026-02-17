package com.bashar.avalag.src.features.appversion.di

import com.bashar.avalag.src.features.appversion.data.AppVersionRepo
import com.bashar.avalag.src.features.appversion.domain.repositories.IAppVersionRepo
import com.bashar.avalag.src.features.appversion.domain.usecase.GetAppVersionInfoUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppVersionModule {

    @Binds
    @Singleton
    abstract fun bindAppVersionRepository(
        impl: AppVersionRepo
    ): IAppVersionRepo

    companion object {

        @Provides
        fun provideGetAppVersionInfoUseCase(
            repo: IAppVersionRepo
        ) = GetAppVersionInfoUseCase(repo)

    }
}
