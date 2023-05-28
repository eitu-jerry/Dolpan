package com.eitu.dolpan.view.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.eitu.dolpan.network.twitch.TwitchRetrofit
import com.eitu.dolpan.network.youtube.YoutubeRetrofit

abstract class BaseFragmentActivity : FragmentActivity(), BaseViewInterface {

    override var activity: Activity
        get() = this
        set(value) {}
    override var TAG: String
        get() = this.javaClass.simpleName
        set(value) {}
    override var sp: SharedPreferences
        get() = getSharedPreferences("PRFS", MODE_PRIVATE)
        set(value) {}
    override var editor: SharedPreferences.Editor
        get() = sp.edit()
        set(value) {}
    override var youtube: YoutubeRetrofit
        get() = YoutubeRetrofit(this)
        set(value) {}
    override var twtich: TwitchRetrofit
        get() = TwitchRetrofit(this)
        set(value) {}

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

    override fun init() {
        TODO("Not yet implemented")
    }
}