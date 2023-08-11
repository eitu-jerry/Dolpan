package com.eitu.dolpan.view.activity

import android.annotation.SuppressLint
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.AdapterFragment
import com.eitu.dolpan.databinding.ActivityHomeBinding
import com.eitu.dolpan.databinding.DialogYtPlAlreadyBinding
import com.eitu.dolpan.databinding.DialogYtPlBinding
import com.eitu.dolpan.databinding.NotiYtPlClipboadBinding
import com.eitu.dolpan.databinding.TabHomeactBinding
import com.eitu.dolpan.dialog.DialogMemberSelected
import com.eitu.dolpan.dialog.DolpanDialog
import com.eitu.dolpan.view.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeActivity : BaseActivity() {

    private lateinit var binding : ActivityHomeBinding

    private lateinit var clipboard: ClipboardManager
    private var checkClipboard = true
    private val calledClip = ArrayList<String>()

    private val adapterFragment by lazy {
        AdapterFragment(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DialogMemberSelected(this)
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun setBinding(): View? {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        binding.btnTest.setOnClickListener {
            val _binding = DialogYtPlBinding.inflate(layoutInflater)

            DolpanDialog(this)
                .viewBinding(_binding)
                .buttons(_binding.positive, null, object : DolpanDialog.OnClickListener {
                    override fun onPositive() {
                        var playlistId = _binding.playlistId.text.toString()
                        playlistId = playlistId.substring(playlistId.indexOf("=") + 1)
                        Toast.makeText(applicationContext, playlistId, Toast.LENGTH_SHORT).show()
//                        checkYtPlIsExist(playlistId)
                    }

                    override fun onNegative() {

                    }
                })
                .show()
        }

        initFragment()
    }

    private fun initFragment() {
        binding.fragmentPager.apply {
            isUserInputEnabled = false
            adapter = adapterFragment

            val viewRecycler = getChildAt(0) as RecyclerView
            viewRecycler.setItemViewCacheSize(adapterFragment.itemCount)
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.fragmentPager,
            false,
            false
        ) { tab, position ->
            tab.apply {
                when(position) {
                    0 -> {
                        text = "홈"
                    }
                    1 -> {
                        text = "RE:WIND"
                    }
                    2 -> {
                        text = "몰루"
                    }
                    3 -> {
                        text = "몰루"
                    }
                    4 -> {
                        text = "마이"
                    }
                }
            }
        }.attach()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (checkClipboard) {
            checkClipboard = false
            if (!clipboard.hasPrimaryClip()) {
                Log.d(TAG, "Clip is empty")
            }
            else if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == false) {
                Log.d(TAG, "Clip is not text")
            }
            else {
                val clipItem = clipboard.primaryClip?.getItemAt(0)
                if (clipItem != null) {
                    val clipText = clipItem.text.toString()
                    Log.d("clip", clipText)
                    if (clipText.startsWith("https://youtube.com/playlist?list=") && !calledClip.contains(clipText)) {
//                        showYtPlNoti(clipText.substring(clipText.indexOf("=") + 1))
                        calledClip.add(clipText)
                    }
                }
            }
        }
    }

//    fun showYtPlNoti(playlistId: String) {
//        var viewIsRemoved = false
//
//        val _binding = NotiYtPlClipboadBinding.inflate(layoutInflater)
//        _binding.root.layoutParams = ConstraintLayout.LayoutParams(
//            ConstraintLayout.LayoutParams.MATCH_PARENT,
//            ConstraintLayout.LayoutParams.WRAP_CONTENT)
//        _binding.clipboard.text = playlistId
//        _binding.btnGet.setOnClickListener {
//            viewIsRemoved = true
//            binding.root.removeView(_binding.root)
//            checkYtPlIsExist(playlistId)
//        }
//
//        binding.root.addView(_binding.root)
//
//
//        val showAnim = AnimationUtils.loadAnimation(activity, R.anim.anim_show_noti)
//        showAnim.setAnimationListener(object : AnimationListener {
//            override fun onAnimationStart(p0: Animation?) {
//
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    if (!viewIsRemoved) {
//                        val hideAnim = AnimationUtils.loadAnimation(activity, R.anim.anim_hide_noti)
//                        hideAnim.setAnimationListener(object : AnimationListener {
//                            override fun onAnimationStart(p0: Animation?) {
//
//                            }
//
//                            override fun onAnimationEnd(p0: Animation?) {
//                                binding.root.removeView(_binding.root)
//                            }
//
//                            override fun onAnimationRepeat(p0: Animation?) {
//
//                            }
//                        })
//                        _binding.root.startAnimation(hideAnim)
//                    }
//                }, 3000)
//            }
//
//            override fun onAnimationRepeat(p0: Animation?) {
//
//            }
//        })
//
//        _binding.root.startAnimation(showAnim)
//    }
//
//    fun checkYtPlIsExist(playlistId: String) {
//        var playlistAlreadyExist = true
//        fdb.collection("playlist")
//            .whereEqualTo("playlistId", playlistId)
//            .get()
//            .addOnSuccessListener { result ->
//                playlistAlreadyExist = result.size() > 0
//            }
//
//        if (playlistAlreadyExist) {
//            Log.d("playlist", "already exist")
//            val binding = DialogYtPlAlreadyBinding.inflate(activity.layoutInflater)
//            DolpanDialog(activity)
//                .viewBinding(binding)
//                .buttons(binding.positive, null, null)
//                .show()
//        }
//        else {
//            youtube.getPlaylist(playlistId)
//        }
//    }

    override fun onStop() {
        super.onStop()
        checkClipboard = true
    }
}