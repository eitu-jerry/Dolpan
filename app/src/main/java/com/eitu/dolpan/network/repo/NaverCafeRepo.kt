package com.eitu.dolpan.network.repo

import com.eitu.dolpan.dataClass.naver.cafe.Article
import com.eitu.dolpan.network.DolpanResult
import com.eitu.dolpan.network.api.NaverCafeAPI
import com.eitu.dolpan.network.returnResult
import javax.inject.Inject

class NaverCafeRepo @Inject constructor(
    private val api : NaverCafeAPI
) {

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

}