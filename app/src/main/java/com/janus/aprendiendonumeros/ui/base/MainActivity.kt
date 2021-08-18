package com.janus.aprendiendonumeros.ui.base

import com.janus.aprendiendonumeros.R

class MainActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        hideSystemUI()
    }
}