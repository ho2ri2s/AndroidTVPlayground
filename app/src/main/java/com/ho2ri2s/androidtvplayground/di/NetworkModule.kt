package com.ho2ri2s.androidtvplayground.di

import com.ho2ri2s.androidtvplayground.data.QiitaApi
import com.ho2ri2s.androidtvplayground.data.QiitaApiClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
  private const val QIITA_API_BASE_URL = "https://qiita.com/api/v2/"
  private const val MEDIA_TYPE_JSON = "application/json"
  private const val NAMED_JSON = "json"

  private val json = Json { ignoreUnknownKeys = true }

  @Provides
  @Singleton
  @Named(NAMED_JSON)
  fun provideContentType(): MediaType {
    return MEDIA_TYPE_JSON.toMediaType()
  }

  @Provides
  @Singleton
  fun provideOkhttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder()
      .connectTimeout(10, TimeUnit.SECONDS)
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
      })
      .build()
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Provides
  @Singleton
  fun provideQiitaRetrofit(
    okHttpClient: OkHttpClient,
    @Named(NAMED_JSON) mediaType: MediaType,
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(QIITA_API_BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(json.asConverterFactory(mediaType))
      .build()
  }
}

@InstallIn(SingletonComponent::class)
@Module
interface QiitaApiModule {
  @Binds
  @Singleton
  fun bindQiitaApi(
    apiClient: QiitaApiClient
  ): QiitaApi
}