package com.eitu.dolpan.view.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.eitu.dolpan.R
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.network.api.menuMap
import com.eitu.dolpan.view.activity.ArticleActivity
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.view.base.BaseFragment
import com.eitu.dolpan.view.composable.MenuBar
import com.eitu.dolpan.view.composable.NoticeArticle
import com.eitu.dolpan.viewModel.CafeNotice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : BaseFragment() {

    private val activity : BaseActivity by lazy {
        requireActivity() as BaseActivity
    }

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

        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        
        Scaffold(
            topBar = {
                MenuBar(scaffoldState = scaffoldState, coroutineScope = coroutineScope)
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                MenuDrawer()
            }
        ) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(id = R.color.lightGray))
                    ) {
                        NoticeArticle(title = "카페 공지사항", list = cafeNotice.listNotice, activity, menuId = 24)
                        NoticeArticle(title = "이세돌 공지사항", list = cafeNotice.listIsdNotice, activity, menuId = 345)
                    }
                }
            }
        }
    }

    @Composable
    fun MenuDrawer() {
        Column() {
            menuMap.keys.forEach {
                TextButton(
                    onClick = {
                        IntentHelper.intentDetail(activity, Intent(activity, ArticleActivity::class.java).apply {
                            putExtra("menuId", it)
                        })
                    }
                ) {
                    Text(text = menuMap[it] ?: "우왁끼는 살아있다")
                }
            }
        }
    }

}