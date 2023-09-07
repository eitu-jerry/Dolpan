package com.eitu.dolpan.view.fragment

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.databinding.FragmentHomeVer2Binding
import com.eitu.dolpan.view.base.BaseFragment
import com.eitu.dolpan.view.custom.MyNestedScrollInteropConnection
import com.eitu.dolpan.view.custom.rememberMyNestedScrollInteropConnection
import com.eitu.dolpan.viewModel.HomeItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeVer2Fragment : BaseFragment() {

    companion object {
        fun newInstance() : HomeVer2Fragment {
            return HomeVer2Fragment()
        }
    }

    private lateinit var binding : FragmentHomeVer2Binding

    override fun setBinding(inflater: LayoutInflater): View {
        binding = FragmentHomeVer2Binding.inflate(inflater)
        return binding.root
    }

    private val homeItems : HomeItems by viewModels()

    private val ownerMap = linkedMapOf(
        Pair("wak", "우왁굳"),
        Pair("ine", "아이네"),
        Pair("jing", "징버거"),
        Pair("lilpa", "릴파"),
        Pair("jururu", "주르르"),
        Pair("gosegu", "고세구"),
        Pair("vichan", "비챤"),
    )

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun init() {
        binding.lifecycleOwner = this
        binding.model = homeItems

        (binding.root as CoordinatorLayout).addView(
            ComposeView(requireContext()).apply {
                setContent {
                    GlideImage(model = R.drawable.img, contentDescription = "asdf", contentScale = ContentScale.Crop, modifier = Modifier.blur(10.dp))
                }
            }
        )

        binding.appBar.bringToFront()
        binding.frame.apply {
            setContent { App() }
            bringToFront()
        }

        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolBar.title = "Title Collapse"
                binding.collapsingToolBar.alpha = 0f
                binding.collapsingToolBar.animate().alpha(1f).setDuration(300).start()
                isShow = true
            } else if (isShow) {
                //careful there should a space between double quote otherwise it wont work
                binding.collapsingToolBar.title = " "
                isShow = false
            }
        }
    }

    val cellHeight : Float by lazy {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics)
    }
    val spacing : Float by lazy {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
    @Composable
    private fun App() {

        val scrollState = rememberScrollState()

        val columnSize = resources.displayMetrics.heightPixels - 250.dp.value
        val swipeableState = rememberSwipeableState(initialValue = 0)
        val anchors = mapOf(0f to 0, -columnSize to 1)

        val lazyListState = rememberLazyListState()

        val nestedScroll = rememberMyNestedScrollInteropConnection(
            lazyListState = lazyListState,
            swipeableState = swipeableState
        )

        var canDownTo by remember { mutableStateOf(false) }
        var canUpTo by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Vertical
                )
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .height(Dp(columnSize))
                    .verticalScroll(
                        scrollState,
                        flingBehavior = remember {
                            object : FlingBehavior {
                                override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                                    Log.d("performFling", "$initialVelocity")
                                    if (scrollState.canScrollForward) {
                                        canDownTo = false
                                        scrollState.animateScrollBy(initialVelocity)
                                    } else if (initialVelocity < 0.1) {
                                        coroutineScope.launch(Dispatchers.Default) {
                                            delay(100)
                                            canDownTo = true
                                        }
                                        if (canDownTo) {
                                            swipeableState.animateTo(1)
                                        }
                                    }
                                    return 0f
                                }
                            }
                        }
                    )
                    //.verticalScroll(scrollState)
                    .padding(top = 50.dp, bottom = 200.dp)
                    .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
            ) {

                ownerMap.keys.forEach {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp)
                            .fillMaxWidth()
                            .height(80.dp)
                            .defaultMinSize(minHeight = 70.dp)
                            .background(
                                color = colorResource(id = R.color.stackBackground),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(15.dp)
                    ) {}
                }

            }

            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScroll)
                    .offset {
                        IntOffset(
                            0,
                            (swipeableState.offset.value + columnSize).roundToInt()
                        )
                    }
            ) {
                itemsIndexed(homeItems.list, key = { position, _ -> position }) {position, it ->
                    Item(it = it, modifier = Modifier.animateItemPlacement())
                }
            }

        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Item(it : Chat, modifier: Modifier) {
        Box(modifier = modifier) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .defaultMinSize(minHeight = 70.dp)
                    .background(
                        color = colorResource(id = R.color.stackBackground),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(15.dp)
            ) {
                GlideImage(
                    model = R.drawable.button_write,
                    contentDescription = "asdf",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(
                            color = colorResource(id = R.color.stackBackground),
                            shape = CircleShape
                        )
                        .align(Alignment.CenterVertically)
                )
                Column() {
                    Text(
                        text = ownerMap[it.owner] ?: "모시깽"
                    )
                    Text(
                        text = it.title
                    )
                }
            }
        }
    }

}