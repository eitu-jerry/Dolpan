package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class TwitchChatMessage(
    @SerializedName("id")
    val id: String,
    @SerializedName("sentAt")
    val sentAt: String,
    @SerializedName("content")
    val content: TwitchChatMessageContent,
    @SerializedName("sender")
    val sender: TwitchChatMessageSender
) {

    private val dateFormat : SimpleDateFormat
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'", Locale.getDefault())

    fun getSendTime() : Long {
        return try {
            dateFormat.parse(sentAt).time
        } catch (e : Exception) {
            0
        }
    }

}
