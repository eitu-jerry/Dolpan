package com.eitu.dolpan.view.fragment

import android.view.LayoutInflater
import android.view.View
import com.eitu.dolpan.databinding.FragmentRewindBinding
import com.eitu.dolpan.view.base.BaseFragment

class RewindFragment: BaseFragment() {

    private lateinit var binding: FragmentRewindBinding

    companion object {
        fun newInstance(): RewindFragment {
            return RewindFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater): View {
        binding = FragmentRewindBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {

    }

}