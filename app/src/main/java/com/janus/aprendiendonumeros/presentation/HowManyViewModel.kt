package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janus.aprendiendonumeros.data.model.Figure
import com.janus.aprendiendonumeros.data.remote.FigureDataSource
import com.janus.aprendiendonumeros.domain.figure.FigureImpl
import com.janus.aprendiendonumeros.domain.figure.FigureProvider
import kotlinx.coroutines.launch

class HowManyViewModel : ViewModel() {

    private val figureProvider: FigureProvider = FigureImpl(FigureDataSource())
    private lateinit var figures: List<Figure>

    fun onCreate(level: String) {
        viewModelScope.launch {
            figures = figureProvider.getAll(FigureDataSource.Level.FIRST.toString())
        }
    }


}