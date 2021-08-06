package com.janus.aprendiendonumeros.ui.utilities

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String) {
    val uri: Uri = Uri.parse(url)
    Glide
        .with(context)
        .load(uri)
        .centerInside()
        .into(this)
}

fun Fragment.goTo(@IdRes idAction: Int) {
    findNavController().navigate(idAction)
}

fun Fragment.goTo(navDirection: NavDirections) {
    findNavController().navigate(navDirection)
}

fun ViewGroup.positionIsRepeated(x: Int, y: Int, size: Int): Boolean {
    var isRepeated = false
    for (view: View in this.children) {
        if (x >= (view.x - size) && x <= (view.x + size) &&
            y >= (view.y - size) && y <= (view.y + size)
        ) {
            isRepeated = true
            break
        }
    }
    return isRepeated
}

fun ViewGroup.removeViews() {
    if (this.childCount > 0) {
        this.removeAllViews()
        this.requestLayout()
        this.invalidate()
    }
}


