package com.eitu.dolpan.viewModel.cafeArticle

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.network.repo.NaverCafeRepo
import com.eitu.dolpan.viewModel.memberChat.ChatPagingSource.PagingKey
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ArticlePagingSource (
    private val repo : NaverCafeRepo,
    private val menuId : Int?
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            val key = params.key ?: 1

            val listArticle = repo.getMenuArticles(menuId = menuId, page = key, offset = params.loadSize)

            return LoadResult.Page(
                data = listArticle,
                nextKey = if (listArticle.isNotEmpty()) key + 1 else null,
                prevKey = null
            )

        } catch (e : Exception) {
            throw e
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}