package com.janus.aprendiendonumeros.ui.activity

import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        hideSystemUI()
    }
}