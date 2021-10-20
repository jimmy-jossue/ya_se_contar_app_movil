package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janus.aprendiendonumeros.data.remote.TextDataSource
import kotlinx.coroutines.launch

class TextViewModel : ViewModel() {

    fun onCreate() {
        viewModelScope.launch {
            TextDataSource().get("es")
        }
    }
}