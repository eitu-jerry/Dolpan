package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.eitu.dolpan.view.base.BaseActivity

class WakitchActivity : BaseActivity() {

    override fun setBinding(): View? = null

    override fun init() {}

    private val owner : String by lazy { intent.getStringExtra("owner") ?: "wak" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    @Composable
    private fun App() {

        Column {
            Twitch()
            Chat()
        }

    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Twitch() {
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