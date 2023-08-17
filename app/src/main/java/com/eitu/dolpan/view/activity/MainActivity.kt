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
//        IntentHelper.intentDetail(this, Intent(this, ServerActivity::class.java))
        IntentHelper.intentDetail(this, Intent(this, HomeActivity::class.java))
        finish()
//        lifecycleScope.launch(Dispatchers.IO) {
//            var socket : Socket? = null
//            try {
//                socket = IO.socket("https://www.youtube.com/feeds/videos.xml?channel_id=UCCA8UWUW80iHqK9ymdjRwPg")
//                socket.connect()
//                socket.on("af") {
//                    Log.d("asdf", it.toString())
//                }
//            } catch (e : Exception) {
//                e.printStackTrace()
//                if (socket != null && socket.isActive) {
//                    socket.disconnect()
//                    socket.off("af")
//                    socket.close()
//                }
//            }
//        }

    }

}