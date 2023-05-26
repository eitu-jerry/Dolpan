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

class YoutubeMemberLiveData: ViewModel() {

    private val _members = MutableLiveData<List<YoutubeMember?>>()
    val members: LiveData<List<YoutubeMember?>>
        get() = _members

    init {
        _members.value = ArrayList()
    }

    fun updateValue(list: List<YoutubeMember?>) {
        _members.value = list
    }

    companion object {
        fun reset(activity: Activity) {
            Firebase.firestore
                .collection("youtubeMember")
                .get()
                .addOnSuccessListener {
                    val list = ArrayList<YoutubeMember>()
                    for (item in it.documents) {
                        item.toObject(YoutubeMember::class.java)?.let { it1 -> list.add(it1) }
                    }
                    val owner = activity as ViewModelStoreOwner
                    val members = ViewModelProvider(owner).get(YoutubeMemberLiveData::class.java)
                    members.updateValue(list)
                }
        }
    }

}