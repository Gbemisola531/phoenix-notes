package com.phoenix.phoenixnotes.utils

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import java.util.*

object SpeechUtil {

    fun getSpeechIntent(speechIntentLauncher: ActivityResultLauncher<Intent>) {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Try saying something")

        speechIntentLauncher.launch(speechIntent)
    }
}