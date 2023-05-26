package com.eitu.dolpan.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.databinding.FragmentMyBinding
import com.eitu.dolpan.view.base.BaseFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyFragment: BaseFragment() {

    private lateinit var binding: FragmentMyBinding

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {
        binding.resetChannel.setOnClickListener {
            youtube.getChannels()
        }
        binding.resetMember.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val owners = resources.getStringArray(R.array.member)
                val members = ArrayList<HashMap<String, Any?>?>()
                for (owner in owners) {
                    val member = YoutubeMember.newInstance(owner)
                    if (member != null) {
                        Firebase.firestore
                            .collection("youtubeMember")
                            .document(owner)
                            .set(member)
                            .await()
                    }
                }
            }
        }
    }
}