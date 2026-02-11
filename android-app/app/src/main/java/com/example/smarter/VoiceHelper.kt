package com.example.smartar

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class VoiceHelper(context: Context) {

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    fun speak(message: String) {
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
