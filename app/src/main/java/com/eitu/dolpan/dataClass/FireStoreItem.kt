package com.eitu.dolpan.dataClass

import android.util.Log
import com.eitu.dolpan.adapter.recycler.AdapterChatByDate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log

data class FireStoreItem(
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
        fun setList(owner: String, adapter: AdapterChatByDate) {
            Firebase.firestore.collection("item")
                .whereEqualTo("owner", owner)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        val list = ArrayList<FireStoreItem>()
                        for (item in it.documents) {
                            item.toObject(FireStoreItem::class.java)?.let { list.add(it) }
                        }
                        adapter.setList(list)
                    }
                }
        }

        fun toList(items: List<DocumentSnapshot>): ArrayList<FireStoreItem> {
            val list = ArrayList<FireStoreItem>()
            for (item in items) {
                item.toObject(FireStoreItem::class.java)?.let {
                    list.add(it)
                    Log.d("item", it.title)
                }
            }
            return list
        }
    }

}