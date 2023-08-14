package com.eitu.dolpan.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eitu.dolpan.dataClass.naver.cafe.Article
import com.eitu.dolpan.network.repo.NaverCafeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CafeNotice @Inject constructor(
    private val repo : NaverCafeRepo
) : ViewModel() {

    val listNotice = mutableStateListOf<Article>()
    val listIsdNotice = mutableStateListOf<Article>()

    init {
        viewModelScope.launch {
            val noticeResult = withContext(Dispatchers.IO) {
                //카페 공지사항
                repo.getMenuArticles("24", 12)
            }
            val isdNoticeResult = withContext(Dispatchers.IO) {
                //이세돌 공지사항
                repo.getMenuArticles("345", 12)
            }
            listNotice.addAll(noticeResult)
            listIsdNotice.addAll(isdNoticeResult)
        }
    }

}