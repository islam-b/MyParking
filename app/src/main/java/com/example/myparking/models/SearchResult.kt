package com.example.myparking.models

data class SearchResult(
    val title:String,
    val highlightedTitle: String,
    val href:String,
    val type: String,
    val resultType: String,
    val highlightedVicinity: String,
    val vicinity: String,
    val position: Array<Double>,
    val category: String,
    val categoryTitle: String,
    val id: String,
    val distance: Int
)