package com.eitu.dolpan.viewModel.memberChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eitu.dolpan.dataClass.firestore.Chat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class MemberChat: ViewModel() {

    private lateinit var owner : String

    val chatList : Flow<PagingData<Chat>> by lazy {
        Pager(PagingConfig(pageSize = 10, prefetchDistance = 30, initialLoadSize = 100, enablePlaceholders = false)) {
            ChatPagingSource(Firebase.firestore, owner)
        }.flow.cachedIn(viewModelScope)
    }

    fun setOwner(owner : String) {
        this.owner = owner

    }

}
