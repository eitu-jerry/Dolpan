package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.view.composable.ArticleItem
import com.eitu.dolpan.view.composable.ArticleItemForActivity
import com.eitu.dolpan.viewModel.cafeArticle.CafeArticle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleActivity : BaseActivity() {

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

        val listArticle : CafeArticle by viewModels()
        listArticle.setMenuId(intent.getIntExtra("menuId", -1))
        val list = listArticle.list.collectAsLazyPagingItems()

        LazyColumn() {
            items(list.itemCount) {article ->
                list[article]?.let { ArticleItemForActivity(item = it, this@ArticleActivity) }
            }

        }

    }

}