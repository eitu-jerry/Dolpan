package com.eitu.dolpan.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.eitu.dolpan.databinding.ActivityMainBinding
import com.eitu.dolpan.databinding.DialogYtPlBinding
import com.eitu.dolpan.dialog.DolpanDialog
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.network.youtube.YoutubeRetrofit
import com.eitu.dolpan.view.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun init() {
//        IntentHelper.intentDetail(this, Intent(this, ServerActivity::class.java))
        IntentHelper.intentDetail(this, Intent(this, HomeActivity::class.java))
    }

}