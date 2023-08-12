package com.eitu.dolpan.viewModel.memberChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eitu.dolpan.dataClass.firestore.Chat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class MemberChat: ViewModel() {

    private val _chat = MutableStateFlow("")
    val chat = _chat.asStateFlow()
    val chatList : Flow<PagingData<Chat>> by lazy {
        _chat.flatMapLatest {
            Pager(PagingConfig(pageSize = 10)) {
                ChatPagingSource(Firebase.firestore, it)
            }.flow.cachedIn(viewModelScope)
        }
    }

    fun setOwner(owner : String) {
        _chat.value = owner
    }

}
