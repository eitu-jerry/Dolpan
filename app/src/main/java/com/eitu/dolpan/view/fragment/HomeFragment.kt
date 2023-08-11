package com.eitu.dolpan.view.fragment

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.recycler.AdapterHomeMember
import com.eitu.dolpan.databinding.FragmentHomeBinding
import com.eitu.dolpan.livedata.MemberSelected
import com.eitu.dolpan.livedata.YoutubeMemberModel
import com.eitu.dolpan.view.base.BaseFragment

class HomeFragment: BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    private val ytMember: YoutubeMemberModel by viewModels()
    private val memberSelected : MemberSelected by activityViewModels()

    private val adapterAll: AdapterHomeMember by lazy {
        AdapterHomeMember(baseActivity, memberSelected, ytMember)
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