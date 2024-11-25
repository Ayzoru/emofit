package com.example.emofit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import soup.neumorphism.NeumorphButton

class MoodDetector : AppCompatActivity() {
    private lateinit var answersGroup1: RadioGroup
    private lateinit var answersGroup2: RadioGroup
    private lateinit var answersGroup3: RadioGroup
    private lateinit var answersGroup4: RadioGroup
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_detector)

        answersGroup1 = findViewById(R.id.answers1)
        answersGroup2 = findViewById(R.id.answers2)
        answersGroup3 = findViewById(R.id.answers3)
        answersGroup4 = findViewById(R.id.answers4)
        submitButton = findViewById(R.id.submit_button)

        submitButton.setOnClickListener {
            // Check if any question is unanswered
            if (isAnyQuestionUnanswered()) {
                // Show an error message if any question is unanswered
                Toast.makeText(this, "Please answer all questions before submitting.", Toast.LENGTH_SHORT).show()
            } else {
                val totalScore = calculateTotalScore()
                val detectedMood = determineMood(totalScore)

                // Save the detected mood to SharedPreferences
                SharedPreferencesMood.saveMood(this, detectedMood)

                // Create an Intent to go to the Mood Result Activity
                val intent = Intent(this, WorkoutMenu::class.java)
                intent.putExtra("DETECTED_MOOD", detectedMood)
                startActivity(intent)
            }


        }
        // Link the back button to the function
        val backButton: NeumorphButton = findViewById(R.id.btnBackToDashboard)
        backButton.setOnClickListener {
            finish() // Closes the WorkoutMenuActivity and returns to the previous screen
        }

    }

    // Function to check if any RadioGroup is unselected
    private fun isAnyQuestionUnanswered(): Boolean {
        return answersGroup1.checkedRadioButtonId == -1 ||
                answersGroup2.checkedRadioButtonId == -1 ||
                answersGroup3.checkedRadioButtonId == -1 ||
                answersGroup4.checkedRadioButtonId == -1
    }



    private fun calculateTotalScore(): Int {
        var score = 0

        // Calculate score for each question
        score += getScoreFromRadioGroup(answersGroup1)
        score += getScoreFromRadioGroup(answersGroup2)
        score += getScoreFromRadioGroup(answersGroup3)
        score += getScoreFromRadioGroup(answersGroup4)

        return score
    }

    private fun getScoreFromRadioGroup(radioGroup: RadioGroup): Int {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val radioButton = findViewById<RadioButton>(selectedId)
            when (radioButton.text) {
                "Very energized" -> 3
                "Somewhat energized" -> 2
                "Neutral" -> 1
                "Low energy" -> 0
                "Not at all" -> 3
                "A little" -> 2
                "Somewhat" -> 1
                "A lot" -> 0
                "Very often" -> 3
                "Often" -> 2
                "Sometimes" -> 1
                "Rarely" -> 0
                "Very interested" -> 3
                "Interested" -> 2
                "Neutral" -> 1
                "Disinterested" -> 0
                else -> 0
            }
        } else {
            0
        }
    }

    private fun determineMood(totalScore: Int): String {
        return when {
            totalScore >= 11 -> "Happy \uD83D\uDE0A"
            totalScore >= 8 -> "Neutral \uD83D\uDE10"
            totalScore >= 4 -> "Stressed \uD83D\uDE24"
            else -> "Sad \uD83D\uDE25"
        }
    }
}