package com.eitu.dolpan.viewModel.memberChat

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eitu.dolpan.dataClass.firestore.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatPagingSource (
    private val fdb : FirebaseFirestore,
    private val owner : String
) : PagingSource<Int, Chat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> {
        try {
            val nextPageNumber = params.key ?: 0

            val chatList = ArrayList<Chat>()

            try {
                val result = fdb.collection("item")
                    .whereEqualTo("owner", owner)
                    .orderBy("date", Query.Direction.ASCENDING)
                    .get()
                    .await()

                result.documents.forEach {
                    it.toObject(Chat::class.java)?.let { chat -> chatList.add(chat) }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return if (chatList.isNotEmpty()) {
                LoadResult.Page(
                    data = chatList,
                    prevKey = null,
                    nextKey = nextPageNumber + 1
                )
            }
            else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e : Exception) {
            throw e
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Chat>): Int? {
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