package com.eitu.dolpan.view.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.view.activity.ArticleActivity
import com.eitu.dolpan.view.activity.WebViewActivity
import com.eitu.dolpan.view.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

@Composable
fun MenuBar(scaffoldState: ScaffoldState, coroutineScope: CoroutineScope, title: String? = "우왁끼는 살아있다") {
    TopAppBar(
        title = { Text(text = title ?: "우왁끼는 살아있다") },
        contentColor = Color.Black,
        backgroundColor = Color.Transparent,
        navigationIcon = {
            IconButton(
                onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
            ) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            } },
        elevation = 0.dp
    )
}

@Composable
fun NoticeArticle(
    title : String,
    list : SnapshotStateList<Article>,
    activity: BaseActivity,
    menuId : Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight(800),
                modifier = Modifier.padding(top = 15.dp, start = 15.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "더보기",
                fontSize = 13.sp,
                fontWeight = FontWeight(800),
                color = colorResource(id = R.color.darkGray),
                modifier = Modifier
                    .padding(end = 15.dp)
                    .clickable {
                        IntentHelper.intentDetail(
                            activity,
                            Intent(activity, ArticleActivity::class.java).apply {
                                putExtra("menuId", menuId)
                            })
                    }
            )
        }
        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .height(256.dp)
                .padding(bottom = 30.dp),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
        ) {
            items(list) {
                ArticleItem(item = it, activity)
            }
        }
    }
}

@Composable
fun ArticleItem(item: Article, activity : BaseActivity) {

    val subjectSize = 14.sp
    val infoSize = 11.sp

    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier
            .width(220.dp)
            .height(50.dp)
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
        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = getStyledSubject(item.subject),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis  ,
                fontWeight = FontWeight(500), 
                fontSize = subjectSize
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = item.formatWriteDate(),
                    modifier = Modifier.weight(1f),
                    fontSize = infoSize,
                    color = colorResource(id = R.color.textLightGray)
                )
                Text(
                    text = "조회수 ${item.readCount}",
                    modifier = Modifier.weight(2f),
                    fontSize = infoSize,
                    color = colorResource(id = R.color.textLightGray)
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(colorResource(id = R.color.lightGray)))
    }
}

@Composable
fun getStyledSubject(subject : String) : AnnotatedString {
    val head : String?

    @Composable
    fun styledSubject(subject: String, head: String?) : AnnotatedString {
        return buildAnnotatedString {
            if (head != null) {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
            else {
                append(subject)
            }
        }
    }

    if (subject.contains("[아이네]")) {
        head = "[아이네]"
    }
    else if (subject.contains("[징버거]")) {
        head = "[징버거]"
    }
    else if (subject.contains("[릴파]")) {
        head = "[릴파]"
    }
    else if (subject.contains("[주르르]")) {
        head = "[주르르]"
    }
    else if (subject.contains("[고세구]")) {
        head = "[고세구]"
    }
    else if (subject.contains("[비챤]")) {
        head = "[비챤]"
    }
    else if (subject.contains("[왁굳이야기]")) {
        head = "[왁굳이야기]"
    }
    else {
        head = null
    }

    return styledSubject(subject = subject, head = head)
}

@Composable
private fun styledHead(head : String) : AnnotatedString {

    val color = when(head) {
        "[아이네]" -> colorResource(id = R.color.ine)
        "[징버거]" -> colorResource(id = R.color.jing)
        "[릴파]" -> colorResource(id = R.color.lilpa)
        "[주르르]" -> colorResource(id = R.color.jururu)
        "[고세구]" -> colorResource(id = R.color.gosegu)
        "[비챤]" -> colorResource(id = R.color.vichan)
        "[왁굳이야기]" -> colorResource(id = R.color.wak)
        else -> Color.Black
    }

    return buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = color,
                fontWeight = FontWeight(600)
            )
        ) {
            append(head)
        }
    }
}