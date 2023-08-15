package com.eitu.dolpan.dataClass.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.PropertyName
import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    @PropertyName("owner")
    val owner: String = "",
    @PropertyName("type")
    val type: String = "",
    @PropertyName("date")
    val date: String = "",
    @PropertyName("title")
    val title: String = "",
    @PropertyName("id")
    val id: String? = null,
    @PropertyName("sendFrom")
    val sendFrom: String? = null,
) {

    private val fromFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val toFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEEEE요일", Locale.getDefault())

    fun getFormattedTime() : String {
        var timeText : String

        try {
            timeText = date.split(" ")[1]
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
        return fromFormat.parse(date)?.let { toFormat.format(it) }
    }

}