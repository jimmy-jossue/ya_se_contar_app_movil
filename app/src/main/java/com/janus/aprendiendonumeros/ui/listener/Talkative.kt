package com.janus.aprendiendonumeros.ui.listener

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.LANG_MISSING_DATA
import android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED
import android.speech.tts.UtteranceProgressListener
import java.util.*

class Talkative(context: Context) : TextToSpeech.OnInitListener {

    private val textToSpeech: TextToSpeech
    private var canSpeak: Boolean

    init {
        canSpeak = false
        textToSpeech = TextToSpeech(context, this)
        textToSpeech.setPitch(1f)
        textToSpeech.setSpeechRate(0.75f)
    }

    // overwritten method of TextToSpeech.OnInitListener
    override fun onInit(status: Int) {
        canSpeak = when (status) {
            TextToSpeech.SUCCESS -> {
                val langSupportCode = textToSpeech.setLanguage(Locale.getDefault())
                val isLangMissingData = (langSupportCode != LANG_MISSING_DATA)
                val isLangSupport = (langSupportCode != LANG_NOT_SUPPORTED)

                (isLangMissingData && isLangSupport)
            }
            else -> false
        }
    }

    fun speakOut(
        text: String,
        onStartAction: () -> Unit = {},
        onEndAction: () -> Unit = {},
        onErrorAction: () -> Unit = {},
    ) {
        class Listener : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) = onStartAction()
            override fun onError(utteranceId: String) = onErrorAction()
            override fun onDone(utteranceId: String) = onEndAction()
        }

        if (canSpeak) {
            textToSpeech.setOnUtteranceProgressListener(Listener())
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    fun onDestroy() = textToSpeech.shutdown()
}