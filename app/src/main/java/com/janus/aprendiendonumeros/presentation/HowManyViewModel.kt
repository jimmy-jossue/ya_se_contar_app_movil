package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.Game
import com.janus.aprendiendonumeros.data.model.Rank
import com.janus.aprendiendonumeros.data.remote.FigureDataSource
import com.janus.aprendiendonumeros.data.remote.InstructionHowManyDataSource
import com.janus.aprendiendonumeros.domain.figure.FigureImpl
import com.janus.aprendiendonumeros.domain.figure.FigureProvider
import kotlinx.coroutines.launch

class HowManyViewModel : ViewModel() {

    private val games = mutableListOf<Game>()
    val game = MutableLiveData<Game>()
    var index = MutableLiveData<Int>()
    val isLoading = MutableLiveData<Boolean>()
    val isFinishing = MutableLiveData(false)
    val coins = MutableLiveData<Int>()

    fun onCreate(level: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            coins.postValue(0)
            index.postValue(0)

            val instruction = InstructionHowManyDataSource()
            val mapUrlQuestions = instruction.get(Exercise.NAME_HOW_MANY)
            val figureProvider: FigureProvider = FigureImpl(FigureDataSource())
            val figures = figureProvider.getAll(level)
            val listNumbers = (1..9).toList().shuffled()

            listNumbers.forEach { number ->
                val firstDistraction = setFirstDistraction(level, number, listNumbers)
                val secondDistraction =
                    setSecondDistraction(level, number, firstDistraction, listNumbers)
                val figure = figures[(figures.indices).random()]
                games.add(
                    Game(
                        instructionAudio = mapUrlQuestions[figure.name] ?: "",
                        figure = figure,
                        number = number,
                        firstDistraction = firstDistraction,
                        secondDistraction = secondDistraction
                    )
                )
            }
            game.postValue(games[index.value!!])
            isLoading.postValue(false)
        }
    }

    fun nextNumber() {
        var count: Int = index.value!!
        if (count < games.size - 1) {
            count++
            index.postValue(count)
            game.postValue(games[count])
        }
        if (count > games.size - 1) {
            isFinishing.postValue(true)
        }
    }

    fun addCoins(rewardAmount: Int) {
        val coinsAmount: Int = coins.value!!.plus(rewardAmount)
        coins.postValue(coinsAmount)
    }

    private fun setFirstDistraction(level: String, omittedNumber: Int, listNumber: List<Int>): Int {
        val rank = Rank.getRank(level, omittedNumber, listNumber.size)
        var firstDistraction: Int
        do {
            firstDistraction = (rank.min..rank.max).random()
        } while (firstDistraction == omittedNumber)
        return firstDistraction
    }

    private fun setSecondDistraction(
        level: String,
        omittedNumberOne: Int,
        omittedNumberTwo: Int,
        listNumber: List<Int>,
    ): Int {
        val rank = Rank.getRank(level, omittedNumberOne, listNumber.size)
        var secondDistraction: Int
        do {
            secondDistraction = (rank.min..rank.max).random()
        } while (secondDistraction == omittedNumberOne || secondDistraction == omittedNumberTwo)
        return secondDistraction
    }
}