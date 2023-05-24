package com.eitu.dolpan.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eitu.dolpan.dataClass.viewpager.YoutubeChannel

class RewindBanners: ViewModel() {

    private val _banners = MutableLiveData<List<YoutubeChannel>>()
    val banners: LiveData<List<YoutubeChannel>>
        get() = _banners

    init {
        _banners.value = ArrayList()
    }

    fun updateValue(list: List<YoutubeChannel>) {
        Log.d("rewindBanner", "updateValue")
        _banners.value = list
    }

}