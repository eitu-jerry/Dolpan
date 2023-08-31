package com.eitu.dolpan.view.dialog

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.eitu.dolpan.databinding.DialogMemberSelectedBinding
import com.eitu.dolpan.databinding.DialogWriteArticleBinding
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.viewModel.MemberSelected
import com.eitu.dolpan.view.activity.ChatActivity
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.viewModel.WriteArticle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
class DialogWriteArticle(activity: BaseActivity, private val writeArticle: WriteArticle) {

    private val bottomSheetDialog : BottomSheetDialog by lazy {
        BottomSheetDialog(activity).apply {
            setCancelable(false)
        }
    }

    private val binding : DialogWriteArticleBinding by lazy {
        DialogWriteArticleBinding.inflate(activity.layoutInflater)
    }

    init {
        binding.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            height = (activity.resources.displayMetrics.heightPixels * 0.9).toInt()
        }

        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("urlLoading", "request?.url?.path")
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    Log.d("intercept", "${request?.url?.path} ${request?.isRedirect}")
                    request?.url?.path?.let {
                        if (it == "/web-mobile/js/chunk-2d20732d.5f5baf1026b0.js") {
                            bottomSheetDialog.dismiss()
                        }
                        else if (it.contains("/cafe-web/cafe-articleapi/v2.1/cafes/27842958/articles/")) {
                            bottomSheetDialog.dismiss()
                            val articleId = it.substringAfter("/cafe-web/cafe-articleapi/v2.1/cafes/27842958/articles/")
                            val goTo = "https://m.cafe.naver.com/ca-fe/web/cafes/steamindiegame/articles/${articleId}?useCafeId=false"
                            IntentHelper.goToWebView(activity, goTo)
                        }
                    }
                    return super.shouldInterceptRequest(view, request)
                }
            }
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.d("webViewScroll", "scrollY : $scrollY")
                if (scrollY != 0) {
                    bottomSheetDialog.setCancelable(false)
                }
                else {
                    bottomSheetDialog.setCancelable(true)
                }
            }
        }

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.peekHeight = activity.resources.displayMetrics.heightPixels
        bottomSheetDialog.setOnDismissListener {
            writeArticle.dismiss()
        }
    }

    fun show(url : String) {
        binding.webView.loadUrl(url)
        bottomSheetDialog.show()
    }

}