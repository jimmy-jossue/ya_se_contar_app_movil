package com.janus.aprendiendonumeros.ui.fragment.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl
import kotlinx.coroutines.delay

class MenuItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val iconStatus: View
    private var name: String = ""
    private val lineBeforeStatus: View
    private val lineAfterStatus: View
    private val btnIcon: ImageButton

    companion object {
        const val STATUS_LOCKED: Int = 0
        const val STATUS_EMPTY: Int = 1
        const val STATUS_INCOMPLETE: Int = 2
        const val STATUS_COMPLETE: Int = 3
        const val POSITION_START: Int = 0
        const val POSITION_MIDDLE: Int = 1
        const val POSITION_END: Int = 2
    }


    init {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.view_menu_item, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MenuItemView,
            0,
            0
        ).apply {
            lineBeforeStatus = view.findViewById(R.id.itemMenu_lineBeforeStatus)
            lineAfterStatus = view.findViewById(R.id.itemMenu_lineAfterStatus)
            btnIcon = view.findViewById(R.id.itemMenu_icon)
            iconStatus = view.findViewById(R.id.itemMenu_status)

            try {
                setPosition(getInteger(R.styleable.MenuItemView_position, 1))
                setStatus(getInteger(R.styleable.MenuItemView_status, STATUS_LOCKED))
            } finally {
                recycle()
            }
        }
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setImage(idImageResources: Int) {
        btnIcon.setBackgroundResource(idImageResources)
        invalidate()
        requestLayout()
    }

    fun setImage(url: String) {
        btnIcon.loadImageFromUrl(url)
        invalidate()
        requestLayout()
    }

    fun setClickListener(listener: OnClickListener) {
        btnIcon.setOnClickListener(listener)
    }

    fun setPosition(position: Int = 2) {
        when (position) {
            POSITION_START -> {
                lineBeforeStatus.visibility = View.INVISIBLE
                lineAfterStatus.visibility = View.VISIBLE
            }
            POSITION_MIDDLE -> {
                lineBeforeStatus.visibility = View.VISIBLE
                lineAfterStatus.visibility = View.VISIBLE
            }
            else -> {
                lineBeforeStatus.visibility = View.VISIBLE
                lineAfterStatus.visibility = View.INVISIBLE
            }
        }
    }

    fun setStatus(status: Int) {
        when (status) {
            STATUS_EMPTY -> {
                iconStatus.setBackgroundResource(R.drawable.ic_status_empty)
                btnIcon.setBackgroundResource(R.drawable.bg_item_menu_button_incomplete)
            }
            STATUS_INCOMPLETE -> {
                iconStatus.setBackgroundResource(R.drawable.ic_status_incomplete)
                btnIcon.setBackgroundResource(R.drawable.bg_item_menu_button_incomplete)
            }
            STATUS_COMPLETE -> {
                iconStatus.setBackgroundResource(R.drawable.ic_status_complete)
                btnIcon.setBackgroundResource(R.drawable.bg_item_menu_button_complete)
            }
            else -> {
                iconStatus.setBackgroundResource(R.drawable.ic_status_locked)
                btnIcon.setBackgroundResource(R.drawable.bg_item_menu_button_locked)
            }
        }
    }


    private suspend fun animationChangeBackground(view: ImageView, iconResources: Int) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_bounce_in))
        delay(100)
        view.setBackgroundResource(iconResources)
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_bounce_out))
    }

    private suspend fun animationChangeBackground(view: ImageView, urlImage: String) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_bounce_in))
        delay(100)
        view.loadImageFromUrl(urlImage)
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_bounce_out))
    }

    suspend fun changeIconButton(iconResources: Int) {
        animationChangeBackground(btnIcon, iconResources)
    }

    suspend fun changeIconButton(urlImage: String) {
        animationChangeBackground(btnIcon, urlImage)
    }
}