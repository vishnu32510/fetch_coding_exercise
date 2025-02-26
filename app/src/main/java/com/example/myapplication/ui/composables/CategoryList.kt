package com.example.myapplication.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.myapplication.models.Item

@Composable
fun CategoryList(groupedItems: Map<Int, List<Item>>, onFavoriteClick: (Item) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp)) {
        groupedItems.forEach { (listId, items) ->
            item {
                CategoryItem(listId = listId, items = items, onFavoriteClick = onFavoriteClick)
            }
        }
    }
}