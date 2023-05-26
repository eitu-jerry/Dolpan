package com.eitu.dolpan.network.youtube

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeAPI {

    object ApiKey{
        const val apiKey = "AIzaSyDAQTrSTT4rwytrm4yOIqVMtshfJhC56uo"
    }

    @GET("playlistItems?" +
            "part=id,snippet,contentDetails&" +
            "key=${ApiKey.apiKey}")
    fun getPlaylist(@Query("playlistId") playlistId: String): Call<String>

    @GET("channels?" +
            "part=id,snippet,brandingSettings&" +
            "maxResults=10&" +
            "key=${ApiKey.apiKey}")
    fun getChannels(@Query("id") id: String): Call<String>

}