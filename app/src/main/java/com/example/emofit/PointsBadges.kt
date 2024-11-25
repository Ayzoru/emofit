package com.example.emofit

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphButton

class PointsBadges : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_points_badges)

        val sharedPreferences = getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        val currentPoints = sharedPreferences.getInt("currentPoints", 0)
        val badgeCount = sharedPreferences.getInt("badgeCount", 0)

        val progressBar = findViewById<ProgressBar>(R.id.progressBarPoints)
        val pointsText = findViewById<TextView>(R.id.textViewPoints)
        val badge1 = findViewById<ImageView>(R.id.appCompatImageViewBadge1)
        val badge2 = findViewById<ImageView>(R.id.appCompatImageViewBadge2)
        val badge3 = findViewById<ImageView>(R.id.appCompatImageViewBadge3)
        val thankYouMessage = findViewById<TextView>(R.id.thankYouMessage)

        progressBar.progress = currentPoints
        pointsText.text = "Points: $currentPoints / 100"

        when (badgeCount) {
            1 -> badge1.setImageResource(R.drawable.badge1)
            2 -> {
                badge1.setImageResource(R.drawable.badge1)
                badge2.setImageResource(R.drawable.badge2)
            }
            3 -> {
                badge1.setImageResource(R.drawable.badge1)
                badge2.setImageResource(R.drawable.badge2)
                badge3.setImageResource(R.drawable.badge3)

                // Show Thank You message when all badges are unlocked
                val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                thankYouMessage.startAnimation(fadeInAnimation)
                thankYouMessage.visibility = android.view.View.VISIBLE

            }
        }

        val backButton: NeumorphButton = findViewById(R.id.btnBackToDashboard)
        backButton.setOnClickListener { finish() }
    }
}
