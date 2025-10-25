package com.bashar.avalag.src.features.setting.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.bashar.avalag.src.features.setting.data.SettingRepo
import com.bashar.avalag.src.features.setting.data.local.datastore.SettingPreferencesDataSource
import com.bashar.avalag.src.features.setting.data.system.AppLocaleControllerImpl
import com.bashar.avalag.src.features.setting.domain.*
import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo
import com.bashar.avalag.src.features.setting.domain.services.AppLocaleController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingModule {

    @Provides @Singleton
    fun provideDs(ds: DataStore<Preferences>) = SettingPreferencesDataSource(ds)

    @Provides @Singleton
    fun provideRepo(ds: SettingPreferencesDataSource): ISettingRepo = SettingRepo(ds)

    @Provides @Singleton
    fun provideLocaleController(): AppLocaleController = AppLocaleControllerImpl()

    // Use cases
    @Provides fun getTheme(repo: ISettingRepo) = GetThemeModeFlowUseCase(repo)
    @Provides fun setTheme(repo: ISettingRepo) = SetThemeModeUseCase(repo)
    @Provides fun getLang(repo: ISettingRepo)  = GetLanguageFlowUseCase(repo)
    @Provides fun setLang(repo: ISettingRepo)  = SetLanguageUseCase(repo)
    @Provides fun applyLang(controller: AppLocaleController) = ApplyLanguageUseCase(controller)
}
