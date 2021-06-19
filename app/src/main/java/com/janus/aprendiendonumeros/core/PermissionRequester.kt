package com.janus.aprendiendonumeros.core

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class PermissionRequester(
    activity: ComponentActivity,
    private val permission: String,
    private val onRationale: () -> Unit = {},
    private val onDenied: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}

    private val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when (isGranted) {
            isGranted -> onGranted
            activity.shouldShowRequestPermissionRationale(permission) -> onRationale
            else -> onDenied
        }
    }

    fun runWithPermissionGranted(action: () -> Unit) {
        onGranted = action
        permissionLauncher.launch(permission)
    }


}