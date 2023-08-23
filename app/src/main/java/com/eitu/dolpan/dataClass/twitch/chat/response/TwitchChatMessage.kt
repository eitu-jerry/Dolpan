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
    val sender: TwitchChatMessageSender,
    var sendAtLong : Long = -1L
) {

    private val dateFormat : SimpleDateFormat
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'", Locale.getDefault())

    fun getSendTime() : Long {
        sendAtLong = dateFormat.parse(sentAt)?.time ?: -1
        sendAtLong += 1000 * 60 * 60 * 9

        return sendAtLong
    }

}
