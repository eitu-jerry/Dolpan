package com.eitu.dolpan.view.fragment

import android.view.LayoutInflater
import android.view.View
import com.eitu.dolpan.databinding.FragmentRewindBinding
import com.eitu.dolpan.view.base.BaseFragment

class RewindFragment: BaseFragment() {

    private lateinit var binding: FragmentRewindBinding

    companion object {
        fun newInstance(): RewindFragment {
            return RewindFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater): View {
        binding = FragmentRewindBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {

    }

//    private lateinit var members: YoutubeMemberLiveData
//
//    private val adapterBanner = AdapterRewindTop()
//    private lateinit var adapterCustomUrl: AdapterCustomUrl
//
//    //banner auto scroll
//    private val handler = Handler(Looper.getMainLooper())
//    private val r: Runnable
//        get() = object : Runnable {
//            override fun run() {
//                binding.bannerPager.currentItem = binding.bannerPager.currentItem + 1
//                handler.postDelayed(this, autoScrollSpeed.toLong())
//            }
//        }
//    private val autoScrollSpeed = 5500
//
//    companion object {
//        fun newInstance(): RewindFragment {
//            return RewindFragment()
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentRewindBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun init() {
//        initTop()
//        setMemberLiveData()
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    fun initTop() {
//        val dm = resources.displayMetrics
//        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, dm)
//        val side = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45f, dm)
//        binding.bannerPager.offscreenPageLimit = 3
//        binding.bannerPager.adapter = adapterBanner
//        binding.bannerPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                changeTop()
//            }
//        })
//        binding.bannerPager.addItemDecoration(object : ItemDecoration() {
//            override fun getItemOffsets(
//                outRect: Rect,
//                view: View,
//                parent: RecyclerView,
//                state: RecyclerView.State
//            ) {
//                super.getItemOffsets(outRect, view, parent, state)
//                outRect.left = padding.toInt()
//                outRect.right = padding.toInt()
//            }
//        })
//        binding.bannerPager.setPageTransformer { page, position ->
//            page.translationX = position * -side
////            if (position != 0f) {
////                val scale = Math.max(0.9f, 1 - Math.abs(position) * 0.1f)
////                page.scaleX = scale
////                page.scaleY = scale
////                val alpha = Math.max(0.9f, 1 - Math.abs(position) * 0.1f)
////                page.alpha = alpha
////            } else {
////                page.scaleX = 1f
////                page.scaleY = 1f
////            }
//        }
//
//        val bannerHeight = binding.bannerPager.measuredHeight
//        binding.bannerPager.translationY = (-(bannerHeight / 4)).toFloat()
//
//        val recyclerInner = binding.bannerPager.getChildAt(0)
//        recyclerInner.setOnTouchListener { view: View?, e: MotionEvent ->
//            if (e.action == MotionEvent.ACTION_DOWN) {
//                cancelHandler()
//            }
//            else if (e.action == MotionEvent.ACTION_UP) {
//                startHandler()
//            }
//            false
//        }
//
//        adapterCustomUrl = AdapterCustomUrl(activity)
//        binding.customUrls.layoutManager = LinearLayoutManager(activity)
//        binding.customUrls.adapter = adapterCustomUrl
//        binding.customUrls.addItemDecoration(object : ItemDecoration() {
//            override fun getItemOffsets(
//                outRect: Rect,
//                view: View,
//                parent: RecyclerView,
//                state: RecyclerView.State
//            ) {
//                super.getItemOffsets(outRect, view, parent, state)
//
//                val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
//                outRect.top = padding.toInt()
//            }
//        })
//    }
//
//    fun changeTop() {
//        val list = adapterBanner.getList()
//        if (list.isNotEmpty()) {
//            val position = binding.bannerPager.currentItem % list.size
//            val item = list.get(position)
//
//            binding.name.text = item?.name?.uppercase()
//            item?.channel?.let { adapterCustomUrl.setCustomUrls(it) }
//            colorAnimation(position)
//        }
//        else Log.d("adapterBanner", "list is empty")
//    }
//
//    private fun colorAnimation(position: Int) {
//        val beforeColor: Int
//        val afterColor: Int
//
//        val background = binding.layoutTopInfo.background
//        if (background is ColorDrawable) beforeColor = background.color
//        else beforeColor = Color.TRANSPARENT
//
//        val owner = adapterBanner.getList().get(position)?.owner
//        if (owner == "wak") afterColor = activity.getColor(R.color.wak)
//        else if (owner == "ine") afterColor = activity.getColor(R.color.ine)
//        else if (owner == "jing") afterColor = activity.getColor(R.color.jing)
//        else if (owner == "lilpa") afterColor = activity.getColor(R.color.lilpa)
//        else if (owner == "jururu") afterColor = activity.getColor(R.color.jururu)
//        else if (owner == "gosegu") afterColor = activity.getColor(R.color.gosegu)
//        else if (owner == "vichan") afterColor = activity.getColor(R.color.vichan)
//        else afterColor = activity.getColor(R.color.gray)
//
//        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), beforeColor, afterColor)
//        colorAnimation.duration = 500
//        colorAnimation.addUpdateListener { animator: ValueAnimator ->
//            binding.layoutTopInfo.setBackgroundColor(
//                animator.animatedValue as Int
//            )
//        }
//
//        colorAnimation.start()
//    }
//
//    private fun startHandler() {
//        if (adapterBanner.getList().isNotEmpty()) handler.postDelayed(r, autoScrollSpeed.toLong())
//    }
//
//    private fun cancelHandler() {
//        handler.removeCallbacks(r)
//        handler.removeCallbacksAndMessages(null)
//    }
//
//    private fun setMemberLiveData() {
//        val modelOwner = activity as ViewModelStoreOwner
//        members = ViewModelProvider(modelOwner).get(YoutubeMemberLiveData::class.java)
//        members.members.observe(activity as LifecycleOwner) {
//            Log.d("reset", SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(System.currentTimeMillis())))
//            adapterBanner.setList(it)
//            binding.bannerPager.setCurrentItem(it.size * 1000, false)
//            changeTop()
//            startHandler()
//        }
//        getChannels()
//    }
//
//    private fun getChannels() {
//        YoutubeMemberLiveData.reset(activity)
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (adapterBanner.getList().isNotEmpty()) startHandler()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        cancelHandler()
//    }
}