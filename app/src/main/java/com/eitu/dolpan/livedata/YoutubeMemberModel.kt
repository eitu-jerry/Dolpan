package com.eitu.dolpan.livedata

import androidx.lifecycle.*
import com.eitu.dolpan.dataClass.YoutubeMember
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class YoutubeMemberModel: ViewModel() {

    private val _members = MutableLiveData<List<YoutubeMember>>(emptyList())
    val members: LiveData<List<YoutubeMember>>
        get() = _members

    private val fdb : FirebaseFirestore = Firebase.firestore

    init {
        refreshMembers()
    }

    private fun refreshMembers() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                fdb.collection("youtubeMember").get().await()
            }

            val list = ArrayList<YoutubeMember>()
            result.documents.forEach {
                it.toObject(YoutubeMember::class.java)?.let { member ->
                    list.add(member)
                }
            }

            _members.value = list
        }
    }

}