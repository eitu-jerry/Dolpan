package com.eitu.dolpan.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.eitu.dolpan.databinding.ActivityWebViewBinding
import com.eitu.dolpan.view.base.BaseActivity

class WebViewActivity : BaseActivity() {

    private val binding : ActivityWebViewBinding by lazy {
        ActivityWebViewBinding.inflate(layoutInflater)
    }

    override fun setBinding(): View? {
        return null
    }

    override fun init() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = WebViewClient()
            loadUrl(intent.getStringExtra("url") ?: "https://m.cafe.naver.com/steamindiegame")
        }
    }

    override fun callWhenBack() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        }
        else {
            super.callWhenBack()
        }
    }
}