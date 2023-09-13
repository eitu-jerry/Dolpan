package com.eitu.dolpan.view.fragment

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.dataClass.firestore.YoutubeMember
import com.eitu.dolpan.databinding.FragmentHomeVer2Binding
import com.eitu.dolpan.view.base.BaseFragment
import com.eitu.dolpan.view.custom.rememberMyNestedScrollInteropConnection
import com.eitu.dolpan.viewModel.HomeAct
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
    private val homeAct : HomeAct by activityViewModels()

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
        var isShow = true
        var scrollRange = -1

        binding.apply {
            lifecycleOwner = this@HomeVer2Fragment
            model = homeItems

            appBar.addOnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolBar.title = "Title Collapse"
                    binding.collapsingToolBar.alpha = 0f
                    binding.collapsingToolBar.animate().alpha(1f).setDuration(300).start()
                    isShow = true
                }
                else if (isShow) {
                    //careful there should a space between double quote otherwise it wont work
                    binding.collapsingToolBar.title = " "
                    isShow = false
                }
            }

            background.apply {
                lifecycleOwner = this@HomeVer2Fragment
                setContent {
                    GlideImage(
                        model = R.drawable.img,
                        contentDescription = "background Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.blur(10.dp)
                    )
                }
            }
            app.apply {
                lifecycleOwner = this@HomeVer2Fragment
                setContent { App() }
            }
            appBottom.apply {
                lifecycleOwner = this@HomeVer2Fragment
                setContent { AppBottom() }
            }
        }
    }

    private val appBarHeight : Float by lazy {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250f, resources.displayMetrics)
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
    @Composable
    private fun App() {

        val scrollState = rememberScrollState()
        val columnSize = resources.displayMetrics.heightPixels - appBarHeight
        val swipeableState = rememberSwipeableState(initialValue = 0)
        val anchors = mapOf(0f to 0, -columnSize to 1)

        val lazyListState = rememberLazyListState()
        val nestedScroll = rememberMyNestedScrollInteropConnection(
            lazyListState = lazyListState,
            swipeableState = swipeableState
        )

        var canDownTo by remember { mutableStateOf(false) }

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

            if (homeItems.member.isEmpty()) {
                Text(
                    text = "뱅없",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset {
                            IntOffset(
                                0,
                                (swipeableState.offset.value).roundToInt()
                            )
                        }
                )
            }
            else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .height(Dp(columnSize))
                        .verticalScroll(
                            scrollState,
                            flingBehavior = remember {
                                object : FlingBehavior {
                                    override suspend fun ScrollScope.performFling(
                                        initialVelocity: Float
                                    ): Float {
                                        Log.d("performFling", "$initialVelocity")
                                        if (scrollState.canScrollForward) {
                                            canDownTo = false
                                            scrollState.animateScrollBy(initialVelocity)
                                        } else if (initialVelocity >= 0) {
                                            coroutineScope.launch(Dispatchers.Default) {
                                                delay(100)
                                                canDownTo = true
                                            }
                                            if (canDownTo) {
                                                swipeableState.animateTo(1)
                                            }
                                        }
                                        return initialVelocity
                                    }
                                }
                            }
                        )
                        .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
                        .padding(top = 50.dp, bottom = 250.dp)
                ) {

                    homeItems.member.forEach {
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
                        ) {
                            Text(text = "${it.name}")
                        }
                    }
                }
            }

            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(bottom = 15.dp),
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
                    Item(
                        it = it,
                        enterTransition =
                        if (lazyListState.isScrollingUp()) {
                            scaleIn(initialScale = 1f)
                        }
                        else {
                            scaleIn(
                                initialScale = 0.7f
                            ) + fadeIn()
                        }
                    )
                }
            }

        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ItemMember(it : YoutubeMember) {

        var visible by remember { mutableStateOf(true) }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally() + scaleIn(),
            exit = slideOutHorizontally() + scaleOut()
        ) {
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
            ) {
                Text(text = "${it.name}")
            }
        }
        
        LaunchedEffect(key1 = 0) {
            visible = true
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Item(it : Chat, modifier: Modifier = Modifier, enterTransition: EnterTransition) {

        var visible by remember { mutableStateOf(false) }

        AnimatedVisibility(
            visible = visible,
            enter = enterTransition
        ) {
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
                        model = if (it.type == "twitchChat") R.drawable.icon_twitch_192
                            else R.drawable.icon_cafe,
                        contentDescription = "asdf",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .background(
                                color = colorResource(id = R.color.stackBackground),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .align(Alignment.CenterVertically)
                    )
                    Column() {
                        Row() {
                            Text(
                                text =
                                if (it.type == "twitchChat") {
                                    if (it.sendFrom == null) {
                                        ownerMap[it.owner] ?: "모시깽"
                                    }
                                    else {
                                        ownerMap[it.sendFrom] ?: "모시깽"
                                    }
                                }
                                else {
                                    ownerMap[it.owner] ?: "모시깽"
                                },
                                fontWeight = FontWeight(500),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = it.formatWriteDate(),
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.textLightGray),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Text(
                            text = if (it.type == "twitchChat") {
                                if (it.sendFrom == null) {
                                    it.title
                                }
                                else {
                                    "To ${ownerMap[it.owner]}. ${it.title}"
                                }
                            }
                            else {
                                it.title
                            },
                            fontWeight = FontWeight(400),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            visible = true
        }

    }

    @OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
    @Composable
    private fun AppBottom() {
        Box(modifier = Modifier) {
            GlideImage(
                model = R.drawable.button_write,
                contentDescription = "",
                modifier = Modifier.align(Alignment.CenterStart).combinedClickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onLongClick = {

                    },
                    onClick = {

                    }
                ))
            GlideImage(
                model = R.drawable.button_write,
                contentDescription = "",
                modifier = Modifier.align(Alignment.CenterEnd).combinedClickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onLongClick = {
                        homeAct.page.value = 2
                    },
                    onClick = {

                    }
                ))
        }
    }

    @Composable
    private fun LazyListState.isScrollingUp(): Boolean {
        var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
        var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
        return remember(this) {
            derivedStateOf {
                if (previousIndex != firstVisibleItemIndex) {
                    previousIndex > firstVisibleItemIndex
                } else {
                    previousScrollOffset >= firstVisibleItemScrollOffset
                }.also {
                    previousIndex = firstVisibleItemIndex
                    previousScrollOffset = firstVisibleItemScrollOffset
                }
            }
        }.value
    }

}