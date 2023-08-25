package com.eitu.dolpan.viewModel.cafeArticle

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.network.repo.NaverCafeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CafeArticle @Inject constructor(
    private val repo : NaverCafeRepo
) : ViewModel() {

    private var menuId = MutableLiveData<Int>()
//    val list = mutableStateListOf<Article>()
//
//    init {
//        viewModelScope.launch {
//            menuId.asFlow().collect {
//                val result = withContext(Dispatchers.IO) {
//                    if (it != -1) {
//                        repo.getMenuArticles(menuId = it)
//                    }
//                    else {
//                        repo.getMenuArticles()
//                    }
//                }
//                list.apply {
//                    clear()
//                    addAll(result)
//                }
//            }
//        }
//    }

    lateinit var list : Flow<PagingData<Article>>

    init {
        viewModelScope.launch {
            menuId.asFlow().collect {
                list = Pager(
                    PagingConfig(pageSize = 30)
                ) {
                    ArticlePagingSource(repo = repo, menuId = it)
                }.flow.cachedIn(viewModelScope)
            }
        }
    }

    fun setMenuId(menuId : Int) {
        this.menuId.value = menuId
    }

}