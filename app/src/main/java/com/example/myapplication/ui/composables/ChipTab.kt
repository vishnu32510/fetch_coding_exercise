package com.example.myapplication.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.screens.TabPage

@Composable
fun ChipTab(
    selected: Boolean,
    onClick: () -> Unit,
    tabPage: TabPage,
    favoriteItemsList: Int? = null
) {
    Surface(
        color = if (selected) {
            if (tabPage== TabPage.FAVORITES){
                MaterialTheme.colorScheme.primaryContainer }
            else{
                MaterialTheme.colorScheme.surfaceDim}
        } else {
            MaterialTheme.colorScheme.primary
        },
        contentColor = if (selected) {
            if (tabPage== TabPage.FAVORITES){
                MaterialTheme.colorScheme.tertiary }
            else{
                MaterialTheme.colorScheme.primary}
        } else {
            MaterialTheme.colorScheme.secondary
        },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(
            1.dp,
            if (selected) {
                MaterialTheme.colorScheme.surfaceBright
            } else {
                MaterialTheme.colorScheme.secondary
            },),
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            when (tabPage) {
                TabPage.FAVORITES -> {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(horizontal = 2.dp),
                    ){
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Text(
                            text = favoriteItemsList.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                TabPage.ALL -> {
                    Text(
                        text = "All",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                TabPage.CATEGORIES -> {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}