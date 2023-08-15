package com.eitu.dolpan.dataClass.naver.menu


import com.google.gson.annotations.SerializedName

data class ProductSale(
    @SerializedName("cost")
    val cost: String,
    @SerializedName("saleStatus")
    val saleStatus: String
)