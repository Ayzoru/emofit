package com.example.emofit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.emofit.databinding.ActivityDashboardBinding
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Inflate layout with DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val helpIcon2: NeumorphButton = findViewById(R.id.btnHelp3)

        helpIcon2.setOnClickListener {
            val youtubeUrl = "https://www.youtube.com/watch?v=mLlPdT_Yz3o"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open the tutorial link", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up exit button click listener
        binding.btnExit.setOnClickListener {
            showExitConfirmationDialog()
        }

        // Load the username from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USER_NAME", "User") ?: "User"
        viewModel.setUsername(username)

        // Observe the username LiveData
        viewModel.username.observe(this, { name ->
            // Concatenate "Welcome, " with the username
            val welcomeMessage = "Welcome, $name"
            binding.textView5.text = welcomeMessage
        })

        // Set up button click listeners
        val btnStartWorkout = findViewById<NeumorphCardView>(R.id.btnStartWorkout)
        btnStartWorkout.setOnClickListener {
            val intent = Intent(this, WorkoutMenu::class.java)
            startActivity(intent)
        }

        val btnMood = findViewById<NeumorphCardView>(R.id.btnMoodDetector)
        btnMood.setOnClickListener {
            val intent = Intent(this, MoodDetector::class.java)
            startActivity(intent)
        }

        val btnBadge = findViewById<NeumorphCardView>(R.id.btnPoints)
        btnBadge.setOnClickListener {
            val intent = Intent(this, PointsBadges::class.java)
            startActivity(intent)
        }

        val btnPref = findViewById<NeumorphCardView>(R.id.btnPreferences)
        btnPref.setOnClickListener {
            val intent = Intent(this, Preferences::class.java)
            startActivity(intent)
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit? EmoFit will miss you \uD83D\uDE2D")

        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}
