package com.example.myparking.utils


import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().fitCenter())
        .into(view)
}

fun loadBackground(view: View, imageUrl: String?) {
    val temp = ImageView(view.context)

    Picasso.get().load(imageUrl).into(temp, object : Callback {
        override fun onSuccess() {
            view.background = temp.drawable
        }

        override fun onError(e: Exception?) {
        }
    })
}
