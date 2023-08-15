package com.eitu.dolpan.dataClass.naver.member


import com.google.gson.annotations.SerializedName

data class ClubMenu(
    @SerializedName("anonyyn")
    val anonyyn: String,
    @SerializedName("boardtype")
    val boardtype: String,
    @SerializedName("cafeRcode1")
    val cafeRcode1: CafeRcode1,
    @SerializedName("cafeRcode2")
    val cafeRcode2: CafeRcode1,
    @SerializedName("cafeRcode3")
    val cafeRcode3: CafeRcode1,
    @SerializedName("clubid")
    val clubid: Int,
    @SerializedName("commentlevel")
    val commentlevel: Int,
    @SerializedName("createdt")
    val createdt: String,
    @SerializedName("defaultWriteOpenType")
    val defaultWriteOpenType: Int,
    @SerializedName("favoriteCafeMenu")
    val favoriteCafeMenu: Boolean,
    @SerializedName("indentyn")
    val indentyn: String,
    @SerializedName("isSubscribed")
    val isSubscribed: Boolean,
    @SerializedName("kinQuestionLinkUse")
    val kinQuestionLinkUse: Boolean,
    @SerializedName("lastupdatedt")
    val lastupdatedt: String,
    @SerializedName("listorder")
    val listorder: Int,
    @SerializedName("menudesc")
    val menudesc: String,
    @SerializedName("menuid")
    val menuid: Int,
    @SerializedName("menuname")
    val menuname: String,
    @SerializedName("menutype")
    val menutype: String,
    @SerializedName("rcodeModifyCount")
    val rcodeModifyCount: Int,
    @SerializedName("readlevel")
    val readlevel: Int,
    @SerializedName("searchrefdate")
    val searchrefdate: String,
    @SerializedName("uparticleviewcount")
    val uparticleviewcount: Int,
    @SerializedName("useComment")
    val useComment: Boolean,
    @SerializedName("usehead")
    val usehead: Boolean,
    @SerializedName("useuparticle")
    val useuparticle: Boolean,
    @SerializedName("writelevel")
    val writelevel: Int
)