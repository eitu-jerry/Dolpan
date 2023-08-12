package com.eitu.dolpan.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.viewModel.memberChat.MemberChat
import com.eitu.dolpan.view.base.BaseActivity
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch
import java.util.*

class ChatActivity: BaseActivity() {

    private val owner : String by lazy {
        intent.getStringExtra("owner") ?: ""
    }
    private val name : String by lazy {
        intent.getStringExtra("name") ?: ""
    }
    private val profileImage : String by lazy {
        intent.getStringExtra("profileImage") ?: ""
    }

    private val memberChat: MemberChat by viewModels()

    override fun setBinding(): View? {
        return null
    }

    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberChat.setOwner(owner)
        setContent { 
            App()
        }
    }

    @Composable
    private fun App() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatList()
            TopLayout()
        }
    }

    @Composable
    fun TopLayout() {
        val interactionSource = remember { MutableInteractionSource() }
        Box(modifier = Modifier
            .clickable(indication = null, interactionSource = interactionSource, onClick = {})
            .fillMaxWidth()
            .height(80.dp)
            .background(color = colorResource(id = R.color.transBlack), shape = RectangleShape)
            .padding(start = 10.dp, end = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_rewind),
                contentDescription = "backButton",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart)
                    .clickable(indication = null, interactionSource = interactionSource, onClick = {
                        finish()
                        IntentHelper.outDetailAnim(this@ChatActivity)
                    })
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(resId = R.font.korail_round_gothic_medium)),
                fontSize = 18.sp,
                modifier = Modifier.align(alignment = Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.img_rewind),
                contentDescription = "backButton",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }

    @Composable
    fun ChatList() {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val chatList = memberChat.chatList.collectAsLazyPagingItems()

        LazyColumn(
            contentPadding = PaddingValues(top = 80.dp, start = 13.dp, end = 13.dp, bottom = 43.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = listState
        ) {
            coroutineScope.launch {
                val size = chatList.itemCount
                val limit = 13
                if (size > limit && listState.firstVisibleItemIndex < limit - 1) {
                    listState.scrollToItem(index = size - 1)
                }
            }
            itemsIndexed(items = chatList.itemSnapshotList) { i, item ->
                var preItem: Chat? = null
                if (i != 0) {
                    preItem = chatList[i - 1]
                }
                ChatShell(preItem = preItem, item = item!!)
            }
        }
    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ChatShell(preItem: Chat?, item: Chat) {

        val preFormatted = preItem?.getFormattedTime()
        val formatted = item.getFormattedTime()

        val showProfile = preFormatted != formatted

        val preToday = preItem?.getFormattedDate()
        val today = item.getFormattedDate()

        val showToday = today != preToday

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            if (showToday) {
                Spacer(modifier = Modifier)
                Text(
                    text = "$today",
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.LightGray
                        )
                        .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                if (showProfile) {
                    GlideImage(
                        model = profileImage,
                        contentDescription = "profileImage",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(17.dp))
                    )
                }
                else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
                Row() {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        if (showProfile) {
                            Text(text = name, fontSize = 13.sp)
                        }
                        ChatText(item.title, formatted)
                        if (item.type == "naverCafe") {
                            NaverCafeLink(id = item.id)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ChatText(title : String, time : String) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Box(modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .widthIn(min = 30.dp, max = 220.dp)
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.LightGray
                )
                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
            ) {
                Text(text = title, fontSize = 15.sp)
            }
            Text(
                text = time,
                fontSize = 9.sp,
                modifier = Modifier.align(alignment = Alignment.Bottom))
        }
    }

    @Composable
    private fun NaverCafeLink(id : String) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.clickable {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("navercafe://cafe/27842958/${id}")
                    startActivity(intent)
                } catch (e: java.lang.Exception) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data =
                        Uri.parse("https://m.cafe.naver.com/ca-fe/web/cafes/steamindiegame/articles/${id}?useCafeId=false")
                    startActivity(intent)
                }
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_cafe_64),
                contentDescription = "iconCafe",
                modifier = Modifier.size(18.dp)
            )
            Text(text = "카페앱에서 보기", fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }

}