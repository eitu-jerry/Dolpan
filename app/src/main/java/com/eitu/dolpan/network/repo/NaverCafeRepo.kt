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
                    var articleList = result.data.message.result.articleList
                    Log.d("getMemberArticles", "success ${articleList.size} ${articleList.first().toWriteTimeLong()}")
                    articleList = articleList.filter { it.toWriteTimeLong() > lastArticleTime }

                    if (articleList.isEmpty()) {
                        throw Exception("${memberMap[memberId]} has no new article")
                    }

                    Log.d("getMemberArticles", "is not empty ${articleList.size}")

                    articleList.forEach {
                        fdb.collection("item")
                            .add(Chat(
                                owner = memberMap[memberId] ?: "",
                                type = "naverCafe",
                                date = it.toWriteTimeLong(),
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

                    return articleList.first().toWriteTimeLong()

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

}