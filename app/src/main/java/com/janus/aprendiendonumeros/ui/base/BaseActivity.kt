package com.janus.aprendiendonumeros.ui.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets.Type
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.janus.aprendiendonumeros.ui.dialog.ConfirmBeingAdultDialog
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.dialog.ProfileImageDialog
import com.janus.aprendiendonumeros.ui.listener.NotifyDrawableListener
import com.janus.aprendiendonumeros.ui.listener.NotifyQuestionListener

abstract class BaseActivity : AppCompatActivity(), ProfileImageDialog.Listener,
    ConfirmBeingAdultDialog.Listener {

    private lateinit var drawableListenerTarget: NotifyDrawableListener
    private lateinit var questionListenerTarget: NotifyQuestionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    abstract fun getLayout(): Int
    abstract fun initUI()

    protected fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.insetsController?.let { controller ->
                controller.hide(Type.statusBars() or Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    fun setDrawableTarget(drawableListenerTarget: NotifyDrawableListener) {
        this.drawableListenerTarget = drawableListenerTarget
    }

    fun setQuestionTarget(questionListenerTarget: NotifyQuestionListener) {
        this.questionListenerTarget = questionListenerTarget
    }

    override fun onSelectionImage(dialog: ProfileImageDialog) {
        drawableListenerTarget.update(dialog.getDrawable())
        dialog.dismiss()
    }

    fun showDialogProfileImage() {
        val dialog = ProfileImageDialog()
        dialog.show(supportFragmentManager, null)
    }

    fun showDialogInformation(icon: String, title: String, text: String) {
        val arguments: Bundle = Bundle().apply {
            putString(InformationDialog.ARGUMENT_ICON, icon)
            putString(InformationDialog.ARGUMENT_TITLE, title)
            putString(InformationDialog.ARGUMENT_TEXT, text)
        }
        val dialog = InformationDialog()
        dialog.arguments = arguments
        dialog.show(supportFragmentManager, null)
    }

    fun showDialogConfirmBeingAdult() {
        val dialog = ConfirmBeingAdultDialog()
        dialog.show(supportFragmentManager, null)
    }

    override fun isPositive(dialog: ConfirmBeingAdultDialog) {
        questionListenerTarget.update(dialog.isCorrect)
        dialog.dismiss()
    }
}