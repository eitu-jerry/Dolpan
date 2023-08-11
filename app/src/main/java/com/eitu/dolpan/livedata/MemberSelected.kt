package com.eitu.dolpan.livedata

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.eitu.dolpan.dataClass.YoutubeMember
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MemberSelected: ViewModel() {

    private val _member = MutableLiveData<YoutubeMember>()
    val member: LiveData<YoutubeMember>
        get() = _member

    init {
        //_member.value = YoutubeMember()
    }

    fun updateValue(member: YoutubeMember?) {
        member?.let { _member.value = it }

    }

}