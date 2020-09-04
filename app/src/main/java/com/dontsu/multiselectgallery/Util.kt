package com.dontsu.multiselectgallery

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadUrl(url: Uri) {
    context?.let {
        val option = RequestOptions()
            .placeholder(progressDrawable(it))
        Glide.with(context.applicationContext)
            .load(url)
            .apply(option)
            .into(this)
    }
}

fun progressDrawable(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 5f
        start()
    }

}