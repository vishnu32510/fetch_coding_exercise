package com.example.myapplication.models // Replace with your package name

data class Item(
    val id: Int,
    val listId: Int,
    val name: String?,
    val image: String? = null,
    var isFavorite: Boolean = false
)