package com.eitu.dolpan.network.twitch

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface TwitchAPI {

    object ID {
        const val client_id = "ywq2id2tpi7ke78l8allk0ui8ksh5c"
        const val client_secret = "pa22487zs2b9pozb6n88hofhzh0ruv"
        const val grant_type = "client_credentials"
    }

    @POST("oauth2/token?" +
            "client_id=${ID.client_id}&" +
            "client_secret=${ID.client_secret}&" +
            "grant_type=${ID.grant_type}")
    fun getAccessToken(): Call<JsonObject>

    @Headers("Client-ID: ${ID.client_id}")
    @GET("helix/search/channels")
    fun isLive(@Header("Authorization") accessToken: String, @Query("query") id:String): Call<String>

}