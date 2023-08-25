package com.eitu.dolpan.viewModel.cafeArticle

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CafeArticle @Inject constructor(
    private val repo : NaverCafeRepo
) : ViewModel() {

    private var _menuId = MutableLiveData<Int>()
    val menuId : LiveData<Int> = _menuId
    lateinit var list : Flow<PagingData<Article>>

    init {
        viewModelScope.launch {
            list = _menuId.asFlow().flatMapLatest {
                Pager(
                    PagingConfig(pageSize = 30)
                ) {
                    ArticlePagingSource(repo = repo, menuId = it)
                }.flow.cachedIn(viewModelScope)
            }
        }
    }

    fun setMenuId(menuId : Int) {
        this._menuId.value = menuId
    }

}