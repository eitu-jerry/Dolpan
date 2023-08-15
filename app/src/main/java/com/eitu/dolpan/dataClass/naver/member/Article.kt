package com.eitu.dolpan.dataClass.naver.member


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Article(
    @SerializedName("articleid")
    val articleid: Int,
    @SerializedName("attachCalendar")
    val attachCalendar: Boolean,
    @SerializedName("attachLink")
    val attachLink: Boolean,
    @SerializedName("attachMap")
    val attachMap: Boolean,
    @SerializedName("attachMusic")
    val attachMusic: Boolean,
    @SerializedName("attachPollyn")
    val attachPollyn: Boolean,
    @SerializedName("attachedCalendar")
    val attachedCalendar: Boolean,
    @SerializedName("attachfileyn")
    val attachfileyn: String,
    @SerializedName("attachimageyn")
    val attachimageyn: String,
    @SerializedName("attachmovie")
    val attachmovie: Boolean,
    @SerializedName("autoSourcing")
    val autoSourcing: Int,
    @SerializedName("cafeBookYn")
    val cafeBookYn: Boolean,
    @SerializedName("ccl")
    val ccl: Int,
    @SerializedName("clubMenu")
    val clubMenu: ClubMenu,
    @SerializedName("clubid")
    val clubid: Int,
    @SerializedName("commentcount")
    val commentcount: Int,
    @SerializedName("delParent")
    val delParent: Boolean,
    @SerializedName("hastag")
    val hastag: Boolean,
    @SerializedName("imageMovieAttachCount")
    val imageMovieAttachCount: Int,
    @SerializedName("lastcommentdate")
    val lastcommentdate: String,
    @SerializedName("liked")
    val liked: Boolean,
    @SerializedName("menuid")
    val menuid: Int,
    @SerializedName("openyn")
    val openyn: String,
    @SerializedName("rclick")
    val rclick: Int,
    @SerializedName("readableBlindArticle")
    val readableBlindArticle: Boolean,
    @SerializedName("readcount")
    val readcount: Int,
    @SerializedName("refarticlecount")
    val refarticlecount: Int,
    @SerializedName("refarticleid")
    val refarticleid: Int,
    @SerializedName("replylistorder")
    val replylistorder: String,
    @SerializedName("replyyn")
    val replyyn: String,
    @SerializedName("scrapcount")
    val scrapcount: Int,
    @SerializedName("scrapedyn")
    val scrapedyn: String,
    @SerializedName("scrapyn")
    val scrapyn: String,
    @SerializedName("searchopen")
    val searchopen: Boolean,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("upcount")
    val upcount: Int,
    @SerializedName("writedt")
    val writedt: String,
    @SerializedName("writerMemberKey")
    val writerMemberKey: String,
    @SerializedName("writerid")
    val writerid: String,
    @SerializedName("writernickname")
    val writernickname: String
) {

    private val dateFormat : SimpleDateFormat
        get() = SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa", Locale.ENGLISH)
    private val dateFormat_ : SimpleDateFormat
        get() = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

    fun getWriteTime() : Long {
        val time = dateFormat.parse(writedt).time
        return try {
            if (writedt.split(" ").last() == "PM")
                time + 1000 * 60 * 60 * 12
            else
                time
        } catch (e : Exception) {
            0
        }
    }

    fun getFormattedWriteTime() : String {
        return dateFormat_.format(getWriteTime())
    }

}