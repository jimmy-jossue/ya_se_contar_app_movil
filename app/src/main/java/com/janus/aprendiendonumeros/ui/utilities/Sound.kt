package com.janus.aprendiendonumeros.ui.utilities

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.IdRes

class Sound {
    private var mediaPlayer: MediaPlayer

    constructor(context: Context?, @IdRes id: Int) {
        context.let {
            mediaPlayer = MediaPlayer.create(context, id)
            mediaPlayer.prepareAsync()
        }
    }

    constructor(url: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
        }
    }

    fun play() = mediaPlayer.start()

    fun playLoop() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    fun resume() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }
}