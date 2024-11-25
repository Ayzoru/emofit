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

class LegRoutine : AppCompatActivity() {

    private lateinit var timerSquats: TextView
    private lateinit var timerLunges: TextView
    private lateinit var timerBulgarian: TextView
    private lateinit var startSquatsButton: Button
    private lateinit var startLungesButton: Button
    private lateinit var startBulgarian: Button

    private var squatsTimer: CountDownTimer? = null
    private var lungesTimer: CountDownTimer? = null
    private var bulgarianTimer: CountDownTimer? = null

    private var workoutDuration: Long = 60000L // Default duration, can be overridden based on mood
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leg_routine)

        // Initialize TextViews and Buttons
        timerSquats = findViewById(R.id.timerSquats)
        timerLunges = findViewById(R.id.timerLunges)
        timerBulgarian = findViewById(R.id.timerBulgarian)

        startSquatsButton = findViewById(R.id.startSquatsButton)
        startLungesButton = findViewById(R.id.startLungesButton)
        startBulgarian = findViewById(R.id.startBulgarian)

        // Set the workout duration based on the saved mood
        setWorkoutDurationBasedOnMood()

        // Set click listeners for each start button
        startSquatsButton.setOnClickListener { startPushUpTimer() }
        startLungesButton.setOnClickListener { startInclinePushUpTimer() }
        startBulgarian.setOnClickListener { startKneePushUpTimer() }

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
        Log.d("LegRoutine", "Workout duration set to $workoutDuration milliseconds based on mood")
        // Format and display the initial time for each timer TextView
        val formattedTime = formatTime(workoutDuration)
        timerSquats.text = formattedTime
        timerLunges.text = formattedTime
        timerBulgarian.text = formattedTime
    }

    private var isTimerRunning = false // Flag to track if a timer is currently active

    private fun startPushUpTimer() {
        if (isTimerRunning) {
            Toast.makeText(this, "Please finish the current workout timer first.", Toast.LENGTH_SHORT).show()
            return
        }

        isTimerRunning = true // Set flag to indicate a timer is running
        startSquatsButton.isEnabled = false // Disable the button when timer starts


        squatsTimer = object : CountDownTimer(workoutDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerSquats.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                timerSquats.text = "00:00"
                //Toast.makeText(this@ChestRoutine, "Push-Up Completed! You earned a badge!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false // Reset flag when timer finishes
                startSquatsButton.isEnabled = true // Re-enable the button after timer finishes
                squatsTimer = null // Reset the timer reference

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
                    mediaPlayer = MediaPlayer.create(this@LegRoutine, R.raw.workoutcomplete)
                    mediaPlayer?.start()
                }
            }
        }.start()
    }

    private fun startInclinePushUpTimer() {
        if (isTimerRunning) {
            Toast.makeText(this, "Please finish the current workout timer first.", Toast.LENGTH_SHORT).show()
            return
        }
        isTimerRunning = true
        startLungesButton.isEnabled = false


        lungesTimer = object : CountDownTimer(workoutDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerLunges.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                timerLunges.text = "00:00"
                //Toast.makeText(this@ChestRoutine, "Incline Push-Up Completed! You earned a badge!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                startLungesButton.isEnabled = true
                lungesTimer = null

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
                    mediaPlayer = MediaPlayer.create(this@LegRoutine, R.raw.workoutcomplete)
                    mediaPlayer?.start()
                }
            }
        }.start()
    }

    private fun startKneePushUpTimer() {
        if (isTimerRunning) {
            Toast.makeText(this, "Please finish the current workout timer first.", Toast.LENGTH_SHORT).show()
            return
        }
        isTimerRunning = true
        startBulgarian.isEnabled = false

        bulgarianTimer = object : CountDownTimer(workoutDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerBulgarian.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                timerBulgarian.text = "00:00"
                //Toast.makeText(this@ChestRoutine, "Knee Push-Up Completed! You earned a badge!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
                startBulgarian.isEnabled = true
                bulgarianTimer = null

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
                    mediaPlayer = MediaPlayer.create(this@LegRoutine, R.raw.workoutcomplete)
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
            mediaPlayer = MediaPlayer.create(this@LegRoutine, R.raw.badgeunlock)
            mediaPlayer?.start()
        } else {
            // Update progress bar points
            sharedPreferences.edit().putInt("currentPoints", updatedPoints).apply()
            Toast.makeText(this, "You earned $points points! Total: $updatedPoints/100", Toast.LENGTH_SHORT).show()
        }

        Log.d("LegRoutine", "Current Points: $updatedPoints, Badge Count: $badgeCount")
    }


    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        squatsTimer?.cancel()
        lungesTimer?.cancel()
        bulgarianTimer?.cancel()
        isTimerRunning = false // Reset flag if activity is destroyed
    }
}
