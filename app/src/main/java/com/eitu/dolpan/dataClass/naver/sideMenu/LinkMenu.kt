package com.eitu.dolpan.dataClass.naver.sideMenu


import com.google.gson.annotations.SerializedName

data class LinkMenu(
    @SerializedName("boardType")
    val boardType: String = "",
    @SerializedName("menuName")
    val menuName: String = "",
    @SerializedName("menuType")
    val menuType: String = ""
)