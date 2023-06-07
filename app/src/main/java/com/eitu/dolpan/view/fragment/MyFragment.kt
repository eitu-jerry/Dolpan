package com.eitu.dolpan.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.databinding.FragmentMyBinding
import com.eitu.dolpan.view.base.BaseFragment
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MyFragment: BaseFragment() {

    private lateinit var binding: FragmentMyBinding

    private val logBuilder = java.lang.StringBuilder()

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

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater)
        return binding.root
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
        binding.loadNaver.setOnClickListener {
            binding.webView.removeJavascriptInterface("Android")
            binding.webView.webViewClient = WebViewClient()
            binding.webView.loadUrl("https://m.naver.com")
        }
        binding.loadCrawl.setOnClickListener {
            crawlCafe()
        }
        crawlCafe()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun crawlCafe() {
        currentMember = memberArray[memberIndex]
        val webView = binding.webView
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
            CoroutineScope(Dispatchers.Main).launch {
                val dateString_ = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis()))
                logBuilder.append("getHtml${memberMap[currentMember]}: $dateString_").append("\n")
                val fdb = Firebase.firestore
                val list = Jsoup.parse(html).select("div#app div div table tbody tr")
                if (list.isNotEmpty()) {
                    for (e in list) {
                        val articleId = e.select(".inner_number").text()
                        val href = e.select(".article").attr("href")
                        val title = e.select(".article").text()
                        var date = e.select(".td_date").text()

                        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis()))
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
                            logBuilder.append("addItem${memberMap[currentMember]}: $article").append("\n")

                            fdb.collection("item")
                                .add(hashMapOf(
                                    Pair("owner", memberMap[currentMember]),
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
                binding.log.text = logBuilder
                memberIndex = (memberIndex + 1) % 7
                currentMember = memberArray[memberIndex]
                binding.webView.loadUrl("https://cafe.naver.com/ca-fe/cafes/27842958/members/$currentMember")
            }
        }
    }

}