package com.eitu.dolpan.view.activity

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.eitu.dolpan.adapter.AdapterFragment
import com.eitu.dolpan.databinding.ActivityHomeBinding
import com.eitu.dolpan.view.dialog.DialogMemberSelected
import com.eitu.dolpan.viewModel.MemberSelected
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.viewModel.HomeAct
import com.eitu.dolpan.viewModel.WriteArticle
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding : ActivityHomeBinding

    private var checkClipboard = true
    private val calledClip = ArrayList<String>()
    private val clipboard: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private val adapterFragment by lazy {
        AdapterFragment(this)
    }

    private val memberSelected : MemberSelected by viewModels()
    private val writeArticle : WriteArticle by viewModels()
    private val homeAct : HomeAct by viewModels()

    override fun callWhenBack() {
        if (binding.fragmentPager.currentItem != 0) {
            binding.fragmentPager.setCurrentItem(0, true)
        }
        else {
            super.callWhenBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DialogMemberSelected(this, memberSelected)
        writeArticle.setDialog(this@HomeActivity)
    }

    override fun setBinding(): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        initFragment()
    }

    private fun initFragment() {
        binding.fragmentPager.apply {
            isUserInputEnabled = true
            adapter = adapterFragment

            val viewRecycler = getChildAt(0) as RecyclerView
            viewRecycler.setItemViewCacheSize(adapterFragment.itemCount)

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d("pageSelected", "page $position")
                }
            })
        }

        homeAct.page.observe(this) {
            Toast.makeText(this, "page $it", Toast.LENGTH_SHORT).show()
            binding.fragmentPager.setCurrentItem(it, false)
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.fragmentPager,
            false,
            true
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
                        text = "게시판"
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