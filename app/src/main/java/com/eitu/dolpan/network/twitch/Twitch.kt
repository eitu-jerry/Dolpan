package com.eitu.dolpan.network.twitch

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.eitu.dolpan.network.ResponseController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Twitch(activity: Activity, whenFail: Call<JsonObject>) {

    private val activity: Activity
    private val whenFail: Call<JsonObject>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    init {
        this.activity = activity
        this.whenFail = whenFail
    }

    fun getAccessToken(call: Call<JsonObject>) {
        call.enqueue(object : ResponseController<JsonObject>("getAccessToken", object : OnResponseListener<JsonObject> {
            override fun onSuccess(response: Response<JsonObject>) {
                val accessToken = response.body()?.get("access_token")?.asString
                Log.d("getAccessToken", accessToken.toString())
                activity.getSharedPreferences("PRFS", 0).edit()
                    .putString("twitchAccessToken", accessToken)
                    .apply()
            }

            override fun onFail(response: Response<JsonObject>) {
                Log.d("getAccessToken", "fail")
            }
        }){})
    }

    fun isLive(call: Call<String>) {
        call.enqueue(object : ResponseController<String>("isLive", object : OnResponseListener<String> {
            override fun onSuccess(response: Response<String>) {
                try {
                    val result = response.body()?.let { JSONObject(it) }
                    if (result != null) {
                        val data = result.getJSONArray("data").getJSONObject(0)
                        val id = data.getString("broadcaster_login")
                        val isLive = data.getBoolean("is_live")
                        Log.d("id", id)
                        Firebase.firestore.collection("youtubeMember")
                            .document(when(id) {
                                "woowakgood" -> "wak"
                                "vo_ine" -> "ine"
                                "jingburger" -> "jing"
                                "lilpaaaaaa" -> "lilpa"
                                "cotton__123" -> "jururu"
                                "gosegugosegu" -> "gosegu"
                                "viichan6" -> "vichan"
                                else -> ""
                            })
                            .update("isLive", isLive)
                            .addOnSuccessListener {
                                Log.d(id, "isLive = $isLive")
                            }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail(response: Response<String>) {
                getAccessToken(whenFail)
            }
        }){})
    }

}