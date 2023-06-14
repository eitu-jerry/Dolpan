package com.eitu.dolpan.network.twitch

import android.app.Activity
import android.util.Log
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.twitchChat.TwitchChatItem
import com.eitu.dolpan.network.ResponseController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Twitch(activity: Activity, whenFail: Call<JsonObject>) {

    private val activity: Activity
    private val whenFail: Call<JsonObject>
    private val liveMap = hashMapOf(
        Pair("woowakgood", false),
        Pair("vo_ine", false),
        Pair("jingburger", false),
        Pair("lilpaaaaaa", false),
        Pair("cotton__123", false),
        Pair("gosegugosegu", false),
        Pair("viichan6", false),
    )

    init {
        this.activity = activity
        this.whenFail = whenFail
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
                        if (activity.resources.getStringArray(R.array.twitch).contains(id)) {
                            if (isLive != liveMap[id]) {
                                liveMap[id] = isLive
                                Log.d("twitchLive", "$id's live is $isLive")
                                Firebase.firestore
                                    .collection("youtubeMember")
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
                                        Log.d("twitchLive", "$id's live is updated to $isLive")
                                    }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFail(response: Response<String>) {
                getAccessToken()
            }
        }){})
    }

    private fun getAccessToken() {
        whenFail.enqueue(object : ResponseController<JsonObject>("getAccessToken", object : OnResponseListener<JsonObject> {
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

    val wak = "49045679"
    val ine = "702754423"
    val jing = "237570548"
    val lilpa = "169700336"
    val jururu = "203667951"
    val gosegu = "707328484"
    val vichan = "195641865"
    val me = "165930742"
    val twitchIds = arrayOf(wak, ine, jing, lilpa, jururu, gosegu, vichan, me)
    val fromDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val toDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    fun getChat(call: Call<Array<TwitchChatItem>>) {
        call.enqueue(object : ResponseController<Array<TwitchChatItem>>("getChat", object : OnResponseListener<Array<TwitchChatItem>> {
            override fun onSuccess(response: Response<Array<TwitchChatItem>>) {
                val item = response.body()?.get(0)
                if (item != null) {
                    try {
                        val channelId = item.data.channel.id
                        val messages = item.data.channel.recentChatMessages

                        try {
                            for (message in messages) {
                                if (message == null) continue
                                val senderId = message.sender.id
                                if (twitchIds.contains(senderId)) {
                                    val sender: String
                                    val owner: String

                                    when(senderId) {
                                        wak -> {
                                            sender = "우왁굳"
                                            owner = "wak"
                                        }
                                        ine -> {
                                            sender = "아이네"
                                            owner = "ine"
                                        }
                                        jing -> {
                                            sender = "징버거"
                                            owner = "jing"
                                        }
                                        lilpa -> {
                                            sender = "릴파"
                                            owner = "lilipa"
                                        }
                                        jururu -> {
                                            sender = "주르르"
                                            owner = "jururu"
                                        }
                                        gosegu -> {
                                            sender = "고세구"
                                            owner = "gosegu"
                                        }
                                        vichan -> {
                                            sender = "비챤"
                                            owner = "vichan"
                                        }
                                        else -> {
                                            sender = "나"
                                            owner = "wak"
                                        }
                                    }

                                    val sendTo: String = when(channelId) {
                                        wak -> "우왁굳"
                                        ine -> "아이네"
                                        jing -> "징버거"
                                        lilpa -> "릴파"
                                        jururu -> "주르르"
                                        gosegu -> "고세구"
                                        else -> "비챤"
                                    }

                                    val title: String
                                    val content = message.content.text
                                    title = if (senderId == channelId) {
                                        content
                                    } else {
                                        "$sender -> $sendTo\n\n$content"
                                    }

                                    val from = fromDate.parse(message.sentAt)!!
                                    from.time = from.time + (1000 * 60 * 60 * 9)
                                    val date = toDate.format(from)

                                    Firebase.firestore.collection("item")
                                        .document(message.id)
                                        .get()
                                        .addOnSuccessListener {
                                            if (!it.exists()) {
                                                Firebase.firestore.collection("item")
                                                    .document(message.id)
                                                    .set(hashMapOf(
                                                        Pair("owner", owner),
                                                        Pair("type", "twitchChat"),
                                                        Pair("title", title),
                                                        Pair("date", date)
                                                    ))
                                                    .addOnSuccessListener {
                                                        Log.d("from ${message.sender.displayName}", title)
                                                    }
                                            }
                                        }


                                }
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFail(response: Response<Array<TwitchChatItem>>) {

            }
        }){})
    }

}