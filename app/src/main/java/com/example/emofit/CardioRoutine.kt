package com.example.emofit

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class CardioRoutine : AppCompatActivity() {

    private lateinit var timerBurpees: TextView
    private lateinit var timerMountain: TextView
    private lateinit var startBurpees: Button
    private lateinit var startMountain: Button

    private var burpeesTimer: CountDownTimer? = null
    private var mountainTimer: CountDownTimer? = null

    private var workoutDuration: Long = 60000L // Default duration, can be overridden based on mood
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cardio_routine)

        // Initialize TextViews and Buttons
        timerBurpees = findViewById(R.id.timerBurpees)
        timerMountain = findViewById(R.id.timerMountain)

        startBurpees = findViewById(R.id.startBurpeesButton)
        startMountain = findViewById(R.id.startMountainButton)

        // Set the workout duration based on the saved mood
        setWorkoutDurationBasedOnMood()

        // Set click listeners for each start button
        startBurpees.setOnClickListener { startBurpeesTimer() }
        startMountain.setOnClickListener { startMountainTimer() }

        // Link the back button to the function
        val backButton: NeumorphButton = findViewById(R.id.btnBackToWorkout)
        backButton.setOnClickListener {
            finish() // Closes the WorkoutMenuActivity and returns to the previous screen
        }
    }

    private fun setWorkoutDurationBasedOnMood() {
        /// Set the timer based on the saved mood and update the timer duration
        SharedPreferencesMood.setTimerBasedOnMood(this)

        // Retrieve the saved timer duration directly after setting it
        workoutDuration = SharedPreferencesMood.getSavedTimerDuration(this)
        Log.d("CardioRoutine", "Workout duration set to $workoutDuration milliseconds based on mood")
        // Format and display the initial time for each timer TextView
        val formattedTime = formatTime(workoutDuration)
        timerBurpees.text = formattedTime
        timerMountain.text = formattedTime
    }

    private var isTimerRunning = false // Flag to track if a timer is currently active


    private fun startBurpeesTimer() {
        if (isTimerRunning) {
            Toast.makeText(this, "Please finish the current workout timer first.", Toast.LENGTH_SHORT).show()
            return
        }
        isTimerRunning = true
        startBurpees.isEnabled = false


        burpeesTimer = object : CountDownTimer(workoutDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerBurpees.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                timerBurpees.text = "00:00"
                //Toast.makeText(this@BackRoutine, "Pull Ups Completed! You earned a badge!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                startBurpees.isEnabled = true
                burpeesTimer = null

                val sharedPreferences = getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
                val badgeCount = sharedPreferences.getInt("badgeCount", 0)
                val currentPoints = sharedPreferences.getInt("currentPoints", 0)

                val earnedBadge = currentPoints + 20 >= 100 && badgeCount < 3 // Check if a badge will be earned

                if (earnedBadge) {
                    incrementPoints(20) // This will handle the badge unlocking and sound
                } else {
                    incrementPoints(20) // Update points without earning a badge
                    // Play workout completion sound
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this@CardioRoutine, R.raw.workoutcomplete)
                    mediaPlayer?.start()
                }
            }
        }.start()
    }

    private fun startMountainTimer() {
        if (isTimerRunning) {
            Toast.makeText(this, "Please finish the current workout timer first.", Toast.LENGTH_SHORT).show()
            return
        }
        isTimerRunning = true
        startMountain.isEnabled = false

        mountainTimer = object : CountDownTimer(workoutDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerMountain.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                timerMountain.text = "00:00"
                //Toast.makeText(this@BackRoutine, "Chin Ups Completed! You earned a badge!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                startMountain.isEnabled = true
                mountainTimer = null

                val sharedPreferences = getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
                val badgeCount = sharedPreferences.getInt("badgeCount", 0)
                val currentPoints = sharedPreferences.getInt("currentPoints", 0)

                val earnedBadge = currentPoints + 20 >= 100 && badgeCount < 3 // Check if a badge will be earned

                if (earnedBadge) {
                    incrementPoints(20) // This will handle the badge unlocking and sound
                } else {
                    incrementPoints(20) // Update points without earning a badge
                    // Play workout completion sound
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this@CardioRoutine, R.raw.workoutcomplete)
                    mediaPlayer?.start()
                }
            }
        }.start()
    }

    private fun incrementPoints(points: Int) {
        val sharedPreferences = getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        val currentPoints = sharedPreferences.getInt("currentPoints", 0)
        val badgeCount = sharedPreferences.getInt("badgeCount", 0)

        if (badgeCount >= 3) {
            // User already earned all badges, stop adding points
            Toast.makeText(this, "You have unlocked all badges! Great job!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedPoints = currentPoints + points

        if (updatedPoints >= 100) {
            // Earn a new badge
            val newBadgeCount = badgeCount + 1
            sharedPreferences.edit().putInt("badgeCount", newBadgeCount).apply()
            sharedPreferences.edit().putInt("currentPoints", 0).apply() // Reset progress bar
            Toast.makeText(this, "Congratulations! You've earned a badge!", Toast.LENGTH_SHORT).show()
            // Play badge sound
            mediaPlayer?.release() // Release the current MediaPlayer instance
            mediaPlayer = MediaPlayer.create(this@CardioRoutine, R.raw.badgeunlock)
            mediaPlayer?.start()
        } else {
            // Update progress bar points
            sharedPreferences.edit().putInt("currentPoints", updatedPoints).apply()
            Toast.makeText(this, "You earned $points points! Total: $updatedPoints/100", Toast.LENGTH_SHORT).show()
        }

        Log.d("BackRoutine", "Current Points: $updatedPoints, Badge Count: $badgeCount")
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        burpeesTimer?.cancel()
        mountainTimer?.cancel()
        isTimerRunning = false // Reset flag if activity is destroyed
    }
}
