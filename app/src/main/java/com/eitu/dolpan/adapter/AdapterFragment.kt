package com.eitu.dolpan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eitu.dolpan.view.fragment.HomeVer2Fragment
import com.eitu.dolpan.view.fragment.*

class AdapterFragment(activity: FragmentActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeVer2Fragment.newInstance()
            1 -> RewindFragment.newInstance()
            2 -> ArticleFragment.newInstance()
            3 -> BlankFragment.newInstance()
            else -> MyFragment.newInstance()
        }
    }
}