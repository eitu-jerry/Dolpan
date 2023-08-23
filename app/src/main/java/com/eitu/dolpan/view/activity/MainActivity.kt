package com.eitu.dolpan.view.activity

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.eitu.dolpan.databinding.ActivityMainBinding
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.view.base.BaseActivity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.IOCase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun setBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        binding.home.setOnClickListener {
            IntentHelper.intentDetail(this, Intent(this, HomeActivity::class.java))
        }
        binding.server.setOnClickListener {
            IntentHelper.intentDetail(this, Intent(this, ServerActivity::class.java))
        }

    }

}