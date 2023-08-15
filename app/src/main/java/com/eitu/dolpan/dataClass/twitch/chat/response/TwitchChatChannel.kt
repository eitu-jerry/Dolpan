package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName

data class TwitchChatChannel(
    @SerializedName("id")
    val id: String,
    @SerializedName("recentChatMessages")
    val recentChatMessages: Array<TwitchChatMessage>
)