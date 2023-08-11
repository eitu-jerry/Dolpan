package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName

data class TwitchChatMessageContent(
    @SerializedName("text")
    val text: String
)
