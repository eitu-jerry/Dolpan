package com.eitu.dolpan.dataClass.naver.cafe


import com.google.gson.annotations.SerializedName

data class ManageMenus(
    @SerializedName("showActivityStop")
    val showActivityStop: Boolean,
    @SerializedName("showArticleDelete")
    val showArticleDelete: Boolean,
    @SerializedName("showArticleMove")
    val showArticleMove: Boolean,
    @SerializedName("showBoardNotice")
    val showBoardNotice: Boolean,
    @SerializedName("showLevelUp")
    val showLevelUp: Boolean,
    @SerializedName("showOneBoardNotice")
    val showOneBoardNotice: Boolean,
    @SerializedName("showPopularArticleHide")
    val showPopularArticleHide: Boolean,
    @SerializedName("showReportBadArticle")
    val showReportBadArticle: Boolean,
    @SerializedName("showRequiredNotice")
    val showRequiredNotice: Boolean,
    @SerializedName("showSecede")
    val showSecede: Boolean
)