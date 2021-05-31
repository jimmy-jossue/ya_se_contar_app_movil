package com.janus.aprendiendonumeros.ui.fragment.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.janus.aprendiendonumeros.R


class MenuItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val iconStatus: View
    private val lineBeforeStatus: View
    private val lineAfterStatus: View
    private val btnIcon: ImageButton

    companion object {
        const val STATUS_LOCKED = 0
        const val STATUS_EMPTY = 1
        const val STATUS_INCOMPLETE = 2
        const val STATUS_COMPLETE = 3
    }


    init {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.view_menu_item, this, true)

        context.theme.obtainStyledAttributes(
                attrs, R.styleable.MenuItemView,
                0, 0).apply {

            lineBeforeStatus = view.findViewById(R.id.itemMenu_lineBeforeStatus)
            lineAfterStatus = view.findViewById(R.id.itemMenu_lineAfterStatus)
            btnIcon = view.findViewById(R.id.itemMenu_icon)
            iconStatus = view.findViewById(R.id.itemMenu_status)

            try {
                initPosition(getInteger(R.styleable.MenuItemView_position, 1))
                setStatus(getInteger(R.styleable.MenuItemView_status, STATUS_LOCKED))
            } finally {
                recycle()
            }

        }
    }

    fun setImage(idImageResources: Int){
        btnIcon.setBackgroundResource(idImageResources)
        invalidate()
        requestLayout()
    }

    fun setClickListener(listener: View.OnClickListener){
        btnIcon.setOnClickListener(listener)
    }

    private fun initPosition(position: Int) {
        when (position) {
            0 -> {
                lineBeforeStatus.visibility = View.INVISIBLE
                lineAfterStatus.visibility = View.VISIBLE
            }
            1 -> {
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

    private fun animationChangeBackground(view: View, iconResources: Int){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_bounce_in))
        Thread.sleep(100)
        view.setBackgroundResource(iconResources)
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_bounce_out))
    }

    fun changeIconButton(iconResources: Int){
        animationChangeBackground(btnIcon, iconResources)
    }

    fun setStatus(){

    }

}