package com.bashar.avalag.src.core.di.hilt.moduls


import com.bashar.avalag.BuildConfig
import com.bashar.avalag.src.core.data.remote.api.AppApi
import com.bashar.avalag.src.core.data.remote.interceptors.PrettyHttpLogger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    private const val TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideHeadersInterceptor(): Interceptor =
        Interceptor { chain ->
            val lang = Locale.getDefault().language.ifBlank { "en" }

            val newRequest = chain.request().newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Accept-Language", lang)
                // TODO (when auth is ready):
                // .header("Authorization", "Bearer $token")
                .build()

            chain.proceed(newRequest)
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        headers: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headers)
//            .addInterceptor(NetworkErrorLoggingInterceptor())
            .addInterceptor(PrettyHttpLogger(tag = "NET"))
            .addInterceptor(logging)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideAppApi(retrofit: Retrofit): AppApi =
        retrofit.create(AppApi::class.java)

}
