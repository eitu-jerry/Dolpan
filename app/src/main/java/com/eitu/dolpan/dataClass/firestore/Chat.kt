package com.eitu.dolpan.dataClass.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.PropertyName

data class Chat(
    @PropertyName("owner")
    val owner: String = "",
    @PropertyName("type")
    val type: String = "",
    @PropertyName("id")
    val id: String = "",
    @PropertyName("title")
    val title: String = "",
    @PropertyName("date")
    val date: String = "",
) {

    companion object {

        fun toList(items: List<DocumentSnapshot>): ArrayList<Chat> {
            val list = ArrayList<Chat>()
            for (item in items) {
                item.toObject(Chat::class.java)?.let {
                    list.add(it)
                    Log.d("item", it.title)
                }
            }
            return list
        }
    }

}