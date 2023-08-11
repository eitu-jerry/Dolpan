package com.eitu.dolpan.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.viewModel.memberChat.MemberChat
import com.eitu.dolpan.view.base.BaseActivity
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        init(memberChat = memberChat)
//        setContent {
//            AppChat()
//        }
//    }
//
//    @Composable
//    fun AppChat() {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//        ) {
//            ListChat(memberChat)
//            TopLayout()
//        }
//    }
//
//    @Composable
//    fun TopLayout() {
//        val interactionSource = remember { MutableInteractionSource() }
//        Box(modifier = Modifier
//            .clickable(indication = null, interactionSource = interactionSource, onClick = {})
//            .fillMaxWidth()
//            .background(color = colorResource(id = R.color.transBlack), shape = RectangleShape)
//            .padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 25.dp)
//
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.img_rewind),
//                contentDescription = "backButton",
//                modifier = Modifier
//                    .size(40.dp)
//                    .align(Alignment.CenterStart)
//                    .clickable(indication = null, interactionSource = interactionSource, onClick = {
//                        finish()
//                        IntentHelper.outDetailAnim(this@ChatActivity)
//                    })
//
//            )
//            Text(
//                text = name,
//                textAlign = TextAlign.Center,
//                fontFamily = FontFamily(Font(resId = R.font.korail_round_gothic_medium)),
//                fontSize = 18.sp,
//                modifier = Modifier.align(alignment = Alignment.Center)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.img_rewind),
//                contentDescription = "backButton",
//                modifier = Modifier
//                    .size(40.dp)
//                    .align(Alignment.CenterEnd)
//            )
//        }
//    }
//
//    @Composable
//    fun ListChat(viewModel: MemberChat) {
//        val chatState = rememberLazyListState()
//        val coroutineScope = rememberCoroutineScope()
//        val chatList = viewModel.chat
//        LazyColumn(
//            contentPadding = PaddingValues(13.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            state = chatState
//        ) {
//            coroutineScope.launch {
//                val size = chatList.size
//                val limit = 13
//                if (size > limit && chatState.firstVisibleItemIndex < limit - 1) {
//                    chatState.scrollToItem(index = chatList.size - 1)
//                }
//            }
//            itemsIndexed(items = chatList) { i, item ->
//                var preItem: Chat? = null
//                if (i != 0) {
//                    preItem = chatList[i - 1]
//                }
//                ChatShell(preItem = preItem, item = item)
//            }
//        }
//    }
//
//    private val fromFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//    private val toFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEEEE요일", Locale.getDefault())
//    @OptIn(ExperimentalGlideComposeApi::class)
//    @Composable
//    fun ChatShell(preItem: Chat?, item: Chat) {
//
//        var preDate: String = ""
//        var date: String = ""
//
//        try {
//            date = toFormat.format(fromFormat.parse(item.date))
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//
//        if (preItem != null) {
//            try {
//                preDate = toFormat.format(fromFormat.parse(preItem.date))
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
//            if (date != preDate) {
//                Text(
//                    text = date,
//                    fontSize = 11.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .background(
//                            shape = RoundedCornerShape(12.dp),
//                            color = Color.LightGray
//                        )
//                        .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
//                )
//            }
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
//                GlideImage(
//                    model = profileImage,
//                    contentDescription = "profileImage",
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(RoundedCornerShape(17.dp))
//                )
//                Row() {
//                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
//                        Text(text = name, fontSize = 13.sp)
//                        ChatText(item)
//                        if (item.type == "naverCafe") {
//                            Row(
//
//                                horizontalArrangement = Arrangement.spacedBy(5.dp),
//                                modifier = Modifier.clickable {
//                                    try {
//                                        val intent = Intent(Intent.ACTION_VIEW)
//                                        intent.data = Uri.parse("navercafe://cafe/27842958/${item.id}")
//                                        startActivity(intent)
//                                    } catch (e: java.lang.Exception) {
//                                        val intent = Intent(Intent.ACTION_VIEW)
//                                        intent.data =
//                                            Uri.parse("https://m.cafe.naver.com/ca-fe/web/cafes/steamindiegame/articles/${item.id}?useCafeId=false")
//                                        startActivity(intent)
//                                    }
//                                }
//                            ) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.icon_cafe_64),
//                                    contentDescription = "iconCafe",
//                                    modifier = Modifier.size(18.dp)
//                                )
//                                Text(text = "카페앱에서 보기", fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterVertically))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun ChatText(item: Chat) {
//        var time = ""
//        try {
//            time = item.date.split(" ")[1]
//            val hour = time.split(":")[0].toInt()
//            val minute = time.split(":")[1]
//            if (hour > 12) {
//                time = "오후 ${hour-12}:$minute"
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//
//        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
//            Box(modifier = Modifier
//                .wrapContentWidth()
//                .wrapContentHeight()
//                .widthIn(min = 30.dp, max = 220.dp)
//                .background(
//                    shape = RoundedCornerShape(12.dp),
//                    color = Color.LightGray
//                )
//                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
//            ) {
//                Text(text = item.title, fontSize = 15.sp)
//            }
//            Text(
//                text = time,
//                fontSize = 9.sp,
//                modifier = Modifier.align(alignment = Alignment.Bottom))
//        }
//    }
//
////    fun init(memberChat: MemberChat) {
////        val fdb = Firebase.firestore
////        val collection = fdb.collection("item")
////        val query = collection
////            .whereEqualTo("owner", owner)
////            .orderBy("date", Query.Direction.ASCENDING)
////
////        query.get(Source.CACHE)
////            .addOnSuccessListener { fromCache ->
////                if (!fromCache.isEmpty) {
////                    Log.d("chat", "chat is loaded from cache")
////                    memberChat.addList(Chat.toList(fromCache.documents))
////                }
////                query.get().addOnSuccessListener { fromCloud ->
////                    if (!fromCloud.isEmpty) {
////                        Log.d("chat", "chat is loaded from cloud")
////                        memberChat.addList(Chat.toList(fromCloud.documents))
////                    }
////                }
////                fdb.collection("item")
////                    .whereEqualTo("owner", owner)
////                    .orderBy("date", Query.Direction.DESCENDING)
////                    .limit(5)
////                    .addSnapshotListener { value, error ->
////                        Log.d("chat", "chat is updated")
////                        if (value != null && !value.isEmpty) {
////                            for (change in value.documentChanges) {
////                                if (change.type == DocumentChange.Type.ADDED) {
////                                    memberChat.addItem(change.document.toObject(Chat::class.java))
////                                }
////                            }
////                        }
////                    }
////            }
////            .addOnFailureListener {
////                Log.d("e", it.toString())
////            }
////
////    }

}