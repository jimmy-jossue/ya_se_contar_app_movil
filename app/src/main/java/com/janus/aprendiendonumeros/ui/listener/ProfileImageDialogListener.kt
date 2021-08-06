package com.janus.aprendiendonumeros.ui.listener

import android.graphics.drawable.Drawable
import com.janus.aprendiendonumeros.ui.dialog.ProfileImageDialog

interface ProfileImageDialogListener {
    fun onSelectionImage(dialog: ProfileImageDialog): Drawable
}