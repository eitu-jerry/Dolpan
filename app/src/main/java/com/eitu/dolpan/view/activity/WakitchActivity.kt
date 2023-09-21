package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.firestore.YoutubeMember
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.viewModel.MemberTwitch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WakitchActivity : BaseActivity() {

    override fun setBinding(): View? = null

    override fun init() {}

    private val owner : String by lazy { intent.getStringExtra("owner") ?: "wak" }

    private val memberModel : MemberTwitch by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberModel.setOwner(owner)
        setContent {
            App()
        }
    }

    @Composable
    private fun App() {

        val member = memberModel.memberState()

        Column {
            Twitch(member)
            Chat()
        }

    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Twitch(member : State<YoutubeMember?>) {

        val isLive by remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Box() {
                GlideImage(
                    model = R.drawable.not_live_jing,
                    contentDescription = "Thumbnail image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
                ) {
                    if (isLive) {
                        Box(modifier = Modifier
                            .size(10.dp)
                            .background(color = Color.Red, shape = CircleShape))
                    }
                    else {
                        Box(modifier = Modifier
                            .size(10.dp)
                            .background(color = Color.Gray, shape = CircleShape)
                            .align(Alignment.CenterVertically)
                        )
                        Text(text = "오프라인")
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp)
            ) {
                GlideImage(
                    model = R.drawable.icon_twitch_192,
                    contentDescription = "profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(
                            color = Color.Gray,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "징버거 (jingburger)",
                        fontSize = 19.sp,
                        fontWeight = FontWeight(600)
                    )
                    Text(
                        text = "오느른여~",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Just Chatting",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    @Composable
    private fun Chat() {
        Column() {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.LightGray))
            Text(text = "채팅", modifier = Modifier.padding(10.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.LightGray))
            LazyColumn() {

            }
        }
    }

}