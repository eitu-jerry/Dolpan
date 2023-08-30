package com.eitu.dolpan.dataClass.naver.sideMenu


import com.google.gson.annotations.SerializedName

data class SideMenu(
    @SerializedName("message")
    val message: Message = Message()
)