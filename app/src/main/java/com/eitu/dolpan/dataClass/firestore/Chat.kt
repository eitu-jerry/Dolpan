package com.eitu.dolpan.dataClass.firestore

import com.google.firebase.firestore.PropertyName
import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    @PropertyName("owner")
    val owner: String = "",
    @PropertyName("type")
    val type: String = "",
    @PropertyName("date")
    val date: Long = -1L,
    @PropertyName("title")
    val title: String = "",
    @PropertyName("id")
    val id: String? = null,
    @PropertyName("sendFrom")
    val sendFrom: String? = null,
) {

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val fromFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        private val toFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEEEE요일", Locale.getDefault())
    }

    fun getFormattedTime() : String {
        var timeText = fromFormat.format(date)

        try {
            val hour = timeText.split(":")[0].toInt()
            val minute = timeText.split(":")[1]
            if (hour > 12) {
                timeText = "오후 ${hour-12}:$minute"
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            timeText = ""
        }

        return timeText
    }

    fun getFormattedDate() : String? {
        return toFormat.format(date)
    }

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

        val timeDifference = thisTime - date

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
                dateFormat.format(date)
            }
        }
    }

}