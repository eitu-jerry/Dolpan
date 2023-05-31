package com.eitu.dolpan.view.base

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.eitu.dolpan.network.twitch.TwitchRetrofit
import com.eitu.dolpan.network.youtube.YoutubeRetrofit

abstract class BaseFragment: Fragment(), BaseViewInterface {

    override var activity: Activity
        get() = requireActivity()
        set(value) {}
    override var TAG: String
        get() = javaClass.simpleName
        set(value) {}
    override var sp: SharedPreferences
        get() = (activity as BaseViewInterface).sp
        set(value) {}
    override var editor: SharedPreferences.Editor
        get() = sp.edit()
        set(value) {}
    override var youtube: YoutubeRetrofit
        get() = (activity as BaseViewInterface).youtube
        set(value) {}
    override var twitch: TwitchRetrofit
        get() = (activity as BaseViewInterface).twitch
        set(value) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}