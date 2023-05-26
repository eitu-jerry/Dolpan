package com.eitu.dolpan.network.youtube

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeAPI {

    @GET("playlistItems?key=AIzaSyDAQTrSTT4rwytrm4yOIqVMtshfJhC56uo&part=id,snippet,contentDetails")
    fun getPlaylist(@Query("playlistId") playlistId: String): Call<String>

    @GET("channels?key=AIzaSyDAQTrSTT4rwytrm4yOIqVMtshfJhC56uo&part=id,snippet,brandingSettings&maxResults=10")
    fun getChannels(@Query("id") id: String): Call<String>

}