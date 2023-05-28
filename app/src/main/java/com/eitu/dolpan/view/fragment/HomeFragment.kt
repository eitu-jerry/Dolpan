package com.eitu.dolpan.view.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.recycler.AdapterHomeMember
import com.eitu.dolpan.databinding.FragmentHomeBinding
import com.eitu.dolpan.livedata.MemberSelected
import com.eitu.dolpan.livedata.YoutubeMemberLiveData
import com.eitu.dolpan.view.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment: BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var members: YoutubeMemberLiveData

    private lateinit var adapterAll: AdapterHomeMember

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {
        initMemberAll()
        setMemberLiveData()
    }

    private fun initMemberAll() {
        adapterAll = AdapterHomeMember(activity, ViewModelProvider(activity as ViewModelStoreOwner)[MemberSelected::class.java])
        binding.recyclerAllMember.layoutManager = LinearLayoutManager(activity)
        binding.recyclerAllMember.adapter = adapterAll
        binding.recyclerAllMember.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val padding = resources.getDimensionPixelOffset(R.dimen.homeMemberPadding)
                outRect.top = padding
            }
        })
    }

    private fun setMemberLiveData() {
        val modelOwner = activity as ViewModelStoreOwner
        members = ViewModelProvider(modelOwner)[YoutubeMemberLiveData::class.java]
        members.members.observe(activity as LifecycleOwner) {
            Log.d("reset", SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis())))
            adapterAll.setList(it)
        }
        YoutubeMemberLiveData.reset(activity)
    }

    private var isLoop = true
    override fun onStart() {
        super.onStart()
//        CoroutineScope(Dispatchers.IO).launch {
//            while (isLoop) {
//                for (twitchId in resources.getStringArray(R.array.twitch)) {
//                    twtich.isLive(twitchId)
//                }
//                delay(2000)
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoop = false
    }
}