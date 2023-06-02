package com.eitu.dolpan.view.activity

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
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
import com.eitu.dolpan.network.youtube.YoutubeRetrofit
import com.eitu.dolpan.view.base.BaseFragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : BaseFragmentActivity() {

    private lateinit var binding : ActivityHomeBinding

    private lateinit var clipboard: ClipboardManager
    private var checkClipboard = true
    private val calledClip = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DialogMemberSelected(this)

        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        init()
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
                        checkYtPlIsExist(playlistId)
                    }

                    override fun onNegative() {

                    }
                })
                .show()
        }

        initFragment()
    }

    private fun initFragment() {
        val adapter = AdapterFragment(this);
        binding.fragmentPager.isUserInputEnabled = false
        binding.fragmentPager.adapter = adapter
        val viewRecycler = binding.fragmentPager.getChildAt(0) as RecyclerView
        viewRecycler.setItemViewCacheSize(adapter.itemCount)

        addNewTab(binding.tabLayout.newTab(), "홈")
        addNewTab(binding.tabLayout.newTab(), "RE:WIND")
        addNewTab(binding.tabLayout.newTab(), "몰루")
        addNewTab(binding.tabLayout.newTab(), "몰루")
        addNewTab(binding.tabLayout.newTab(), "마이")
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.fragmentPager.setCurrentItem(binding.tabLayout.selectedTabPosition, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    private fun addNewTab(tab: TabLayout.Tab, text: String) {
        val _binding = TabHomeactBinding.inflate(layoutInflater)
        _binding.text.text = text
        _binding.image.setImageResource(R.drawable.img_rewind)
        tab.customView = _binding.root

        binding.tabLayout.addTab(tab)
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
                        showYtPlNoti(clipText.substring(clipText.indexOf("=") + 1))
                        calledClip.add(clipText)
                    }
                }
            }
        }
    }

    fun showYtPlNoti(playlistId: String) {
        var viewIsRemoved = false

        val _binding = NotiYtPlClipboadBinding.inflate(layoutInflater)
        _binding.root.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT)
        _binding.clipboard.text = playlistId
        _binding.btnGet.setOnClickListener {
            viewIsRemoved = true
            binding.root.removeView(_binding.root)
            checkYtPlIsExist(playlistId)
        }

        binding.root.addView(_binding.root)


        val showAnim = AnimationUtils.loadAnimation(activity, R.anim.anim_show_noti)
        showAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!viewIsRemoved) {
                        val hideAnim = AnimationUtils.loadAnimation(activity, R.anim.anim_hide_noti)
                        hideAnim.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(p0: Animation?) {

                            }

                            override fun onAnimationEnd(p0: Animation?) {
                                binding.root.removeView(_binding.root)
                            }

                            override fun onAnimationRepeat(p0: Animation?) {

                            }
                        })
                        _binding.root.startAnimation(hideAnim)
                    }
                }, 3000)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })

        _binding.root.startAnimation(showAnim)
    }

    fun checkYtPlIsExist(playlistId: String) {
        var playlistAlreadyExist = true
        fdb.collection("playlist")
            .whereEqualTo("playlistId", playlistId)
            .get()
            .addOnSuccessListener { result ->
                playlistAlreadyExist = result.size() > 0
            }

        if (playlistAlreadyExist) {
            Log.d("playlist", "already exist")
            val binding = DialogYtPlAlreadyBinding.inflate(activity.layoutInflater)
            DolpanDialog(activity)
                .viewBinding(binding)
                .buttons(binding.positive, null, null)
                .show()
        }
        else {
            youtube.getPlaylist(playlistId)
        }
    }

    private var isLoop = true
    override fun onStart() {
        super.onStart()
        isLoop = true
        CoroutineScope(Dispatchers.IO).launch {
//            while (isLoop) {
//                for (twitchId in resources.getStringArray(R.array.twitch)) {
//                    twitch.isLive(twitchId)
//                }
//                delay(2000)
//            }
        }
    }

    override fun onStop() {
        super.onStop()
        checkClipboard = true
        isLoop = false
    }
}