package com.eitu.dolpan.network.api

import com.eitu.dolpan.dataClass.TwitchChatPayload
import com.eitu.dolpan.dataClass.TwitchLive
import com.eitu.dolpan.dataClass.TwitchToken
import com.eitu.dolpan.dataClass.twitchChat.TwitchChatItem
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

const val CHAT_CLIENT_ID : String = "kimne78kx3ncx6brgo4mv6wki5h1ko"
const val CHAT_AUTHORIZATION : String = "OAuth 33n243nv1kk3u7jnmc3u5kahfpoltr"
const val CLIENT_ID = "ywq2id2tpi7ke78l8allk0ui8ksh5c"
const val CLIENT_SECRET = "pa22487zs2b9pozb6n88hofhzh0ruv"
const val GRANT_TYPE = "client_credentials"
const val YOUTUBE_API_KEY = "AIzaSyDAQTrSTT4rwytrm4yOIqVMtshfJhC56uo"

interface TwitchUpdateTokenAPI {

    @POST("oauth2/token?" +
            "client_id=${CLIENT_ID}&" +
            "client_secret=${CLIENT_SECRET}&" +
            "grant_type=${GRANT_TYPE}")
    suspend fun getAccessToken(): Response<TwitchToken>

}

interface TwitchCheckLiveAPI {

    @Headers("Client-ID: ${CLIENT_ID}")
    @GET("helix/search/channels")
    suspend fun isLive(@Header("Authorization") accessToken: String, @Query("query") id:String): Response<TwitchLive>

}

interface TwitchGetChatAPI {

//    @Headers({
//        "Client-ID: ${CHAT_CLIENT_ID}",
//        "Authorization : ${CHAT_AUTHORIZATION}"
//    })
    @POST("gql")
    suspend fun getChat(
        @Header("Client-ID") clientId : String = CHAT_CLIENT_ID,
        @Header("Authorization") authorization : String = CHAT_AUTHORIZATION,
        @Body payload: TwitchChatPayload
    ): Response<TwitchChatItem>

}

interface YoutubeAPI_ {

    @GET("playlistItems?" +
            "part=id,snippet,contentDetails&" +
            "key=${YOUTUBE_API_KEY}")
    fun getPlaylist(@Query("playlistId") playlistId: String): Call<String>

    @GET("channels?" +
            "part=id,snippet,brandingSettings&" +
            "maxResults=10&" +
            "key=${YOUTUBE_API_KEY}")
    fun getChannels(@Query("id") id: String): Call<String>

}