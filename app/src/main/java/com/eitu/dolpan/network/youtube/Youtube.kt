package com.eitu.dolpan.network.youtube

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.recycler.AdapterYtPl
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.dataClass.recycler.YoutubePlaylist
import com.eitu.dolpan.dataClass.viewpager.YoutubeChannel
import com.eitu.dolpan.databinding.DialogYtPlCheckBinding
import com.eitu.dolpan.dialog.DolpanDialog
import com.eitu.dolpan.livedata.YoutubeMemberLiveData
import retrofit2.Call
import retrofit2.Response
import com.eitu.dolpan.network.ResponseController
import com.eitu.dolpan.view.base.BaseViewInterface
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Youtube(activity: Activity) {

    private val activity: Activity
    private val sp: SharedPreferences
    private val fdb: FirebaseFirestore

    init {
        this.activity = activity
        sp = (activity as BaseViewInterface).sp
        fdb = (activity as BaseViewInterface).fdb
    }

    fun getPlaylist(call: Call<String>, playlistId: String) {
        call.enqueue(object : ResponseController<String>("getPlaylist", object : OnResponseListener<String> {
            override fun onSuccess(response: Response<String>) {
                Log.d("getPlaylist", response.body().toString())
                val binding = DialogYtPlCheckBinding.inflate(activity.layoutInflater)
                val adapter = AdapterYtPl()
                val list = YoutubePlaylist.toList(response.body())
                adapter.setList(list)

                binding.recyclerPlaylist.layoutManager = LinearLayoutManager(activity)
                binding.recyclerPlaylist.adapter = adapter

                if (list.size > 0) {
                    DolpanDialog(activity)
                        .viewBinding(binding)
                        .buttons(binding.positive, null, object : DolpanDialog.OnClickListener {
                            override fun onPositive() {
                                val db = Firebase.firestore

                                db.collection("playlist")
                                    .add(hashMapOf(
                                        "playlistId" to playlistId,
                                        "items" to YoutubePlaylist.toHashList(list)
                                    ))
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("getPlaylist", "DocumentSnapshot added with ID: ${documentReference.id}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("getPlaylist", "Error adding document", e)
                                    }

                            }

                            override fun onNegative() {

                            }
                        })
                        .show()
                }


            }

            override fun onFail(response: Response<String>) {

            }
        }){})
    }

    fun getChannels(call: Call<String>) {
        call.enqueue(object : ResponseController<String>("getChannels", object : OnResponseListener<String> {
            override fun onSuccess(response: Response<String>) {
                Log.d("getChannels", response.body().toString())
                val list = YoutubeChannel.toList(response.body())

                val hashList = YoutubeChannel.toHashList(list, activity)
                for (i in 0 until hashList.size) {
                    val item = hashList.get(i)
                    val docId = String.format("channel%02d", i)

                    fdb.collection("youtubeChannel")
                        .document(docId)
                        .set(item)
                        .addOnSuccessListener { documentReference ->
                            Log.d("getChannels", "DocumentSnapshot added with ID: $docId")
                        }
                        .addOnFailureListener { e ->
                            Log.w("getChannels", "Error adding document", e)
                        }
                }

                YoutubeMemberLiveData.reset(activity)
            }

            override fun onFail(response: Response<String>) {

            }
        }){})
    }

}