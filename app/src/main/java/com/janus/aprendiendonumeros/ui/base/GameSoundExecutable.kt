package com.janus.aprendiendonumeros.ui.base

import com.janus.aprendiendonumeros.ui.utilities.SetSound

interface GameSoundExecutable {
    var setSounds: SetSound

    //var soundCorrect: Int
    var soundIncorrect: Int
    var soundCoin: Int

    public fun playSoundCorrect() {
        setSounds.playSound(soundIncorrect)
    }

    public fun playSoundIncorrect() {
        setSounds.playSound(soundIncorrect)
    }

    public fun playSoundCoin() {
        setSounds.playSound(soundCoin)
    }
}