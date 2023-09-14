package com.eitu.dolpan.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.dataClass.firestore.YoutubeMember
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeItems @Inject constructor(val fdb : FirebaseFirestore) : ViewModel() {

    val date = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val list = mutableStateListOf<Chat>()
    val member = mutableStateListOf<YoutubeMember>()

    init {

        val dateFormat = SimpleDateFormat("M월 d일 EEEEEE요일", Locale.KOREA)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.KOREA)

        viewModelScope.launch {
            while (true) {
                date.value = dateFormat.format(System.currentTimeMillis())
                time.value = timeFormat.format(System.currentTimeMillis())
                delay(100)
            }
        }

        viewModelScope.launch {
            val result = fdb.collection("item")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .asDeferred()

            list.addAll(result.await().toObjects(Chat::class.java))
        }

        viewModelScope.launch {
            fdb.collection("youtubeMember")
                .whereEqualTo("isLive", true)
                .addSnapshotListener { value, error ->
                     value?.let {
                         val inRealTime = it.toObjects(YoutubeMember::class.java)

                         val toRemove = member.filter { !inRealTime.contains(it) }
                         val toAdd = inRealTime.filter { !member.contains(it) }

                         member.removeAll(toRemove)
                         member.addAll(toAdd)
                     }
                }
        }
    }

}