package com.eitu.dolpan.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.databinding.ActivityServerBinding
import com.eitu.dolpan.databinding.FragmentMyBinding
import com.eitu.dolpan.view.base.BaseActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class ServerActivity: BaseActivity() {

    private lateinit var binding: ActivityServerBinding

    private val wakCafe = "Iep9BEdfIxd759MU7JgtSg"
    private val ineCafe = "Lp-S_8ZQuLCK03pDpod-7Q"
    private val jingCafe = "8g_F8kj48MSqBeVnVAhnCw"
    private val lilpaCafe = "ANjFuUREskKRC7DcGwAXNA"
    private val jururuCafe = "ri0vjEn1-XpglkwfwSDuBw"
    private val goseguCafe = "kvYmWvSHP9_wnnbRX4nGXg"
    private val vichanCafe = "6Wj7By3k4NnbeXohIaIltQ"
    private val memberArray = arrayOf(wakCafe, ineCafe, jingCafe, lilpaCafe, jururuCafe, goseguCafe, vichanCafe)
    private var memberIndex = 0
    private var currentMember: String = ""
    private val memberMap = hashMapOf(
        Pair(wakCafe, "wak"),
        Pair(ineCafe, "ine"),
        Pair(jingCafe, "jing"),
        Pair(lilpaCafe, "lilpa"),
        Pair(jururuCafe, "jururu"),
        Pair(goseguCafe, "gosegu"),
        Pair(vichanCafe, "vichan"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun init() {
        binding.resetChannel.setOnClickListener {
            youtube.getChannels()
        }
        binding.resetMember.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val owners = resources.getStringArray(R.array.member)
                for (owner in owners) {
                    val member = YoutubeMember.toHash(owner)
                    if (member != null) {
                        Firebase.firestore
                            .collection("youtubeMember")
                            .document(owner)
                            .set(member)
                            .await()
                    }
                }
            }
        }
        binding.getNaverAccessToken.setOnClickListener {
            NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
                override fun onError(errorCode: Int, message: String) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess() {
                    Log.d("accessToken", NaverIdLoginSDK.getAccessToken().toString())
                }
            })
        }

        checkTwitchisLive()
        getTwitchChat()
        crawlCafe()
    }

    private fun getTwitchChat() {
        for (member in resources.getStringArray(R.array.twitch)) {
            CoroutineScope(Dispatchers.IO).launch {
                while (true) {
                    twitch.getChat(member)
                    delay(300)
                }
            }
        }
    }

    private fun checkTwitchisLive() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                for (twitchId in resources.getStringArray(R.array.twitch)) {
                    twitch.isLive(twitchId)
                }
                delay(2000)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun crawlCafe() {
        currentMember = memberArray[memberIndex]
        val webView = binding.webViewWak
        webView.webViewClient = MyClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246"
        webView.addJavascriptInterface(MyJavascriptInterface(), "Android")
        webView.loadUrl("https://cafe.naver.com/ca-fe/cafes/27842958/members/$currentMember")

    }

    private inner class MyClient: WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            CookieManager.getInstance().flush()
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
            }

        }
    }

    inner class MyJavascriptInterface {
        @JavascriptInterface
        fun getHtml(html: String) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            CoroutineScope(Dispatchers.IO).launch {
                val logBuilder = java.lang.StringBuilder()
                val dateString_ = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date(System.currentTimeMillis())
                )
                val owner = memberMap[currentMember]
                logBuilder.append("getHtml$owner: $dateString_").append("\n")
                val fdb = Firebase.firestore
                val list = Jsoup.parse(html).select("div#app div div table tbody tr")
                if (list.isNotEmpty()) {
                    for (e in list) {
                        val articleId = e.select(".inner_number").text()
                        val href = e.select(".article").attr("href")
                        val title = e.select(".article").text()
                        var date = e.select(".td_date").text()

                        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(System.currentTimeMillis())
                        )
                        val timeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(System.currentTimeMillis()))
                        if (date.length == 11) {
                            date = date.substring(0, date.length - 1).replace(".","-") + " " + timeString
                        }
                        else {
                            date = "$dateString $date"
                        }

                        if (fdb.collection("item")
                                .whereEqualTo("id", articleId)
                                .get()
                                .await()
                                .isEmpty) {

                            val article = "articleId=$articleId href=$href title=$title date=$date"
                            logBuilder.append("addItem$owner: $article").append("\n")

                            fdb.collection("item")
                                .add(hashMapOf(
                                    Pair("owner", owner),
                                    Pair("type", "naverCafe"),
                                    Pair("id", articleId),
                                    Pair("title", title),
                                    Pair("date", date)
                                ))
                                .await()
                        }
                    }
                }
                else {
                    logBuilder.append("crawlFailed${memberMap[currentMember]}: $dateString_").append("\n")
                }
                launch(Dispatchers.Main) {
                    when(owner) {
                        "wak" -> binding.txtWak.text = logBuilder
                        "ine" -> binding.txtIne.text = logBuilder
                        "jing" -> binding.txtJing.text = logBuilder
                        "lilpa" -> binding.txtLilpa.text = logBuilder
                        "jururu" -> binding.txtJururu.text = logBuilder
                        "gosegu" -> binding.txtGosegu.text = logBuilder
                        "vichan" -> binding.txtVichan.text = logBuilder
                    }
                }
                memberIndex = (memberIndex + 1) % 7
                currentMember = memberArray[memberIndex]

                launch(Dispatchers.Main) {
                    binding.webViewWak.loadUrl("https://cafe.naver.com/ca-fe/cafes/27842958/members/$currentMember")
                }
            }
        }
    }

}