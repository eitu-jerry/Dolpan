package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName

data class TwitchChatItem(
    @SerializedName("data")
    val data: TwitchChatData
)
