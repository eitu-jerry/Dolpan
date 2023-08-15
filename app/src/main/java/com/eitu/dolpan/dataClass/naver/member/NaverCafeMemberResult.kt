package com.eitu.dolpan.dataClass.naver.member


import com.google.gson.annotations.SerializedName

data class NaverCafeMemberResult(
    @SerializedName("message")
    val message: Message
)