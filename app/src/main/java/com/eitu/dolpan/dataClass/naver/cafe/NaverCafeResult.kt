package com.eitu.dolpan.dataClass.naver.cafe


import com.google.gson.annotations.SerializedName

data class NaverCafeResult(
    @SerializedName("message")
    val message: Message
)