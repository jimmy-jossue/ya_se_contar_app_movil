package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janus.aprendiendonumeros.data.model.Figure
import com.janus.aprendiendonumeros.data.model.Rank
import com.janus.aprendiendonumeros.data.remote.FigureDataSource
import com.janus.aprendiendonumeros.domain.figure.FigureImpl
import com.janus.aprendiendonumeros.domain.figure.FigureProvider
import kotlinx.coroutines.launch

class HowManyViewModel : ViewModel() {

    private val games = mutableListOf<GameHowMany>()
    val game = MutableLiveData<GameHowMany>()
    var index = MutableLiveData<Int>()
    val isLoading = MutableLiveData<Boolean>()
    val isFinishing = MutableLiveData(false)
    val coins = MutableLiveData<Int>()
    val mapUrlQuestions = mutableMapOf<String, String>()

    fun onCreate(level: String) {
        viewModelScope.launch {
            mapUrlQuestions["bee"] =
                "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/sounds%2FhowMany%2FhowManyBee.mp3?alt=media&token=141ed1da-43b9-410f-acee-5f1e92c3092a"
            mapUrlQuestions["kite"] =
                "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/sounds%2FhowMany%2FhowManyKite.mp3?alt=media&token=cc5f573f-0424-48a2-800f-7409d3d1dfcf"
            isLoading.postValue(true)
            coins.postValue(0)
            index.postValue(0)

            val figureProvider: FigureProvider = FigureImpl(FigureDataSource())
            val figures = figureProvider.getAll(level)
            val listNumbers = (1..9).toList().shuffled()

            listNumbers.forEach { number ->
                val firstDistraction = setFirstDistraction(level, number, listNumbers)
                val secondDistraction =
                    setSecondDistraction(level, number, firstDistraction, listNumbers)
                val figure = figures[(figures.indices).random()]
                games.add(
                    GameHowMany(
                        questionAudio = mapUrlQuestions[figure.name] ?: "",
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
        if (count >= games.size - 1) {
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

data class GameHowMany(
    val questionAudio: String = "",
    val figure: Figure,
    val number: Int = 0,
    val firstDistraction: Int = 0,
    val secondDistraction: Int = 0,
)