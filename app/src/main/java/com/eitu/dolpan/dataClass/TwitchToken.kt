package com.eitu.dolpan.dataClass

import com.google.gson.annotations.SerializedName

data class TwitchToken(
    @SerializedName("access_token")
    val accessToken : String,
    @SerializedName("expires_in")
    val expires_in : Long,
    @SerializedName("token_type")
    val token_type : String
)
