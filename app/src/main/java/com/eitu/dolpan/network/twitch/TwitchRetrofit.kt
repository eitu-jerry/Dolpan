package com.eitu.dolpan.network.twitch

import android.app.Activity
import android.content.SharedPreferences
import com.eitu.dolpan.R
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import com.eitu.dolpan.view.base.BaseViewInterface
import com.google.firebase.firestore.FirebaseFirestore

class TwitchRetrofit(activity: Activity) {

    private val getTokenUrl = "https://id.twitch.tv/"
    private val isLiveUrl = "https://api.twitch.tv/"

    private val activity: Activity
    private val sp: SharedPreferences
    private val fdb: FirebaseFirestore
    private var client: OkHttpClient
    private val twitch: Twitch
    private lateinit var retrofit: Retrofit
    private lateinit var api: TwitchAPI

    init {
        this.activity = activity
        this.sp = (activity as BaseViewInterface).sp
        this.fdb = (activity as BaseViewInterface).fdb
        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        twitch = Twitch(activity, set(getTokenUrl, true).getAccessToken())
    }

    private fun set(url: String, post: Boolean): TwitchAPI {
        val builder = Retrofit.Builder().client(client).baseUrl(url)

        if (post) {
            builder.addConverterFactory(GsonConverterFactory.create())
        }
        else {
            builder.addConverterFactory(ScalarsConverterFactory.create())
        }

        retrofit = builder.build()
        api = retrofit.create(TwitchAPI::class.java)

        return api
    }

    fun isLive(id: String) {
        val accessToken = sp.getString("twitchAccessToken", "")
        twitch.isLive(set(isLiveUrl, false).isLive("Bearer $accessToken", id))
    }

}