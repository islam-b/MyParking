package com.example.myparking.utils

import androidx.databinding.BindingAdapter
import com.appyvet.materialrangebar.RangeBar

@BindingAdapter("tickEnd")
fun setTickEnd(rangeBar : RangeBar, value: Float) {
    rangeBar.tickEnd = value
}

@BindingAdapter("tickStart")
fun setTickStart(rangeBar : RangeBar, value: Float) {
    rangeBar.tickStart = value
}
