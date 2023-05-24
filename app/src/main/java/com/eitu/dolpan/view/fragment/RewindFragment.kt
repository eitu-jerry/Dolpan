package com.eitu.dolpan.view.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.viewpager.AdapterRewindTop
import com.eitu.dolpan.dataClass.viewpager.YoutubeChannel
import com.eitu.dolpan.databinding.FragmentRewindBinding
import com.eitu.dolpan.view.base.BaseFragment

class RewindFragment: BaseFragment() {

    private lateinit var binding: FragmentRewindBinding

    private val adapterBanner = AdapterRewindTop()

    //banner auto scroll
    private val handler = Handler(Looper.getMainLooper())
    private val r: Runnable
        get() = object : Runnable {
            override fun run() {
                binding.bannerPager.currentItem = binding.bannerPager.currentItem + 1
                handler.postDelayed(this, autoScrollSpeed.toLong())
            }
        }
    private val autoScrollSpeed = 5500

    companion object {
        fun newInstance(): RewindFragment {
            return RewindFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRewindBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {
        initTop()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initTop() {
        val dm = resources.displayMetrics
        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, dm)
        val side = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45f, dm)
        binding.bannerPager.offscreenPageLimit = 3
        binding.bannerPager.adapter = adapterBanner
        binding.bannerPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeTop()
            }
        })
        binding.bannerPager.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = padding.toInt()
                outRect.right = padding.toInt()
            }
        })
        binding.bannerPager.setPageTransformer { page, position ->
            page.translationX = position * -side
//            if (position != 0f) {
//                val scale = Math.max(0.9f, 1 - Math.abs(position) * 0.1f)
//                page.scaleX = scale
//                page.scaleY = scale
//                val alpha = Math.max(0.9f, 1 - Math.abs(position) * 0.1f)
//                page.alpha = alpha
//            } else {
//                page.scaleX = 1f
//                page.scaleY = 1f
//            }
        }

        val bannerHeight = binding.bannerPager.measuredHeight
        binding.bannerPager.translationY = (-(bannerHeight / 4)).toFloat()

        val recyclerInner = binding.bannerPager.getChildAt(0)
        recyclerInner.setOnTouchListener { view: View?, e: MotionEvent ->
            if (e.action == MotionEvent.ACTION_DOWN) {
                cancelHandler()
            }
            else if (e.action == MotionEvent.ACTION_UP) {
                startHandler()
            }
            false
        }
    }

    fun changeTop() {
        val list = adapterBanner.getList()
        if (list.isNotEmpty()) {
            val position = binding.bannerPager.currentItem % list.size
            val item = list.get(position)

            binding.name.text = item.title

            val customUrl = item.customUrl
            if (customUrl == null || customUrl == "") binding.customUrl.text = resources.getString(R.string.noChannelCustomUrl)
            else binding.customUrl.text = customUrl

            val description = item.description
            if (description == null || description == "") binding.description.text = resources.getString(R.string.noChannelDesc)
            else binding.description.text = description

            colorAnimation(position)
        }
        else Log.d("adapterBanner", "list is empty")
    }

    private fun colorAnimation(position: Int) {
        val beforeColor: Int
        val afterColor: Int

        val background = binding.layoutTopInfo.background
        if (background is ColorDrawable) beforeColor = background.color
        else beforeColor = Color.TRANSPARENT

        val id = adapterBanner.getList().get(position).id
        if (resources.getStringArray(R.array.wak).contains(id)) afterColor = activity.getColor(R.color.wak)
        else if (resources.getStringArray(R.array.ine).contains(id)) afterColor = activity.getColor(R.color.ine)
        else if (resources.getStringArray(R.array.jing).contains(id)) afterColor = activity.getColor(R.color.jing)
        else if (resources.getStringArray(R.array.lilpa).contains(id)) afterColor = activity.getColor(R.color.lilpa)
        else if (resources.getStringArray(R.array.jururu).contains(id)) afterColor = activity.getColor(R.color.jururu)
        else if (resources.getStringArray(R.array.gosegu).contains(id)) afterColor = activity.getColor(R.color.gosegu)
        else if (resources.getStringArray(R.array.vichan).contains(id)) afterColor = activity.getColor(R.color.vichan)
        else afterColor = activity.getColor(R.color.gray)

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), beforeColor, afterColor)
        colorAnimation.duration = 500
        colorAnimation.addUpdateListener { animator: ValueAnimator ->
            binding.layoutTopInfo.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        colorAnimation.start()
    }

    private fun startHandler() {
        if (adapterBanner.getList().isNotEmpty()) handler.postDelayed(r, autoScrollSpeed.toLong())
    }

    private fun cancelHandler() {
        handler.removeCallbacks(r)
        handler.removeCallbacksAndMessages(null)
    }

    private fun getChannels() {
        if (adapterBanner.getList().isEmpty()) {
            fdb.collection("youtubeChannel")
                .whereEqualTo("type", "main")
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        adapterBanner.setList(YoutubeChannel.toList(it.documents))
                        binding.bannerPager.setCurrentItem(adapterBanner.getList().size * 10000000, false)
                        changeTop()
                        startHandler()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        //youtube.getChannels()
        getChannels()
    }

    override fun onResume() {
        super.onResume()
        if (adapterBanner.getList().isNotEmpty()) startHandler()
    }

    override fun onPause() {
        super.onPause()
        cancelHandler()
    }
}