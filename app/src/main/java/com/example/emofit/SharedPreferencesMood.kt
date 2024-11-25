package com.example.emofit

import android.content.Context
import android.util.Log

object SharedPreferencesMood {
    // Save mood in SharedPreferences
    fun saveMood(context: Context, mood: String) {
        val sharedPreferences = context.getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("currentMood", mood)
        editor.apply()
    }

    // Retrieve mood from SharedPreferences
    fun getSavedMood(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("currentMood", null)
    }


    // Save timer duration based on mood
    fun saveTimerDuration(context: Context, timerDuration: Long) {
        val sharedPreferences = context.getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("timerDuration", timerDuration)
        editor.apply()
    }

    // Retrieve timer duration from SharedPreferences
    fun getSavedTimerDuration(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        // Default value is 60 seconds if not found
        return sharedPreferences.getLong("timerDuration", 60 * 1000) // 60 seconds in milliseconds
    }

    // Determine and save the appropriate timer based on mood
    fun setTimerBasedOnMood(context: Context) {
        val savedMood = getSavedMood(context)
        Log.d("SharedPreferencesMood", "Mood retrieved: $savedMood") // Debug log

        // Check if the mood is null or not expected
        if (savedMood.isNullOrEmpty()) {
            Log.d("SharedPreferencesMood", "No valid mood found, setting default timer (60s)")
        }

        // Retrieve existing timer duration to avoid overwriting it unnecessarily
        val existingTimerDuration = getSavedTimerDuration(context)

        val timerDuration: Long = when (savedMood) {
            "Happy \uD83D\uDE0A" -> 60 * 1000 // 1 minute
            "Sad \uD83D\uDE25", -> 20 * 1000 // 20 seconds
            "Stressed \uD83D\uDE24" -> 30 * 1000 // 40 seconds
            else -> 40 * 1000 // Default to 1 minute
        }
        Log.d("SharedPreferencesMood", "Timer duration set to: $timerDuration milliseconds") // Debug log
        saveTimerDuration(context, timerDuration)
    }
}