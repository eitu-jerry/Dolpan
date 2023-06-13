package com.eitu.dolpan.dataClass.twitchChat

import com.google.gson.annotations.SerializedName

data class TwitchChatItem(
    @SerializedName("data")
    val data: TwitchChatData
)
