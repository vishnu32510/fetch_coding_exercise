package com.example.myapplication.viewmodel // Replace with your package name

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.myapplication.models.Item
import com.example.myapplication.RetrofitClient

class ItemViewModel : ViewModel() {

    private val _groupedItems = MutableStateFlow<Map<Int, List<Item>>>(emptyMap())
    val groupedItems: StateFlow<Map<Int, List<Item>>> = _groupedItems

    private val _errorMessage = mutableStateOf<String?>(null) //Use mutableStateOf instead of MutableLiveData for Compose
    val errorMessage: State<String?> = _errorMessage

    private val _isLoading = mutableStateOf(false) // Add a loading state
    val isLoading: State<Boolean> = _isLoading

    init {
        loadItems()
    }

    fun toggleFavorite(item: Item) {
        viewModelScope.launch {
            val updatedGroupedItems = _groupedItems.value.toMutableMap()
            updatedGroupedItems.forEach { (listId, items) ->
                updatedGroupedItems[listId] = items.map {
                    if (it.id == item.id) {
                        it.copy(isFavorite = !it.isFavorite)
                    } else {
                        it
                    }
                }
            }
            _groupedItems.value = updatedGroupedItems
        }
    }

    fun loadItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val items = RetrofitClient.instance.getItems()
                val filteredItems = items.filter { !it.name.isNullOrBlank() }

                val grouped = filteredItems.groupBy { it.listId }
                    .toSortedMap()

                val sortedGrouped = grouped.mapValues { (_, items) ->
                    items.sortedBy { it.name }
                }

                _groupedItems.value = sortedGrouped
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}