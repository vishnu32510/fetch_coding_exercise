package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.composables.CategoryList
import com.example.myapplication.ui.composables.ChipTab
import com.example.myapplication.ui.composables.ItemList
import com.example.myapplication.viewmodel.ItemViewModel

enum class TabPage {
    FAVORITES,
    ALL,
    CATEGORIES
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(TabPage.ALL) }
    val groupedItems by itemViewModel.groupedItems.collectAsState()
    val errorMessage by itemViewModel.errorMessage
    val isLoading by itemViewModel.isLoading
    val favoriteItemsList = groupedItems.values.flatten().filter { it.isFavorite }
    if (favoriteItemsList.isEmpty() && selectedTab == TabPage.FAVORITES) selectedTab = TabPage.ALL

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator()
        }
    }else{
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                indicator = {},
                divider = {  },
                modifier = Modifier.wrapContentWidth(align = Alignment.Start)
            ) {
                if (favoriteItemsList.isNotEmpty()){
                    ChipTab(
                        selected = selectedTab == TabPage.FAVORITES,
                        onClick = { selectedTab = TabPage.FAVORITES },
                        tabPage = TabPage.FAVORITES,
                        favoriteItemsList = favoriteItemsList.size
                    )
                }
                ChipTab(
                    selected = selectedTab == TabPage.ALL,
                    onClick = { selectedTab = TabPage.ALL },
                    tabPage = TabPage.ALL // Pass TabPage
                )
                ChipTab(
                    selected = selectedTab == TabPage.CATEGORIES,
                    onClick = { selectedTab = TabPage.CATEGORIES },
                    tabPage = TabPage.CATEGORIES // Pass TabPage
                )
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }, label = ""
            ){
                    targetState ->  when (targetState) {
                TabPage.FAVORITES -> {
                    ItemList(items = favoriteItemsList,
                        onFavoriteClick = { item ->
                            itemViewModel.toggleFavorite(item)
                        }
                    )
                }
                TabPage.ALL -> {
                    val allItems = groupedItems.values.flatten()
                    ItemList(items = allItems,
                        onFavoriteClick = { item ->
                            itemViewModel.toggleFavorite(item)
                        }
                    )
                }
                TabPage.CATEGORIES -> {
                    CategoryList(groupedItems = groupedItems,
                        onFavoriteClick = { item ->
                            itemViewModel.toggleFavorite(item)
                        }
                    )
                }
            }
            }
        }
    }
}