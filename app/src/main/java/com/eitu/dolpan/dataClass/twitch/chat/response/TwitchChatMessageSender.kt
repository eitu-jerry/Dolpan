package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName

data class TwitchChatMessageSender(
    @SerializedName("id")
    val id: String,
    @SerializedName("displayName")
    val displayName: String
)
