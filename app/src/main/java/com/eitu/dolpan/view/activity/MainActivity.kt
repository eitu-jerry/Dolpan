package com.eitu.dolpan.view.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.eitu.dolpan.databinding.ActivityMainBinding
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.view.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun setBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
//        IntentHelper.intentDetail(this, Intent(this, ServerActivity::class.java))
        IntentHelper.intentDetail(this, Intent(this, HomeActivity::class.java))
        finish()
    }

}