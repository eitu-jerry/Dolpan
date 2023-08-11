package com.eitu.dolpan.viewModel.memberChat

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.eitu.dolpan.dataClass.firestore.Chat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MemberChat: ViewModel() {

//    @SuppressLint("MutableCollectionMutableState")
//    private val _chat = mutableStateListOf<Chat>()
//    val chat: SnapshotStateList<Chat>
//        get() = _chat

    private var owner : String = ""

    private val _chat = MutableStateFlow(owner)
    val chat = _chat.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val chatList = _chat.flatMapLatest {
        Pager(PagingConfig(pageSize = 1000)) {
            ChatPagingSource(Firebase.firestore, owner)
        }.flow.cachedIn(viewModelScope)
    }

    init {

    }

    fun getMemberChat(member : String) {

    }

    fun setOwner(owner : String) {
        this.owner = owner
    }

//    fun addList(list: ArrayList<Chat>) {
//        _chat.addAll(list.filterNot { _chat.contains(it) })
//        _chat.sortBy { it.date }
//    }
//
//    fun addItem(item: Chat) {
//        if (!_chat.contains(item)) _chat.add(item)
//    }

}
