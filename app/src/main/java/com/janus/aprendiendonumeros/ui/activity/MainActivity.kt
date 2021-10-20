package com.janus.aprendiendonumeros.ui.activity

import androidx.activity.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.presentation.TextViewModel
import com.janus.aprendiendonumeros.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private val textViewModel: TextViewModel by viewModels()

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        hideSystemUI()
        textViewModel.onCreate()
    }
}