package com.example.myapplication.services

import com.example.myapplication.models.Item
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
    suspend fun getItems(): List<Item>
}