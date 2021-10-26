package com.janus.aprendiendonumeros.ui.animation.coins

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import com.janus.aprendiendonumeros.R
import java.util.*

class Coin(context: Context) : AppCompatImageView(context) {

    init {
        setImageResource(R.drawable.ic_coin)
    }

    companion object {
        fun addTo(
            container: ConstraintLayout,
            customWidth: Int = WRAP_CONTENT,
            customHeight: Int = WRAP_CONTENT,
        ): Coin {
            val coin = Coin(container.context)
            coin.id = UUID.randomUUID().mostSignificantBits.toInt()
            coin.layoutParams = ViewGroup.LayoutParams(customWidth, customHeight)
            coin.bringToFront()
            container.addView(coin)
            return coin
        }
    }
}