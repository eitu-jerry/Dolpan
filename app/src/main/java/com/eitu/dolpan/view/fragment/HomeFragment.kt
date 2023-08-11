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
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
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

    private val adapterAll: AdapterHomeMember by lazy {
        AdapterHomeMember(requireActivity())
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {
        initMemberAll()
    }

    private fun initMemberAll() {
        binding.recyclerAllMember.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterAll
            addItemDecoration(MyItemDecoration())

            val animator = itemAnimator as SimpleItemAnimator
            animator.supportsChangeAnimations = false
        }
    }

    inner class MyItemDecoration : ItemDecoration() {
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
    }

}