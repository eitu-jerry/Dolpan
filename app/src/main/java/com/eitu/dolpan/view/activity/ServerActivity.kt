package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.eitu.dolpan.databinding.ActivityServerBinding
import com.eitu.dolpan.network.api.*
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

    override fun setBinding(): View {
        binding = ActivityServerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {

        getChat("woowakgood")
        getChat("vo_ine")
        getChat("jingburger")
        getChat("lilpaaaaaa")
        getChat("cotton__123")
        getChat("gosegugosegu")
        getChat("viichan6")

        getArticle(wakCafe)
        getArticle(ineCafe)
        getArticle(jingCafe)
        getArticle(lilpaCafe)
        getArticle(jururuCafe)
        getArticle(goseguCafe)
        getArticle(vichanCafe)
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
        }
    }

}