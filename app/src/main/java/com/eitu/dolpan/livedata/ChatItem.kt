package com.eitu.dolpan.livedata

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eitu.dolpan.dataClass.FireStoreItem
import com.eitu.dolpan.dataClass.YoutubeMember

class ChatItem: ViewModel() {

    @SuppressLint("MutableCollectionMutableState")
    private val _chat = mutableStateListOf<FireStoreItem>()
    val chat: SnapshotStateList<FireStoreItem>
        get() = _chat

    init {

    }

    fun addList(list: ArrayList<FireStoreItem>) {
        _chat.addAll(list.filterNot { _chat.contains(it) })
        _chat.sortBy { it.date }
    }

    fun addItem(item: FireStoreItem) {
        if (!_chat.contains(item)) _chat.add(item)
    }

}
