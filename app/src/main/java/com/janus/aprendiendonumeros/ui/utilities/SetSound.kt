package com.janus.aprendiendonumeros.ui.utilities

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SetSound(private val context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder().apply {
        setAudioAttributes(AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )
        setMaxStreams(20)
    }.build()

    fun addSound(idSound: Int): Int {
        return soundPool.load(context, idSound, 1)
    }

    fun playSound(soundID: Int) = soundPool.play(soundID, 1F, 1F, 1, 0, 1F)
}