package com.eitu.dolpan.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.eitu.dolpan.databinding.ActivityMainBinding
import com.eitu.dolpan.databinding.DialogYtPlBinding
import com.eitu.dolpan.dialog.DolpanDialog
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.network.repo.TwitchRepo
import com.eitu.dolpan.network.youtube.YoutubeRetrofit
import com.eitu.dolpan.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject lateinit var twitch : TwitchRepo

    private lateinit var binding:ActivityMainBinding

    override fun setBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
//        IntentHelper.intentDetail(this, Intent(this, ServerActivity::class.java))
//        IntentHelper.intentDetail(this, Intent(this, HomeActivity::class.java))
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                twitch.getChat("woowakgood")
                twitch.updateToken()
                twitch.checkLive("viichan6")
            }
        }

    }

}