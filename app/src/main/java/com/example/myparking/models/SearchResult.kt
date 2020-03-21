package com.example.myparking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class SearchResult(
    val title:String,
    val highlightedTitle: String,
    val href:String,
    val type: String,
    val resultType: String,
    val highlightedVicinity: String,
    val vicinity: String,
    val position: @RawValue Array<Double>,
    val category: String,
    val categoryTitle: String,
    val id: String,
    val distance: Int
): Parcelable