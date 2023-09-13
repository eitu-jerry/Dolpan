package com.eitu.dolpan.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.lifecycle.HiltViewModel

class HomeAct : ViewModel() {

    val page = MutableLiveData<Int>()

}