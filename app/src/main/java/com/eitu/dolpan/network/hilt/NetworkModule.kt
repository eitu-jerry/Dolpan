package com.eitu.dolpan.network.hilt

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context)
    = context.getSharedPreferences("PRFS", Context.MODE_PRIVATE)

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @TwitchUpdateTokenUrl
    fun provideTwitchUpdateTokenUrl() = "https://id.twitch.tv/"

    @Provides
    @TwitchCheckLiveUrl
    fun provideTwitchCheckLiveUrl() = "https://api.twitch.tv/"

    @Provides
    @TwitchGetChatUrl
    fun provideTwitchGetChatUrl() = "https://gql.twitch.tv/"

    @Provides
    @YoutubeUrl
    fun provideYoutubeUrl() = "https://www.googleapis.com/youtube/v3/"

    @Provides
    @NaverCafeUrl
    fun provideNaverCafeUrl() = "https://apis.naver.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient()
    = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @TwitchUpdateTokenRetrofit
    fun provideTwitchUpdateTokenRetrofit(@TwitchUpdateTokenUrl baseUrl : String, client: OkHttpClient)
    = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @TwitchCheckLiveRetrofit
    fun provideTwitchCheckLiveRetrofit(@TwitchCheckLiveUrl baseUrl : String, client: OkHttpClient)
    = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @TwitchGetChatRetrofit
    fun provideTwitchGetChatRetrofit(@TwitchGetChatUrl baseUrl : String, client: OkHttpClient)
    = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @YoutubeRetrofit
    fun provideYoutubeRetrofit(@YoutubeUrl baseUrl : String, client: OkHttpClient)
    = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @NaverCafeRetrofit
    fun provideNaverCafeRetrofit(@NaverCafeUrl baseUrl : String, client: OkHttpClient)
    = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideTwitchUpdateTokenAPI(@TwitchUpdateTokenRetrofit retrofit: Retrofit)
    = retrofit.create(com.eitu.dolpan.network.api.TwitchUpdateTokenAPI::class.java)

    @Provides
    @Singleton
    fun provideTwitchCheckLiveAPI(@TwitchCheckLiveRetrofit retrofit: Retrofit)
            = retrofit.create(com.eitu.dolpan.network.api.TwitchCheckLiveAPI::class.java)

    @Provides
    @Singleton
    fun provideTwitchGetChatAPI(@TwitchGetChatRetrofit retrofit: Retrofit)
            = retrofit.create(com.eitu.dolpan.network.api.TwitchGetChatAPI::class.java)

    @Provides
    @Singleton
    fun provideYoutubeAPI(@YoutubeRetrofit retrofit: Retrofit)
            = retrofit.create(com.eitu.dolpan.network.api.YoutubeAPI::class.java)

    @Provides
    @Singleton
    fun provideNaverCafeAPI(@NaverCafeRetrofit retrofit: Retrofit)
            = retrofit.create(com.eitu.dolpan.network.api.NaverCafeAPI::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchUpdateTokenUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchCheckLiveUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchGetChatUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverCafeUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchUpdateTokenRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchCheckLiveRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwitchGetChatRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverCafeRetrofit