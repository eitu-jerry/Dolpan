package com.eitu.dolpan.dataClass.naver.member


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("error")
    val error: Error,
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: String
)