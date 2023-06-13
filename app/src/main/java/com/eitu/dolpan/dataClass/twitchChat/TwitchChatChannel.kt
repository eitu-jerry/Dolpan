package com.eitu.dolpan.dataClass.twitchChat

import com.google.gson.annotations.SerializedName

data class TwitchChatChannel(
    @SerializedName("id")
    val id: String,
    @SerializedName("recentChatMessages")
    val recentChatMessages: Array<TwitchChatMessage?>
)