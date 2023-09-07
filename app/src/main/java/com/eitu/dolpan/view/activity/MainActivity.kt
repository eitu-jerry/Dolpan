package com.eitu.dolpan.view.activity

import android.content.Intent
import android.view.View
import com.eitu.dolpan.databinding.ActivityMainBinding
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.view.base.BaseActivity

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
        binding.manageItem.setOnClickListener {
            IntentHelper.intentDetail(this, Intent(this, ManageItemActivity::class.java))
        }
//        lifecycleScope.launch {
//            var socket : Socket? = null
//            try {
//                socket = IO.socket("https://www.youtube.com/feeds/videos.xml?channel_id=UCBkyj16n2snkRg1BAzpovXQ")
//                socket.connect()
//                socket.on(Socket.EVENT_CONNECT) {
//                    Log.d("Socket", it.toString())
//                }
//            } catch (e : Exception) {
//                e.printStackTrace()
//                if (socket != null) {
//                    socket.disconnect()
//                    socket.close()
//                }
//            }
//        }
    }

}