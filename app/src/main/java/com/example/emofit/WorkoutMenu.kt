package com.example.emofit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class WorkoutMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_menu)

        // Retrieve the saved mood from SharedPreferences
        val savedMood = SharedPreferencesMood.getSavedMood(this) ?: "Unknown"

        // Find the TextView for displaying the current mood
        val currentMoodText = findViewById<TextView>(R.id.currentMoodtext)

        // Set the detected mood in the TextView
        currentMoodText.text = "Current Mood: $savedMood"

        val btnChestPage = findViewById<NeumorphCardView>(R.id.btnChest)
        btnChestPage.setOnClickListener {
            // Navigate to WorkoutMenu
            val intent = Intent(this, ChestRoutine::class.java)
            startActivity(intent)
        }

        val btnBackPage = findViewById<NeumorphCardView>(R.id.btnBackBody)
        btnBackPage.setOnClickListener {
            // Navigate to WorkoutMenu
            val intent = Intent(this, BackRoutine::class.java)
            startActivity(intent)
        }

        val btnLegsPage = findViewById<NeumorphCardView>(R.id.btnLegs)
        btnLegsPage.setOnClickListener {
            // Navigate to WorkoutMenu
            val intent = Intent(this, LegRoutine::class.java)
            startActivity(intent)
        }

        val btnCardioPage = findViewById<NeumorphCardView>(R.id.btnCardio)
        btnCardioPage.setOnClickListener {
            // Navigate to WorkoutMenu
            val intent = Intent(this, CardioRoutine::class.java)
            startActivity(intent)
        }

        // Link the back button to the function
        val backButton: NeumorphButton = findViewById(R.id.btnBackToDashboard)
        backButton.setOnClickListener {
            finish() // Closes the WorkoutMenuActivity and returns to the previous screen
        }



    }

}