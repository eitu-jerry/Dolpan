package com.eitu.dolpan.dataClass.naver.cafe


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("adFreeGameCafe")
    val adFreeGameCafe: Boolean,
    @SerializedName("advertMessageList")
    val advertMessageList: List<Any>,
    @SerializedName("articleList")
    val articleList: List<Article>,
    @SerializedName("blockMemberList")
    val blockMemberList: List<String>,
    @SerializedName("cafeId")
    val cafeId: Int,
    @SerializedName("cafeMember")
    val cafeMember: Boolean,
    @SerializedName("cafeName")
    val cafeName: String,
    @SerializedName("cafeStaff")
    val cafeStaff: Boolean,
    @SerializedName("exposePlugReservation")
    val exposePlugReservation: Boolean,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("homeDaAdvertVisible")
    val homeDaAdvertVisible: Boolean,
    @SerializedName("manageMenus")
    val manageMenus: ManageMenus,
    @SerializedName("menuInfo")
    val menuInfo: MenuInfo,
    @SerializedName("recentBoardNoticeList")
    val recentBoardNoticeList: List<RecentBoardNotice>
)