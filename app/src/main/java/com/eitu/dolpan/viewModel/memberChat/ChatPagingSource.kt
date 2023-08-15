package com.eitu.dolpan.viewModel.memberChat

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.viewModel.memberChat.ChatPagingSource.PagingKey
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ChatPagingSource (
    private val fdb : FirebaseFirestore,
    private val owner : String
) : PagingSource<PagingKey, Chat>() {

    override suspend fun load(params: LoadParams<PagingKey>): LoadResult<PagingKey, Chat> {
        try {
            var query = fdb.collection("item")
                .whereEqualTo("owner", owner)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())

            val key = params.key

            query = when(key) {
                is PagingKey.PrevKey -> {
                    query.endBefore(key.endBefore)
                }
                is PagingKey.NextKey -> {
                    query.startAfter(key.startAfter)
                }
                null -> query
            }

            val result = query.get().await()
            val documents = result.documents
            val chatList = result.toObjects(Chat::class.java)

            val firstDoc = documents.firstOrNull()
            val lastDoc = documents.lastOrNull()

            return LoadResult.Page(
                data = chatList,
                prevKey = if (firstDoc != null) PagingKey.PrevKey(firstDoc) else null,
                nextKey = if (lastDoc != null) PagingKey.NextKey(lastDoc) else null
            )
        } catch (e : Exception) {
            throw e
        }
    }

    override fun getRefreshKey(state: PagingState<PagingKey, Chat>): PagingKey? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            //anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            null
        }
    }

    sealed class PagingKey {
        data class PrevKey(val endBefore : DocumentSnapshot) : PagingKey()
        data class NextKey(val startAfter : DocumentSnapshot) : PagingKey()
    }

}