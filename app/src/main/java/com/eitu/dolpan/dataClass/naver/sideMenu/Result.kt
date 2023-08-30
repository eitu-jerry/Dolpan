package com.eitu.dolpan.dataClass.naver.sideMenu


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("linkMenus")
    val linkMenus: List<LinkMenu> = listOf(),
    @SerializedName("menus")
    val menus: List<Menu> = listOf(),
    @SerializedName("styleCode")
    val styleCode: String = ""
)