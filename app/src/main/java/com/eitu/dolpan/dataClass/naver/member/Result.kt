package com.eitu.dolpan.dataClass.naver.member


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("articleList")
    val articleList: List<Article>,
    @SerializedName("totalCount")
    val totalCount: Int
)