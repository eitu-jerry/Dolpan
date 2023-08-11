package com.eitu.dolpan.view.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.eitu.dolpan.databinding.FragmentMyBinding
import com.eitu.dolpan.network.repo.YoutubeRepo
import com.eitu.dolpan.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MyFragment: BaseFragment() {

    private lateinit var binding: FragmentMyBinding

    @Inject lateinit var youtube : YoutubeRepo

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
        binding.updateMembers.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    youtube.updateChannels()
                }
            }
        }
    }

}