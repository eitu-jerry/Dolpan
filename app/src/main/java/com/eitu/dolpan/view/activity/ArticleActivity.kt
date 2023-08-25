package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.eitu.dolpan.network.api.menuMap
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.view.composable.ArticleItemForActivity
import com.eitu.dolpan.view.composable.MenuBar
import com.eitu.dolpan.viewModel.cafeArticle.CafeArticle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleActivity : BaseActivity() {

    private val listArticle : CafeArticle by viewModels()

    override fun setBinding(): View? {
        return null
    }

    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }

    @Composable
    private fun App() {

        listArticle.setMenuId(intent.getIntExtra("menuId", -1))
        val list = listArticle.list.collectAsLazyPagingItems()

        var title by remember { mutableStateOf("") }
        listArticle.menuId.observe(this) {
            title = menuMap[it] ?: "우왁끼는 살아있다"
        }

        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                MenuBar(
                    scaffoldState = scaffoldState,
                    coroutineScope = coroutineScope,
                    title = title
                )
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                MenuDrawer(scaffoldState, coroutineScope)
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                when {
                    list.loadState.refresh is LoadState.Loading -> {

                    }
                    else -> {
                        items(list.itemCount) {article ->
                            list[article]?.let { ArticleItemForActivity(item = it, this@ArticleActivity) }
                        }
                    }
                }
            }
        }

    }

    @Composable
    fun MenuDrawer(scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
        Column() {
            menuMap.keys.forEach {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            listArticle.setMenuId(it)
                            scaffoldState.drawerState.close()
                        }
                    }
                ) {
                    Text(text = menuMap[it] ?: "우왁끼는 살아있다")
                }
            }
        }
    }

}