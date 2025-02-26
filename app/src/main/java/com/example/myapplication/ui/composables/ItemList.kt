package com.example.myapplication.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.myapplication.models.Item

@Composable
fun ItemList(items: List<Item>, onFavoriteClick: (Item) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(12.dp)) {
        items(items) { item ->
            ItemRow(item = item, isFavorite = item.isFavorite, onFavoriteClick = { onFavoriteClick(item) })
        }
    }
}