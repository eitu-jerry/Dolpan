package com.eitu.dolpan.dataClass.twitchChat

import com.google.gson.annotations.SerializedName

data class TwitchChatMessageContent(
    @SerializedName("text")
    val text: String
)
