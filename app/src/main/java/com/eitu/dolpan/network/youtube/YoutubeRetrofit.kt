package com.eitu.dolpan.network.youtube

import android.app.Activity
import android.content.SharedPreferences
import com.eitu.dolpan.R
import com.eitu.dolpan.network.api.YoutubeAPI
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import com.eitu.dolpan.view.base.BaseViewInterface
import com.google.firebase.firestore.FirebaseFirestore

class YoutubeRetrofit(activity: Activity) {

    private val baseUrl = "https://www.googleapis.com/youtube/v3/"

    private val activity: Activity
    private lateinit var sp: SharedPreferences
    private val fdb: FirebaseFirestore
    private var client: OkHttpClient
    private val youtube = Youtube(activity)
    private lateinit var retrofit: Retrofit
    private lateinit var api: YoutubeAPI

    init {
        this.activity = activity
        //this.sp = (activity as BaseViewInterface).sp
        this.fdb = (activity as BaseViewInterface).fdb
        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun set(post: Boolean): YoutubeAPI {
        val builder = Retrofit.Builder().client(client).baseUrl(baseUrl)

        if (post) {
            builder.addConverterFactory(GsonConverterFactory.create())
        }
        else {
            builder.addConverterFactory(ScalarsConverterFactory.create())
        }

        retrofit = builder.build()
        api = retrofit.create(YoutubeAPI::class.java)

        return api
    }

    fun getPlaylist(playlistId: String) {
        youtube.getPlaylist(set(false).getPlaylist(playlistId), playlistId)
    }

    fun getChannels() {
        val ids = activity.resources.getStringArray(R.array.channelAll)
        youtube.getChannels(set(false).getChannels(ids.joinToString(",")))
    }

}