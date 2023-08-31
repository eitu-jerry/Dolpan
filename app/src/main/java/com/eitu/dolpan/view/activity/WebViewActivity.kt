package com.eitu.dolpan.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Message
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
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
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportMultipleWindows(true)
                javaScriptCanOpenWindowsAutomatically = true
            }

            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {

                    resultMsg?.let {

                        val dialogWebView = WebView(this@WebViewActivity).apply {
                            settings.javaScriptEnabled = true
                        }

                        val dialog = Dialog(this@WebViewActivity)
                        dialog.setContentView(dialogWebView)
                        dialog.show()

                        (it.obj as WebView.WebViewTransport).webView = dialogWebView
                        it.sendToTarget()
                    }

                    return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                }
            }
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