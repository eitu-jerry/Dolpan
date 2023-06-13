package com.eitu.dolpan.dataClass.twitchChat

import com.google.gson.annotations.SerializedName

data class TwitchChatData(
    @SerializedName("channel")
    val channel: TwitchChatChannel
)
