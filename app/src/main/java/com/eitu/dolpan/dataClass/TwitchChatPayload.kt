package com.eitu.dolpan.dataClass

import com.google.gson.annotations.SerializedName

data class TwitchChatPayload (
    @SerializedName("operationName")
    val operationName : String = "MessageBufferChatHistory",
    @SerializedName("variables")
    var variables : Variables = Variables(""),
    @SerializedName("extensions")
    val extensions : Extensions = Extensions()
) {

    data class Variables(
        @SerializedName("channelLogin")
        val channelLogin : String
    )

    data class Extensions(
        @SerializedName("persistedQuery")
        val persistedQuery : PersistedQuery = PersistedQuery()
    ) {
        data class PersistedQuery(
            @SerializedName("version")
            val version : Int = 1,
            @SerializedName("sha256Hash")
            val sha256Hash : String = "432ef3ec504a750d797297630052ec7c775f571f6634fdbda255af9ad84325ae",
        )
    }

    fun setChannelId(id : String) : TwitchChatPayload {
        variables = Variables(id)
        return this
    }


}