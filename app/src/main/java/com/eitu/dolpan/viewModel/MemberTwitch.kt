package com.eitu.dolpan.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eitu.dolpan.dataClass.firestore.YoutubeMember
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberTwitch @Inject constructor(private val fdb : FirebaseFirestore) : ViewModel() {

    private val _member = MutableLiveData<YoutubeMember>()

    @Composable
    fun memberState() : State<YoutubeMember?> {
        val state = _member.observeAsState()
        return remember { state }
    }

    fun setOwner(owner : String) {
        fdb.collection("youtubeMember")
            .document("owner")
            .addSnapshotListener { value, error ->
                if (error == null && value != null) {
                    _member.value = value.toObject(YoutubeMember::class.java)
                }
                else {
                    error?.printStackTrace()
                }
            }
    }

}