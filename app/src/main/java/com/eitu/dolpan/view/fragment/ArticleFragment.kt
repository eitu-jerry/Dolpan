package com.eitu.dolpan.view.fragment

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.view.base.BaseFragment
import com.eitu.dolpan.viewModel.CafeNotice
import dagger.hilt.android.AndroidEntryPoint
import com.eitu.dolpan.R

@AndroidEntryPoint
class ArticleFragment : BaseFragment() {

    companion object {
        fun newInstance() : ArticleFragment {
            return ArticleFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                App()
            }
        }
    }

    @Composable
    private fun App() {

        val cafeNotice : CafeNotice by viewModels()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.gray))
                ) {
                    NoticeArticle(title = "카페 공지사항", list = cafeNotice.listNotice)
                    NoticeArticle(title = "이세돌 공지사항", list = cafeNotice.listIsdNotice)
                }
            }
        }
    }

    @Composable
    private fun NoticeArticle(title : String, list : SnapshotStateList<Article>) {
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
                    modifier = Modifier.padding(top = 20.dp, start = 15.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "더보기",
                    fontSize = 13.sp,
                    fontWeight = FontWeight(800),
                    modifier = Modifier.padding(end = 15.dp)
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
                    ArticleItem(item = it)
                }
            }
        }
    }

    @Composable
    private fun ArticleItem(item: Article) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .width(220.dp)
                .height(50.dp)
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("navercafe://cafe/27842958/${item.articleId}")
                        startActivity(intent)
                    } catch (e: java.lang.Exception) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data =
                            Uri.parse("https://m.cafe.naver.com/ca-fe/web/cafes/steamindiegame/articles/${item.articleId}?useCafeId=false")
                        startActivity(intent)
                    }
                }
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
                Text(text = getStyledSubject(item.subject), overflow = TextOverflow.Ellipsis  , maxLines = 2, fontWeight = FontWeight(400))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = item.formatWriteDate(), modifier = Modifier.weight(1f), fontSize = 12.sp)
                    Text(text = "조회수 ${item.readCount}", modifier = Modifier.weight(2f), fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(colorResource(id = R.color.lightGray)))
        }
    }

    @Composable
    private fun getStyledSubject(subject : String) : AnnotatedString {
        val head : String

        if (subject.contains("[아이네]")) {
            head = "[아이네]"
            return buildAnnotatedString {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
        }
        else if (subject.contains("[징버거]")) {
            head = "[징버거]"
            return buildAnnotatedString {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
        }
        else if (subject.contains("[릴파]")) {
            head = "[릴파]"
            return buildAnnotatedString {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
        }
        else if (subject.contains("[주르르]")) {
            head = "[주르르]"
            return buildAnnotatedString {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
        }
        else if (subject.contains("[고세구]")) {
            head = "[고세구]"
            return buildAnnotatedString {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
        }
        else if (subject.contains("[비챤]")) {
            head = "[비챤]"
            return buildAnnotatedString {
                append(styledHead(head))
                append(subject.substringAfter(head))
            }
        }
        else {
            return buildAnnotatedString {
                append(subject)
            }
        }
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

}