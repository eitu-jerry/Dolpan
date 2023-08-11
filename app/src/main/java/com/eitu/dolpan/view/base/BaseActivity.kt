package com.eitu.dolpan.view.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.network.twitch.TwitchRetrofit
import com.eitu.dolpan.network.youtube.YoutubeRetrofit

abstract class BaseActivity : AppCompatActivity(), BaseViewInterface {

    override val TAG: String
        get() = this.javaClass.simpleName

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setBinding()?.let {
            setContentView(it)
            init()
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onBackPressedDispatcher.addCallback(OnBackPressed())
    }

    private inner class OnBackPressed : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callWhenBack()
        }
    }

    open fun callWhenBack() {
        finish()
        IntentHelper.outDetailAnim(this)
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

    abstract fun setBinding() : View?
    abstract fun init()

}