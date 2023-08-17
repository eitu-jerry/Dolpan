package com.eitu.dolpan.network.repo

import android.util.Log
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.network.DolpanResult
import com.eitu.dolpan.network.api.*
import com.eitu.dolpan.network.returnResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NaverCafeRepo @Inject constructor(
    private val api : NaverCafeAPI,
    private val fdb : FirebaseFirestore
) {

    val memberMap = hashMapOf(
        Pair(wakCafe, "wak"),
        Pair(ineCafe, "ine"),
        Pair(jingCafe, "jing"),
        Pair(lilpaCafe, "lilpa"),
        Pair(jururuCafe, "jururu"),
        Pair(goseguCafe, "gosegu"),
        Pair(vichanCafe, "vichan")
    )

    suspend fun getMenuArticles(menuId : String, offset : Int = 10) : List<Article> {
        var listArticle : List<Article> = emptyList()

        try {
            val result = returnResult(api.getMenuArticles(menuId = menuId, offset = offset))

            when(result) {
                is DolpanResult.Success -> {
                    listArticle = result.data.message.result.articleList
                }
                is DolpanResult.Error -> {
                    result.exception.printStackTrace()
                }
                else -> {

                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
            listArticle = emptyList()
        }

        return listArticle

    }

    suspend fun getMemberArticles(memberId : String, lastArticleTime : Long) : Long {
        try {
            val result = returnResult(api.getMemberArticles(memberKey = memberId))
            Log.d("getMemberArticles", "called")

            when(result) {
                is DolpanResult.Success -> {
                    Log.d("getMemberArticles", "success")
                    val articleList = result.data.message.result.articleList
                        .filter { it.getWriteTime() > lastArticleTime }

                    if (articleList.isEmpty()) {
                        throw Exception("${memberMap[memberId]} has no new article")
                    }

                    Log.d("getMemberArticles", "is not empty ${articleList.size}")

                    articleList.forEach {
                        fdb.collection("item")
                            .add(Chat(
                                owner = memberMap[memberId] ?: "",
                                type = "naverCafe",
                                date = it.getFormattedWriteTime(),
                                title = it.subject,
                                id = it.articleid.toString()
                            ))
                            .addOnSuccessListener {
                                Log.d("getMemberArticles", "success to add")
                            }
                            .addOnFailureListener {
                                Log.d("getMemberArticles", "fail to add")
                            }
                    }

                    return articleList.last().getWriteTime()

                }
                is DolpanResult.Error -> {
                    Log.d("getMemberArticles", "error")
                    result.exception.printStackTrace()
                }
                else -> {

                }
            }
        } catch (e : Exception) {
            Log.d("getMemberArticle", e.message.toString())
        }

        return lastArticleTime
    }

    suspend fun getMemberArticles(memberId : String) {
        try {
            var page = 1
            while (true) {
                val result = returnResult(api.getMemberArticles(memberKey = memberId, page = page++))
                Log.d("getMemberArticles", "called")

                when(result) {
                    is DolpanResult.Success -> {
                        Log.d("getMemberArticles", "success")
                        val articleList = result.data.message.result.articleList
                            .filter { it.getWriteTime() > 1690815600 }

                        if (articleList.isEmpty()) {
                            throw Exception("${memberMap[memberId]} has no new article")
                        }

                        Log.d("getMemberArticles", "is not empty ${articleList.size}")

                        articleList.forEach {
                            Log.d("getArticle", "${it.subject} ${it.getFormattedWriteTime()}")
                            fdb.collection("item")
                                .add(Chat(
                                    owner = memberMap[memberId] ?: "",
                                    type = "naverCafe",
                                    date = it.getFormattedWriteTime(),
                                    title = it.subject,
                                    id = it.articleid.toString()
                                ))
                                .await()
//                                .addOnSuccessListener {
//                                    Log.d("getMemberArticles", "success to add")
//                                }
//                                .addOnFailureListener {
//                                    Log.d("getMemberArticles", "fail to add")
//                                }
                        }
                    }
                    is DolpanResult.Error -> {
                        Log.d("getMemberArticles", "error")
                        result.exception.printStackTrace()
                    }
                    else -> {

                    }
                }
            }

        } catch (e : Exception) {
            Log.d("getMemberArticle", e.message.toString())
        }
    }

}