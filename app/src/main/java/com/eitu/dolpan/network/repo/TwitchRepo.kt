package com.eitu.dolpan.network.repo

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.eitu.dolpan.dataClass.twitch.chat.request.TwitchChatPayload
import com.eitu.dolpan.network.DolpanResult
import com.eitu.dolpan.network.api.TwitchCheckLiveAPI
import com.eitu.dolpan.network.api.TwitchGetChatAPI
import com.eitu.dolpan.network.api.TwitchUpdateTokenAPI
import com.eitu.dolpan.network.returnResult
import javax.inject.Inject

class TwitchRepo @Inject constructor(
    private val sp : SharedPreferences,
    private val updateTokenAPI : TwitchUpdateTokenAPI,
    private val checkLiveAPI: TwitchCheckLiveAPI,
    private val getChatAPI: TwitchGetChatAPI
) {

    private val members = arrayOf("woowakgood", "vo_ine", "jingburger", "lilpaaaaaa", "cotton__123", "gosegugosegu", "viichan6")

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

    suspend fun getChat(channelId : String) {
        try {
            val result = returnResult(getChatAPI.getChat(payload = TwitchChatPayload().setChannelId(channelId)))
            when(result) {
                is DolpanResult.Success -> {
                    val channelOwner = result.data.data.channel.id
                    val recentMessages = result.data.data.channel.recentChatMessages
                    recentMessages.filter { it?.sender?.id == channelOwner }.forEach {
                        it?.let { chat -> Log.d("getChat", chat.content.text) }
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

}