package com.eitu.dolpan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eitu.dolpan.view.fragment.HomeFragment
import com.eitu.dolpan.view.fragment.MyFragment
import com.eitu.dolpan.view.fragment.RewindFragment

class AdapterFragment(activity: FragmentActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment.newInstance()
            1 -> RewindFragment.newInstance()
            2 -> HomeFragment.newInstance()
            3 -> HomeFragment.newInstance()
            else -> MyFragment.newInstance()
        }
//        return MyFragment.newInstance()
    }
}