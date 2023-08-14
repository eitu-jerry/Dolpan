package com.eitu.dolpan.dataClass.naver.cafe


import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Article(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("attachCalendar")
    val attachCalendar: Boolean,
    @SerializedName("attachFile")
    val attachFile: Boolean,
    @SerializedName("attachGpx")
    val attachGpx: Boolean,
    @SerializedName("attachImage")
    val attachImage: Boolean,
    @SerializedName("attachLink")
    val attachLink: Boolean,
    @SerializedName("attachMap")
    val attachMap: Boolean,
    @SerializedName("attachMovie")
    val attachMovie: Boolean,
    @SerializedName("attachMusic")
    val attachMusic: Boolean,
    @SerializedName("attachPoll")
    val attachPoll: Boolean,
    @SerializedName("blindArticle")
    val blindArticle: Boolean,
    @SerializedName("blogScrap")
    val blogScrap: Boolean,
    @SerializedName("boardType")
    val boardType: String,
    @SerializedName("cafeId")
    val cafeId: Int,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("cost")
    val cost: Int,
    @SerializedName("delParent")
    val delParent: Boolean,
    @SerializedName("enableComment")
    val enableComment: Boolean,
    @SerializedName("escrow")
    val escrow: Boolean,
    @SerializedName("formattedCost")
    val formattedCost: String,
    @SerializedName("hasNewComment")
    val hasNewComment: Boolean,
    @SerializedName("imageAttachCount")
    val imageAttachCount: Int,
    @SerializedName("lastCommentedTimestamp")
    val lastCommentedTimestamp: Long,
    @SerializedName("likeItCount")
    val likeItCount: Int,
    @SerializedName("marketArticle")
    val marketArticle: Boolean,
    @SerializedName("memberKey")
    val memberKey: String,
    @SerializedName("memberLevel")
    val memberLevel: Int,
    @SerializedName("memberLevelIconId")
    val memberLevelIconId: Int,
    @SerializedName("menuId")
    val menuId: Int,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("menuType")
    val menuType: String,
    @SerializedName("newArticle")
    val newArticle: Boolean,
    @SerializedName("noticeType")
    val noticeType: String,
    @SerializedName("onSale")
    val onSale: Boolean,
    @SerializedName("openArticle")
    val openArticle: Boolean,
    @SerializedName("popular")
    val popular: Boolean,
    @SerializedName("productSale")
    val productSale: ProductSale,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("readCount")
    val readCount: Int,
    @SerializedName("refArticleCount")
    val refArticleCount: Int,
    @SerializedName("refArticleId")
    val refArticleId: Int,
    @SerializedName("replyArticle")
    val replyArticle: Boolean,
    @SerializedName("replyListOrder")
    val replyListOrder: String,
    @SerializedName("representImage")
    val representImage: String,
    @SerializedName("representImageType")
    val representImageType: String,
    @SerializedName("restrictMenu")
    val restrictMenu: Boolean,
    @SerializedName("showNoticeDelete")
    val showNoticeDelete: Boolean,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("useSafetyPayment")
    val useSafetyPayment: Boolean,
    @SerializedName("writeDateTimestamp")
    val writeDateTimestamp: Long,
    @SerializedName("writerNickname")
    val writerNickname: String
) {

    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

//    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        get() = if (field != null) field else SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val oneSecond: Long = 1000
        get() = if (field > 0) field else 1000
    private val oneMinute: Long = oneSecond * 60
        get() = if (field > 0) field else oneSecond * 60
    private val oneHour: Long = oneMinute * 60
        get() = if (field > 0) field else oneMinute * 60
    private val oneDay: Long = oneHour * 24
        get() = if (field > 0) field else oneHour * 24

    fun formatWriteDate() : String {
        val thisTime = System.currentTimeMillis()

        val timeDifference = thisTime - writeDateTimestamp

        if (timeDifference < oneDay) {
            return if (timeDifference < oneMinute) {
                "방금 전"
            }
            else if (timeDifference < oneHour) {
                "${timeDifference / oneMinute}분 전"
            }
            else {
                "${timeDifference / oneHour}시간 전"
            }
        }
        else {
            val dayDifference = timeDifference / oneDay
            return if (dayDifference < 4) {
                "${dayDifference}일 전"
            }
            else {
                dateFormat.format(writeDateTimestamp)
            }
        }
    }
}