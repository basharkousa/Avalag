package com.bashar.avalag.src.features.appversion.di

import com.bashar.avalag.src.features.appversion.data.remote.AppVersionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppVersionNetworkModule {

    @Provides
    @Singleton
    fun provideAppVersionApi(retrofit: Retrofit): AppVersionApi =
        retrofit.create(AppVersionApi::class.java)
}
