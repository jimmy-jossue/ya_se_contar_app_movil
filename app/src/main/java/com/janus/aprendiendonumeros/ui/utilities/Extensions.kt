package com.janus.aprendiendonumeros.ui.utilities

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String) {
    val uri: Uri = Uri.parse(url)
    Glide
        .with(context)
        .load(uri)
        .centerInside()
//        .placeholder(R.drawable.btn_close)
        .into(this)
}

fun ViewGroup.removeContainerViews() {
    if (this.childCount > 0) this.removeAllViews()
}
