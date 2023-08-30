package com.eitu.dolpan.dataClass.naver.sideMenu


import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("alarm")
    val alarm: Boolean = false,
    @SerializedName("badMenu")
    val badMenu: Boolean = false,
    @SerializedName("boardType")
    val boardType: String = "",
    @SerializedName("cafeId")
    val cafeId: Int = 0,
    @SerializedName("commentAlarm")
    val commentAlarm: Boolean = false,
    @SerializedName("favorite")
    val favorite: Boolean = false,
    @SerializedName("fold")
    val fold: Boolean = false,
    @SerializedName("hasNewArticle")
    val hasNewArticle: Boolean = false,
    @SerializedName("hasRegion")
    val hasRegion: Boolean = false,
    @SerializedName("indent")
    val indent: Boolean = false,
    @SerializedName("lastUpdateDate")
    val lastUpdateDate: String = "",
    @SerializedName("linkUrl")
    val linkUrl: String = "",
    @SerializedName("listOrder")
    val listOrder: Int = 0,
    @SerializedName("menuId")
    val menuId: Int = 0,
    @SerializedName("menuName")
    val menuName: String = "",
    @SerializedName("menuType")
    val menuType: String = "",
    @SerializedName("searchRefDate")
    val searchRefDate: String = "",
    @SerializedName("subscription")
    val subscription: Boolean = false
)