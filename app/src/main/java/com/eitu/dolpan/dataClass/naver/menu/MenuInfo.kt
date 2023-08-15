package com.eitu.dolpan.dataClass.naver.menu


import com.google.gson.annotations.SerializedName

data class MenuInfo(
    @SerializedName("boardType")
    val boardType: String,
    @SerializedName("menuId")
    val menuId: Int,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("menuType")
    val menuType: String
)