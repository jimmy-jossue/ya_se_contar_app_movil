package com.janus.aprendiendonumeros.ui.utilities

import android.app.Activity
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.IdRes

class Sound(private val context: Activity) {

    private var mediaPlayer: MediaPlayer

    init {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }
    }

    //constructor() : this(context) {
    //
    //}

    constructor(context: Activity, @IdRes id: Int) : this(context) {
        context.let {
            mediaPlayer = MediaPlayer.create(context, id)
            mediaPlayer.prepareAsync()
        }
    }

    //constructor(url: String) {
    //    mediaPlayer = MediaPlayer().apply {
    //        setAudioAttributes(
    //            AudioAttributes.Builder()
    //                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
    //                .build()
    //        )
    //        setDataSource(url)
    //        prepareAsync()
    //    }
    //}

    fun setUrl(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }

    fun play(onEndAction: () -> Unit = {}) {
        mediaPlayer.setOnCompletionListener {
            context.runOnUiThread {
                onEndAction()
            }
        }
        mediaPlayer.start()
    }

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

    fun reset() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
    }
}