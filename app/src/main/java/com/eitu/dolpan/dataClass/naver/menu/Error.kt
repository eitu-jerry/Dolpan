package com.eitu.dolpan.dataClass.naver.menu


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: String,
    @SerializedName("msg")
    val msg: String
)