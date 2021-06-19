package com.janus.aprendiendonumeros.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.janus.aprendiendonumeros.ui.dialog.ProfileImageDialog

abstract class BaseActivity : AppCompatActivity() {

    protected val mContext: Context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initUI()
    }

    abstract fun getLayout(): Int

    abstract fun initUI()

    public fun openDialogProfileImage() {
        val dialog: ProfileImageDialog = ProfileImageDialog()
        dialog.show(supportFragmentManager, null)
    }

}