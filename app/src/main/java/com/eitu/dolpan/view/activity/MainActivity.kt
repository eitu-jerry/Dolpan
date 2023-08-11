package com.eitu.dolpan.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.eitu.dolpan.databinding.ActivityMainBinding
import com.eitu.dolpan.databinding.DialogYtPlBinding
import com.eitu.dolpan.view.dialog.DolpanDialog
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.network.repo.TwitchRepo
import com.eitu.dolpan.network.youtube.YoutubeRetrofit
import com.eitu.dolpan.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun setBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        IntentHelper.intentDetail(this, Intent(this, HomeActivity::class.java))
        finish()
    }

}