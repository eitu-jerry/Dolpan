package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName

data class TwitchChatData(
    @SerializedName("channel")
    val channel: TwitchChatChannel
)
