package com.janus.aprendiendonumeros.ui.utilities

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.ui.base.BaseActivity
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog

fun ImageView.loadImageFromUrl(url: String) {
    val uri: Uri = Uri.parse(url)
    Glide
        .with(context)
        .load(uri)
        .centerInside()
        .into(this)
}

fun ImageView.loadImageFromUrl(url: String, defaultImage: Int) {
    val uri: Uri = Uri.parse(url)
    Glide
        .with(context)
        .load(uri)
        .error(defaultImage)
        .centerInside()
        .into(this)
}

fun EditText.fieldIsEmpty(mContext: BaseActivity, nameEditText: String) {
    this.error = "Ingresa $nameEditText."
    mContext.showDialogInformation(
        icon = InformationDialog.ICON_ERROR,
        title = "¡Vaya! Campo vacío",
        text = "Parece que se te olvidó ingresar $nameEditText."
    )
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

fun Context.printUser(user: User) {
    val text: String = "\n nickName: ${user.nickName}" +
            "\n gender: ${user.gender}" +
            "\n image: ${user.image}" +
            "\n passwordChild: ${user.passwordChild}" +
            "\n passwordAdult: ${user.passwordAdult}" +
            "\n birthDate: ${user.birthDate}" +
            "\n email: ${user.email}" +
            "\n coins: ${user.coins}"

    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.message(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun Int.isLessThan(number: Int): Boolean {
    return this < number
}

fun Int.isGreaterThan(number: Int): Boolean {
    return this > number
}


