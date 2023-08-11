package com.eitu.dolpan.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eitu.dolpan.databinding.FragmentMyBinding
import com.eitu.dolpan.view.base.BaseFragment

class MyFragment: BaseFragment() {

    private lateinit var binding: FragmentMyBinding

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater): View {
        binding = FragmentMyBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {

    }

}