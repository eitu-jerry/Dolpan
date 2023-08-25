package com.eitu.dolpan.view.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.view.activity.WebViewActivity
import com.eitu.dolpan.view.base.BaseActivity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleItemForActivity(item : Article, activity: BaseActivity) {

    val subjectSize = 14.sp
    val infoSize = 11.sp

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 85.dp)
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("navercafe://cafe/27842958/${item.articleId}")
                        activity.startActivity(intent)
                    } catch (e: Exception) {
                        IntentHelper.intentDetail(
                            activity,
                            Intent(activity, WebViewActivity::class.java).apply {
                                putExtra(
                                    "url",
                                    "https://m.cafe.naver.com/ca-fe/web/cafes/steamindiegame/articles/${item.articleId}?useCafeId=false"
                                )
                            })
                    }
                }
        ) {
            Text(
                text = getStyledSubject(
                    if (item.useHead) "[${item.headName}] ${item.subject}"
                    else item.subject
                ),
                fontWeight = FontWeight(500),
                fontSize = subjectSize
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = item.writerNickname,
                    maxLines = 2,
                    fontSize = infoSize,
                    color = colorResource(id = R.color.textLightGray)
                )
                Text(
                    text = item.formatWriteDate(),
                    fontSize = infoSize,
                    color = colorResource(id = R.color.textLightGray)
                )
                Text(
                    text = "조회 ${item.readCount}",
                    fontSize = infoSize,
                    color = colorResource(id = R.color.textLightGray)
                )
            }
        }
        if (item.attachImage && item.representImage != null) {
            GlideImage(
                model = item.representImage,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(65.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(5.dp))
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(40.dp)
                .height(65.dp)
                .background(
                    color = colorResource(id = R.color.lightGray),
                    shape = RoundedCornerShape(5.dp)
                )
                .clickable {
                    IntentHelper.intentDetail(
                        activity,
                        Intent(activity, WebViewActivity::class.java).apply {
                            putExtra(
                                "url",
                                "https://m.cafe.naver.com/ca-fe/web/cafes/27842958/articles/${item.articleId}/comments?page=1"
                            )
                        })
                }
        ) {
            Text(
                text = "${item.commentCount}",
                fontWeight = FontWeight(500),
                fontSize = infoSize,
                color = colorResource(id = R.color.darkGray)
            )
            Text(
                text = "댓글",
                fontSize = 10.sp,
                color = colorResource(id = R.color.textLightGray)
            )
        }
    }
    Divider()
}