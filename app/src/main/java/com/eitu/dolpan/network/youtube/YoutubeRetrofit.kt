package com.eitu.dolpan.network.youtube

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

class YoutubeRetrofit(activity: Activity) {

    private val baseUrl = "https://www.googleapis.com/youtube/v3/"

    private val activity: Activity
    private val sp: SharedPreferences
    private val fdb: FirebaseFirestore
    private var client: OkHttpClient
    private val youtube = Youtube(activity)
    private lateinit var retrofit: Retrofit
    private lateinit var api: YoutubeAPI

    init {
        this.activity = activity
        this.sp = (activity as BaseViewInterface).sp
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
        val ids = ArrayList<String>()
        ids.add(activity.getString(R.string.wak_main)) //우왁굳의 게임채널
        ids.add(activity.getString(R.string.wakta_main)) //왁타버스
        ids.add(activity.getString(R.string.ine_main)) //아이네
        ids.add(activity.getString(R.string.jing_main)) //징버거
        ids.add(activity.getString(R.string.lilpa_main)) //릴파
        ids.add(activity.getString(R.string.jururu_main)) //주르르
        ids.add(activity.getString(R.string.gosegu_main)) //고세구
        ids.add(activity.getString(R.string.vichan_main)) //비챤
        ids.add(activity.getString(R.string.wak_replay)) //우왁굳의 계륵
        ids.add(activity.getString(R.string.ine_replay)) //아이네 다시보기
        ids.add(activity.getString(R.string.jing_replay)) //징버거 다시보기
        ids.add(activity.getString(R.string.lilpa_replay)) //릴파 다시보기
        ids.add(activity.getString(R.string.jururu_replay)) //주르르 다시보기
        ids.add(activity.getString(R.string.gosegu_replay)) //고세구 다시보기
        ids.add(activity.getString(R.string.vichan_replay)) //비챤 다시보기
        ids.add(activity.getString(R.string.wak_sub)) //우왁굳의 돚거
        ids.add(activity.getString(R.string.ine_sub)) //아이네 데숙
        ids.add(activity.getString(R.string.jing_sub)) //징버거 짱
        ids.add(activity.getString(R.string.lilpa_sub)) //릴파 꼬꼬
        ids.add(activity.getString(R.string.jururu_sub)) //주르르 봉인
        ids.add(activity.getString(R.string.gosegu_sub)) //고세구 좀더
        ids.add(activity.getString(R.string.vichan_sub)) //비챤 나랑놀

        youtube.getChannels(set(false).getChannels(ids.joinToString(",")))
    }

}