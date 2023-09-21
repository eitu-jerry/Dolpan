package com.eitu.dolpan.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eitu.dolpan.dataClass.firestore.YoutubeMember

class MemberSelected: ViewModel() {

    private val _member = MutableLiveData<YoutubeMember>()
    val member: LiveData<YoutubeMember>
        get() = _member

    fun updateValue(member: YoutubeMember?) {
        member?.let { _member.value = it }
    }



}