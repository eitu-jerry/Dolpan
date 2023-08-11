package com.eitu.dolpan.dataClass.twitch.chat.response

import com.google.gson.annotations.SerializedName

data class TwitchChatMessage(
    @SerializedName("id")
    val id: String,
    @SerializedName("sentAt")
    val sentAt: String,
    @SerializedName("content")
    val content: TwitchChatMessageContent,
    @SerializedName("sender")
    val sender: TwitchChatMessageSender
)
