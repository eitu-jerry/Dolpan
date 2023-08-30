package com.eitu.dolpan.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.naver.menu.Article
import com.eitu.dolpan.dataClass.naver.sideMenu.Menu
import com.eitu.dolpan.network.repo.NaverCafeRepo
import com.eitu.dolpan.view.base.BaseActivity
import com.eitu.dolpan.view.base.BaseFragment
import com.eitu.dolpan.view.composable.ArticleItemForActivity
import com.eitu.dolpan.view.composable.MenuBar
import com.eitu.dolpan.view.composable.NoticeArticle
import com.eitu.dolpan.viewModel.CafeNotice
import com.eitu.dolpan.viewModel.cafeArticle.CafeArticle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : BaseFragment() {

    @Inject lateinit var repo : NaverCafeRepo

    private val listArticle : CafeArticle by viewModels()
    private val cafeNotice : CafeNotice by viewModels()

    private val sideMenu = mutableStateListOf<Menu>()
    private val selectedMenu = MutableLiveData<Menu>()

    private val activity : BaseActivity by lazy {
        requireActivity() as BaseActivity
    }

    companion object {
        fun newInstance() : ArticleFragment {
            return ArticleFragment()
        }
    }

    override fun setBinding(inflater: LayoutInflater): View {
        selectedMenu.observe(activity) {
            listArticle.setMenuId(it.menuId)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                App()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            sideMenu.addAll(repo.getSideMenu())
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    private fun App() {

        listArticle.setMenuId(-1)

        val list = listArticle.list.collectAsLazyPagingItems()

        val scaffoldState = rememberScaffoldState()
        val listState = rememberLazyListState()
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        var currentMenu by remember {
            mutableStateOf("전체 게시글")
        }
        selectedMenu.observe(activity) {
            currentMenu = it.menuName
        }
        
        Scaffold(
            topBar = {
                MenuBar(
                    listArticle = listArticle,
                    selectedMenu = selectedMenu,
                    scaffoldState = scaffoldState,
                    listState = listState,
                    coroutineScope = coroutineScope
                )
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                MenuDrawer(scaffoldState.drawerState, pagerState, listState, coroutineScope)
            }
        ) {
            Column() {
                TabRow(selectedTabIndex = pagerState.currentPage) {
                    Tab(
                        text = { Text(text = currentMenu) },
                        selected = pagerState.currentPage == 0,
                        onClick = {
                            coroutineScope.launch {
                                if(pagerState.currentPage == 0) {
                                    listState.scrollToItem(0)
                                }
                                else {
                                    pagerState.scrollToPage(0)
                                }

                            }
                        }
                    )
                    Tab(
                        text = { Text(text = "공지사항") },
                        selected = pagerState.currentPage == 1,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(1)
                            }
                        }
                    )
                }
                HorizontalPager(
                    pageCount = 2,
                    state = pagerState,
                    userScrollEnabled = false
                ) { page ->
                    when(page) {
                        0 -> ArticleLayout(list, listState = listState, paddingValues = it)
                        1 -> NoticeLayout(paddingValues = it , pagerState = pagerState, coroutineScope = coroutineScope)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MenuDrawer(drawerState: DrawerState, pagerState: PagerState, listState: LazyListState, coroutineScope: CoroutineScope) {

        fun compareAndSet(it : Menu) {
            if (selectedMenu.value != it) {
                selectedMenu.value = it
            }
            coroutineScope.launch {
                listState.scrollToItem(0)
            }
        }

        LazyColumn(contentPadding = PaddingValues(10.dp)) {
            items(sideMenu) {
                if (it.menuType == "F") {
                    Text(
                        text = it.menuName, 
                        fontSize = 17.sp, 
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )
                }
                else if (it.menuType == "B") {
                    Text(
                        text = if (it.indent) {
                            "ㄴ " + it.menuName
                        }
                        else {
                            it.menuName
                        },
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.textLightGray),
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    drawerState.close()
                                    pagerState.scrollToPage(0)
                                }
                                compareAndSet(it)
                            }
                            .padding(top = 10.dp, bottom = 10.dp)
                    )
                }
                else {

                }
            }
        }
    }

    @Composable
    private fun ArticleLayout(list : LazyPagingItems<Article>,listState : LazyListState, paddingValues: PaddingValues) {

        LazyColumn(
            state = listState,
            modifier = Modifier.padding(paddingValues)
        ) {
            when {
                list.loadState.refresh is LoadState.Loading -> {

                }
                else -> {
                    items(list.itemCount) {article ->
                        list[article]?.let { ArticleItemForActivity(item = it, activity) }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun NoticeLayout(paddingValues: PaddingValues, pagerState: PagerState, coroutineScope: CoroutineScope) {

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.lightGray))
                ) {
                    NoticeArticle(title = "카페 공지사항", list = cafeNotice.listNotice, activity, menuId = 24, pagerState = pagerState, selectedMenu = selectedMenu, coroutineScope = coroutineScope)
                    NoticeArticle(title = "이세돌 공지사항", list = cafeNotice.listIsdNotice, activity, menuId = 345, pagerState = pagerState, selectedMenu = selectedMenu, coroutineScope = coroutineScope)
                }
            }
        }
    }

}