package com.eitu.dolpan.dataClass.naver.menu


import com.google.gson.annotations.SerializedName

data class NaverCafeMenuResult(
    @SerializedName("message")
    val message: Message
)