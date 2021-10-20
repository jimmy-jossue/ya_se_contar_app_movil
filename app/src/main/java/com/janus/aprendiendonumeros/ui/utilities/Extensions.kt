package com.janus.aprendiendonumeros.ui.utilities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.ui.base.BaseActivity
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import kotlinx.coroutines.delay
import java.util.*

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

suspend fun TextView.addCoins(view: View, addCoins: Int) {
    val waitTime: Long = (800 / addCoins).toLong()
    val setSounds = SetSound(context)
    val soundCoin: Int = setSounds.addSound(R.raw.sound_coin)
    var coins = this.text.toString().toInt()

    for (coin in 1..addCoins) {
        coins++
        this.text = coins.toString()
        setSounds.playSound(soundCoin)
        UIAnimations(context).startAnimation(view, R.anim.fast_zoom)
        delay(waitTime)
    }
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

fun ConstraintLayout.linkViews(idToConstrain: Int, idLink: Int) {
    val constraint = ConstraintSet()

    constraint.connect(idToConstrain,
        ConstraintSet.START,
        idLink,
        ConstraintSet.START)
    constraint.connect(idToConstrain,
        ConstraintSet.END,
        idLink,
        ConstraintSet.END)
    constraint.connect(idToConstrain,
        ConstraintSet.TOP,
        idLink,
        ConstraintSet.TOP)
    constraint.connect(idToConstrain,
        ConstraintSet.BOTTOM,
        idLink,
        ConstraintSet.BOTTOM)

    constraint.applyTo(this)
}

suspend fun ConstraintLayout.animationJumpCoin(
    restrictionButton: AppCompatButton,
    addedCoins: Int,
) {
    val listViews = mutableListOf<AppCompatImageView>()

    for (number in 1..addedCoins) {
        val coin: AppCompatImageView = AppCompatImageView(context).apply {
            this.id = UUID.randomUUID().mostSignificantBits.toInt()
            val size: Int = context.resources.getDimension(R.dimen.img_size_medium).toInt()
            this.layoutParams = ConstraintLayout.LayoutParams(size, size)
            this.setBackgroundResource(R.drawable.ic_coin)
            this.visibility = View.VISIBLE
        }
        this.addView(coin)
        this.linkViews(coin.id, restrictionButton.id)
        UIAnimations(context).startAnimation(coin, R.anim.animation_example)
        listViews.add(coin)
        delay(200)
    }
    delay(800)

    listViews.forEach {
        this.removeView(it)
    }
}

suspend fun ConstraintLayout.animationJumpCoin(
    restrictionButton: AppCompatButton,
    endX: Float,
    endY: Float,
    addedCoins: Int,
) {
    val listViews = mutableListOf<AppCompatImageView>()
    for (number in 1..addedCoins) {
        val coin: AppCompatImageView = AppCompatImageView(context).apply {
            this.id = UUID.randomUUID().mostSignificantBits.toInt()
            val size: Int = context.resources.getDimension(R.dimen.img_size_medium).toInt()
            this.layoutParams = ConstraintLayout.LayoutParams(size, size)
            this.setBackgroundResource(R.drawable.ic_coin)
            this.visibility = View.VISIBLE
        }
        this.addView(coin)
        this.linkViews(coin.id, restrictionButton.id)
        //val translationX = ObjectAnimator.ofFloat(coin, "translationX", coin.x, (endX - coin.x))
        val translationY = ObjectAnimator.ofFloat(coin, "translationY", coin.y, (coin.y - endY))
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationY)//, translationX)
        animatorSet.duration = 700
        animatorSet.interpolator = DecelerateInterpolator(0.5F)
        animatorSet.start()

        listViews.add(coin)
        delay(150)
    }
    delay(1000)

    listViews.forEach {
        this.removeView(it)
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