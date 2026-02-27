package com.bashar.avalag.src.features.basics.di

import com.bashar.avalag.src.features.basics.data.BasicsRepo
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import com.bashar.avalag.src.features.basics.domain.usecases.GetBasicsInfoUseCase
import com.bashar.avalag.src.features.basics.domain.usecases.GetEnumsUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class BasicsModule {

    @Binds
    @Singleton
    abstract fun bindBasicRepo(impl: BasicsRepo): IBasicsRepo

    companion object{
        @Provides fun provideGetEnumsUseCase(repo: IBasicsRepo) = GetEnumsUseCase(repo)
        @Provides fun provideGetBasicsInfoUseCase(repo: IBasicsRepo) = GetBasicsInfoUseCase(repo)
    }
}