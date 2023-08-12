package com.eitu.dolpan.viewModel.memberChat

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eitu.dolpan.dataClass.firestore.Chat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatPagingSource (
    private val fdb : FirebaseFirestore,
    private val owner : String
) : PagingSource<Int, Chat>() {

    private var lastDoc : DocumentSnapshot? = null
    private var limit : Long? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> {
        try {
            val nextPageNumber = params.key ?: 0

            Log.d("nextPageNumber", "$nextPageNumber")

            val chatList = ArrayList<Chat>()

            var query = fdb.collection("item")
                .whereEqualTo("owner", owner)
                .orderBy("date", Query.Direction.ASCENDING)

            limit?.let { query = query.limit(it) }
            lastDoc?.let { query = query.startAfter(it) }

            try {
                val result = query.get().await()
                val documents = result.documents

                if (limit == null) limit = 15L

                if (documents.isNotEmpty()) {
                    lastDoc = result.documents.last()
                    documents.forEach {
                        it.toObject(Chat::class.java)?.let { chat -> chatList.add(chat) }
                    }
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