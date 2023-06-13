package com.eitu.dolpan.dataClass.twitchChat

import com.google.gson.annotations.SerializedName

data class TwitchChatMessageSender(
    @SerializedName("id")
    val id: String,
    @SerializedName("displayName")
    val displayName: String
)
