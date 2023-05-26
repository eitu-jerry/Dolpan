package com.eitu.dolpan.view.fragment

import com.eitu.dolpan.databinding.FragmentMyBinding
import com.eitu.dolpan.view.base.BaseFragment

class MyFragment: BaseFragment() {

    private val binding: FragmentMyBinding
        get() = FragmentMyBinding.inflate(layoutInflater)

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun init() {
        binding.resetChannel.setOnClickListener {
            youtube.getChannels()
        }
    }
}