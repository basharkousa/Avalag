package com.bashar.avalag.src.features.auth.di

import com.bashar.avalag.src.features.auth.data.AuthRepo
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import com.bashar.avalag.src.features.auth.domain.usecases.LoginUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepo(impl: AuthRepo): IAuthRepo

    companion object{
        @Provides fun provideLoginUseCase(repo: IAuthRepo) = LoginUseCase(repo)
    }
}