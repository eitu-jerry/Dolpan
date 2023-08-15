package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.eitu.dolpan.databinding.ActivityServerBinding
import com.eitu.dolpan.network.repo.NaverCafeRepo
import com.eitu.dolpan.network.repo.TwitchRepo
import com.eitu.dolpan.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ServerActivity: BaseActivity() {

    @Inject lateinit var twitch : TwitchRepo
    @Inject lateinit var naver : NaverCafeRepo

    private lateinit var binding: ActivityServerBinding

    private val wakCafe = "Iep9BEdfIxd759MU7JgtSg"
    private val ineCafe = "Lp-S_8ZQuLCK03pDpod-7Q"
    private val jingCafe = "8g_F8kj48MSqBeVnVAhnCw"
    private val lilpaCafe = "ANjFuUREskKRC7DcGwAXNA"
    private val jururuCafe = "ri0vjEn1-XpglkwfwSDuBw"
    private val goseguCafe = "kvYmWvSHP9_wnnbRX4nGXg"
    private val vichanCafe = "6Wj7By3k4NnbeXohIaIltQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setBinding(): View {
        binding = ActivityServerBinding.inflate(layoutInflater)
        return binding.root
    }

    private val dateFormat : SimpleDateFormat
        get() = SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa", Locale.ENGLISH)
    private val dateFormat_ : SimpleDateFormat
        get() = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)

    override fun init() {
        Log.d("currentTime", System.currentTimeMillis().toString())

        getChat("woowakgood")
        getChat("vo_ine")
        getChat("jingburger")
        getChat("lilpaaaaaa")
        getChat("cotton__123")
        getChat("gosegugosegu")
        getChat("viichan6")

        getArticle("Iep9BEdfIxd759MU7JgtSg")
        getArticle("Lp-S_8ZQuLCK03pDpod-7Q")
        getArticle("8g_F8kj48MSqBeVnVAhnCw")
        getArticle("ANjFuUREskKRC7DcGwAXNA")
        getArticle("ri0vjEn1-XpglkwfwSDuBw")
        getArticle("kvYmWvSHP9_wnnbRX4nGXg")
        getArticle("6Wj7By3k4NnbeXohIaIltQ")
    }

    fun getChat(channelId : String) {
        CoroutineScope(Dispatchers.IO).launch {
            var lastMessageTime : Long? = null
            while (true) {
                lastMessageTime = twitch.getChat(channelId, lastMessageTime)
                delay(100)
            }
        }
    }

    fun getArticle(memberKey : String) {
        CoroutineScope(Dispatchers.IO).launch {
            var lastArticleTime : Long = System.currentTimeMillis()
            while (true) {
                lastArticleTime = naver.getMemberArticles(memberKey, lastArticleTime)
                delay(30000)
            }
//            naver.getMemberArticles(memberKey)
        }
    }

}