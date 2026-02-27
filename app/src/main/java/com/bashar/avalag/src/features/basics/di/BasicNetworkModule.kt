package com.bashar.avalag.src.features.basics.di

import com.bashar.avalag.src.features.basics.data.remote.BasicsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BasicNetworkModule {

    @Provides
    @Singleton
    fun provideBasicApi(retrofit: Retrofit): BasicsApi =
        retrofit.create(BasicsApi::class.java)

}