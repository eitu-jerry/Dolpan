package com.eitu.dolpan.dataClass.naver.cafe


import com.google.gson.annotations.SerializedName

data class RecentBoardNotice(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("newNotice")
    val newNotice: Boolean,
    @SerializedName("subject")
    val subject: String
)