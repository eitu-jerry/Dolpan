package com.eitu.dolpan.network.repo

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.dataClass.twitch.chat.request.TwitchChatPayload
import com.eitu.dolpan.dataClass.twitch.chat.response.TwitchChatMessage
import com.eitu.dolpan.network.DolpanResult
import com.eitu.dolpan.network.api.TwitchCheckLiveAPI
import com.eitu.dolpan.network.api.TwitchGetChatAPI
import com.eitu.dolpan.network.api.TwitchUpdateTokenAPI
import com.eitu.dolpan.network.returnResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TwitchRepo @Inject constructor(
    private val sp : SharedPreferences,
    private val fdb : FirebaseFirestore,
    private val updateTokenAPI : TwitchUpdateTokenAPI,
    private val checkLiveAPI: TwitchCheckLiveAPI,
    private val getChatAPI: TwitchGetChatAPI
) {

    /**
     * woowakgood   49045679
     * vo_ine       702754423
     * jingburger   237570548
     * lilpaaaaaa   169700336
     * cotton__123  203667951
     * gesegugosegu 707328484
     * viichan6     195641865
     */
    private val members = arrayOf("49045679", "702754423", "237570548", "169700336", "203667951", "707328484", "195641865")
    private val memberMap = hashMapOf(
        Pair("49045679", "wak"),
        Pair("702754423", "ine"),
        Pair("237570548", "jing"),
        Pair("169700336", "lilpa"),
        Pair("203667951", "jururu"),
        Pair("707328484", "gosegu"),
        Pair("195641865", "vichan")
    )
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    suspend fun updateToken() {
        try {
            val result = returnResult(updateTokenAPI.getAccessToken())
            when(result) {
                is DolpanResult.Success -> {
                    val accessToken = "Bearer ${result.data.accessToken}"
                    Log.d("accessToken", accessToken)
                    sp.edit {
                        putString("accessToken", accessToken)
                        apply()
                    }
                }
                is DolpanResult.Error -> {
                    result.exception.printStackTrace()
                }
                else -> {

                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    suspend fun checkLive(query : String) {
        try {
            val accessToken = sp.getString("accessToken", "")!!

            val result = returnResult(checkLiveAPI.isLive(accessToken, query))
            when(result) {
                is DolpanResult.Success -> {
                    result.data.data.find { it.broadcaster == query }?.let {
                        Log.d("checkLive", "${it.broadcaster} : ${it.isLive}")
                    }
                }
                is DolpanResult.Error -> {
                    result.exception.printStackTrace()
                }
                else -> {

                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getChat(channelId : String, lastMessageTime : Long?) : Long? {
        try {
            val result = returnResult(getChatAPI.getChat(payload = TwitchChatPayload().setChannelId(channelId)))
            when(result) {
                is DolpanResult.Success -> {
                    val channelOwner = result.data.data.channel.id
                    val messages = result.data.data.channel.recentChatMessages

                    if (messages.isEmpty()) throw Exception("$channelId's message is empty")

                    var filteredMessages = messages.filter { members.contains(it.sender.id) }

                    if (filteredMessages.isEmpty()) throw Exception("$channelId's message is empty")

                    if (lastMessageTime != null) {
                        filteredMessages = filteredMessages.filter { it.getSendTime() > lastMessageTime }
                    }

                    if (filteredMessages.isEmpty()) throw Exception("$channelId's new message is empty")

                    CoroutineScope(Dispatchers.IO).launch {
                        filteredMessages.filter { members.contains(it.sender.id) }.forEach {
                            val sender = it.sender.id
                            fdb.collection("item")
                                .add(Chat(
                                    owner = memberMap[channelOwner] ?: "",
                                    type = "twitchChat",
                                    date = dateFormat.format(System.currentTimeMillis()),
                                    title = it.content.text,
                                    sendFrom = if (sender == channelOwner) null else memberMap[sender]
                                ))
                                .await()
                        }
                    }

                    return messages.last().getSendTime()
                }
                is DolpanResult.Error -> {
                    result.exception.printStackTrace()
                }
                else -> {

                }
            }
        } catch (e : Exception) {
            //e.printStackTrace()
            Log.d("getChat", e.message.toString())
        }

        return lastMessageTime
    }

}