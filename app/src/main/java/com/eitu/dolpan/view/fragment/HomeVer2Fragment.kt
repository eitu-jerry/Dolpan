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
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.dataClass.firestore.YoutubeMember
import com.eitu.dolpan.databinding.FragmentHomeVer2Binding
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.view.activity.WakitchActivity
import com.eitu.dolpan.view.base.BaseFragment
import com.eitu.dolpan.view.custom.rememberMyNestedScrollInteropConnection
import com.eitu.dolpan.viewModel.HomeAct
import com.eitu.dolpan.viewModel.HomeItems
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlin.math.pow
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

    var isShow = mutableStateOf(true)

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun init() {
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
                    isShow.value = true
                    binding.coordinator.setPadding(0,0,0,0)
                }
                else if (isShow.value) {
                    //careful there should a space between double quote otherwise it wont work
                    binding.collapsingToolBar.title = " "
                    isShow.value = false
                    //binding.coordinator.setPadding(0,0,0, resources.getDimensionPixelOffset(R.dimen.appBottomHeight))
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
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    App()
                }
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

    private fun toDp(pixel : Float) : Dp = (pixel / resources.displayMetrics.density).dp
    private fun toPx(dp : Dp) : Float = (dp.value * resources.displayMetrics.density)

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun App() {

        val columnSize = resources.displayMetrics.heightPixels - appBarHeight// - toPx(dimensionResource(id = R.dimen.appBottomHeight))
        val swipeableState = rememberSwipeableState(initialValue = 0)
        val anchors = mapOf(0f to 0, -columnSize to 1)

        val twitchIcon = painterResource(id = R.drawable.icon_twitch_192)
        val naverCafeIcon = painterResource(id = R.drawable.icon_cafe)

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Vertical
                )

        ) {

            val startFolding = (constraints.maxHeight / 18 * 13).toFloat()
            val bottomPadding = (constraints.maxHeight - startFolding)

            val startFoldingMember = columnSize - toPx(70.dp) - toPx(dimensionResource(id = R.dimen.appBottomHeight))
            val bottomPaddingMember = toPx(70.dp) * 2

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
                LiveMemberList(
                    swipeableState = swipeableState,
                    columnSize = columnSize,
                    startFoldingY = startFoldingMember,
                    bottomPadding = bottomPaddingMember
                )
            }

            if (homeItems.list.isNotEmpty()) {
                NotificationList(
                    swipeableState = swipeableState,
                    columnSize = columnSize,
                    startFolding = startFolding,
                    bottomPadding = bottomPadding,
                    twitchIcon = twitchIcon,
                    naverCafeIcon = naverCafeIcon
                )
            }

        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun LiveMemberList(
        swipeableState : SwipeableState<Int>,
        columnSize : Float,
        startFoldingY : Float,
        bottomPadding : Float
    ) {

        val memberLazyListState = rememberLazyListState()
        val memberScrollUp = memberLazyListState.isScrollingUp()
        val memberScrollDown = memberLazyListState.isScrollingDown()
        val memberNested = object : NestedScrollConnection {
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                Log.d("isScrollingUp", "$memberScrollUp")
                Log.d("isScrollingDown", "$memberScrollDown")
                Log.d("canScrollForward", "${memberLazyListState.canScrollForward}")
                Log.d("onPostFling", "consumed : ${consumed.y}\navaliable : ${available.y}")
                if (!memberLazyListState.isScrollInProgress && memberScrollDown && !memberLazyListState.canScrollForward && available.y == 0f) {
                    swipeableState.animateTo(1)
                }
                return super.onPostFling(consumed, available)
            }
        }

        LazyColumn(
            state = memberLazyListState,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(top = 0.dp, bottom = toDp(bottomPadding)),
            modifier = Modifier
                .height(toDp(columnSize))
                .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
                //.padding(bottom = dimensionResource(id = R.dimen.appBottomHeight))
                .nestedScroll(memberNested)
        ) {

            itemsIndexed(homeItems.member) { index, it ->

                val opacity by remember {
                    derivedStateOf {
                        if (memberLazyListState.layoutInfo.visibleItemsInfo.size < 3)
                            return@derivedStateOf 1f
                        val currentItemInfo = memberLazyListState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == index }
                            ?: return@derivedStateOf 0.5f
                        val itemHalfSize = currentItemInfo.size / 2
                        if (currentItemInfo.offset + itemHalfSize < startFoldingY) return@derivedStateOf 1f
                        (1f - minOf(1f, abs(currentItemInfo.offset + itemHalfSize - startFoldingY) / startFoldingY) * 0.4f)
                    }
                }

                val translateY by remember {
                    derivedStateOf {
                        if (memberLazyListState.layoutInfo.visibleItemsInfo.size < 3)
                            return@derivedStateOf 0f
                        val currentItemInfo = memberLazyListState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == index }
                            ?: return@derivedStateOf 0f
                        val itemHalfSize = currentItemInfo.size / 2
                        if (currentItemInfo.offset + itemHalfSize < startFoldingY) return@derivedStateOf 0f
                        toPx(80.dp) * (1 - opacity)
                    }
                }

                ItemMember(it = it, opacity = opacity, translateY = translateY)
            }
        }
    }

    @Composable
    private fun ItemMember(
        it : YoutubeMember,
        opacity : Float,
        translateY : Float
    ) {

        Box(
            modifier = Modifier
                .offset(y = -Dp(translateY))
                .graphicsLayer(
                    translationY = -(translateY),
                    alpha = opacity.pow((1 - opacity) * 100)
                )
                .scale(opacity)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxWidth()
                    .height(80.dp)
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun NotificationList(
        swipeableState: SwipeableState<Int>,
        columnSize : Float,
        startFolding: Float,
        bottomPadding: Float,
        twitchIcon : Painter,
        naverCafeIcon : Painter
    ) {

        val lazyListState = rememberLazyListState()
        val nestedScroll = rememberMyNestedScrollInteropConnection(
            lazyListState = lazyListState,
            swipeableState = swipeableState
        )

        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(bottom = toDp(bottomPadding.toFloat())),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .nestedScroll(nestedScroll)
                .offset {
                    IntOffset(
                        0,
                        (swipeableState.offset.value + columnSize).roundToInt()
                    )
                }
        ) {
            itemsIndexed(homeItems.list, key = { position, _ -> position }) {index, it ->

                val opacity by remember {
                    derivedStateOf {
                        if (lazyListState.layoutInfo.visibleItemsInfo.size < 3)
                            return@derivedStateOf 1f
                        val currentItemInfo = lazyListState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == index }
                            ?: return@derivedStateOf 0.5f
                        val itemHalfSize = currentItemInfo.size / 2
                        if (currentItemInfo.offset + itemHalfSize < startFolding) return@derivedStateOf 1f
                        (1f - minOf(1f, abs(currentItemInfo.offset + itemHalfSize - startFolding) / startFolding) * 0.4f)
                    }
                }

                val translateY by remember {
                    derivedStateOf {
                        if (lazyListState.layoutInfo.visibleItemsInfo.size < 3)
                            return@derivedStateOf 0f
                        val currentItemInfo = lazyListState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == index }
                            ?: return@derivedStateOf 0f
                        val itemHalfSize = currentItemInfo.size / 2
                        if (currentItemInfo.offset + itemHalfSize < startFolding) return@derivedStateOf 0f
                        toPx(70.dp) * (1 - opacity.pow(opacity * 5))
                    }
                }

                Item(
                    it = it,
                    index = index,
                    opacity = opacity,
                    translateY = translateY,
                    twitchIcon = twitchIcon,
                    naverCafeIcon = naverCafeIcon
                )
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Item(
        it : Chat,
        index : Int,
        opacity : Float,
        translateY : Float,
        twitchIcon : Painter,
        naverCafeIcon : Painter,
        modifier: Modifier = Modifier
    ) {

        val type = it.type

        Box(modifier = modifier
            .offset(y = -Dp(translateY))
            .graphicsLayer(
                translationY = -(translateY),
                alpha = opacity.pow((1 - opacity) * 100)
            )
            .scale(Math.max(0.7f, opacity))
            .zIndex(-index.toFloat())

        ) {
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
                    .clickable {
                        if (it.type == "twitchChat") {
                            IntentHelper.intentDetail(
                                requireActivity(),
                                WakitchActivity::class.java
                            )
                        }
                    }
            ) {
                Image(
                    painter =
                    if (it.type == "twitchChat") twitchIcon
                    else naverCafeIcon,
                    contentDescription = "type icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
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
                                     "${ownerMap[it.sendFrom]} -> ${ownerMap[it.owner]}"
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
                        text = it.title,
                        fontWeight = FontWeight(400),
                        fontSize = 13.sp
                    )
                }
            }
        }

    }

    @OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
    @Composable
    private fun AppBottom() {
        Box(modifier = Modifier.padding(30.dp)) {
            GlideImage(
                model = R.drawable.button_write,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .combinedClickable(
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
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .combinedClickable(
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

    @Composable
    private fun LazyListState.isScrollingDown(): Boolean {
        var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
        var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
        return remember(this) {
            derivedStateOf {
                if (previousIndex != firstVisibleItemIndex) {
                    previousIndex < firstVisibleItemIndex
                }
                else {
                    previousScrollOffset <= firstVisibleItemScrollOffset
                }.also {
                    previousIndex = firstVisibleItemIndex
                    previousScrollOffset = firstVisibleItemScrollOffset
                }
            }
        }.value
    }

}