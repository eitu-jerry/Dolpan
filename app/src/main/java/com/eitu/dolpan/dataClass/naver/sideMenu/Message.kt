package com.eitu.dolpan.dataClass.naver.sideMenu


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("error")
    val error: Error = Error(),
    @SerializedName("result")
    val result: Result = Result(),
    @SerializedName("status")
    val status: String = ""
)