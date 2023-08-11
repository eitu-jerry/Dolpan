package com.eitu.dolpan.dataClass.twitch

import com.google.gson.annotations.SerializedName

data class TwitchLive(
    @SerializedName("data")
    val data : List<Broadcaster>
) {
    data class Broadcaster(
        @SerializedName("broadcaster_login")
        val broadcaster : String,
        @SerializedName("is_live")
        val isLive : Boolean
    )
}