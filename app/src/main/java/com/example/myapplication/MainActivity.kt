package com.example.myapplication
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.CardDefaults
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.res.painterResource //Add
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import com.example.myapplication.ui.theme.MyApplicationTheme
import coil.compose.AsyncImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

enum class TabPage {
    FAVORITES,
    ALL,
    CATEGORIES
}

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

@Composable
fun MainScreen(itemViewModel: ItemViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(TabPage.ALL) }
    val groupedItems by itemViewModel.groupedItems.collectAsState()
    val errorMessage by itemViewModel.errorMessage
    val isLoading by itemViewModel.isLoading
    val favoriteItemsList = groupedItems.values.flatten().filter { it.isFavorite }
    if (favoriteItemsList.isEmpty() && selectedTab == TabPage.FAVORITES) selectedTab = TabPage.ALL

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator()
        }
    }else{
        Column(
            modifier = Modifier
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
                        tabPage = TabPage.FAVORITES ,
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

@Composable
fun ItemList(items: List<Item>, onFavoriteClick: (Item) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(12.dp)) {
        items(items) { item ->
            ItemRow(item = item, isFavorite = item.isFavorite, onFavoriteClick = { onFavoriteClick(item) })
        }
    }
}

@Composable
fun ItemRow(item: Item, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors( // Use CardDefaults.cardColors
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.image ?: R.drawable.fetch,
                placeholder = painterResource(R.drawable.fetch),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)) {
                Text(text = item.name ?: "N/A",fontWeight = FontWeight.Bold, // Make text bold
                    fontSize = 18.sp)
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    }
}

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

@Composable
fun CategoryItem(listId: Int, items: List<Item>, onFavoriteClick: (Item) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors( // Use CardDefaults.cardColors
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "List $listId", style = MaterialTheme.typography.headlineSmall)
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }
        Column(modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 14.dp)
            .animateContentSize()) {
            if (expanded) {
                items.forEach { item ->
                    ItemRow(item = item, isFavorite = item.isFavorite, onFavoriteClick = {onFavoriteClick(item)})
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}