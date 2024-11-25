package com.example.emofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.emofit.databinding.ActivityPreferencesBinding
import soup.neumorphism.NeumorphButton

class Preferences : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

        // Load user data
        loadUserData()

        // Set up update button
        binding.btnUpdate.setOnClickListener {
            updateUserData()
        }

        // Set up clear data button
        binding.btnClearData.setOnClickListener {
            clearUserData()
        }

        val backButton: NeumorphButton = findViewById(R.id.btnBackToDashboard)
        backButton.setOnClickListener { finish() }
    }

    private fun loadUserData() {
        // Retrieve data from SharedPreferences
        val name = sharedPreferences.getString("USER_NAME", "User")
        val age = sharedPreferences.getInt("USER_AGE", 0)

        // Display current data
        binding.textViewName.text = "Username: $name"
        binding.textViewAge.text = "Age: $age"
        binding.editTextName.setText(name)
        binding.editTextAge.setText(if (age != 0) age.toString() else "")
    }

    private fun updateUserData() {
        // Retrieve updated values
        val newName = binding.editTextName.text.toString().trim()
        val newAgeString = binding.editTextAge.text.toString().trim()

        // Validate inputs
        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (newName.length > 10) {
            Toast.makeText(this, "Name cannot exceed 10 characters", Toast.LENGTH_SHORT).show()
            return
        }


        if (newAgeString.isEmpty() || newAgeString.toIntOrNull() == null || newAgeString.toInt() <= 0) {
            Toast.makeText(this, "Please enter a valid age greater than 0", Toast.LENGTH_SHORT).show()
            return
        }

        val newAge = newAgeString.toInt()
        if (newAge > 120) {
            Toast.makeText(this, "Age cannot exceed 120", Toast.LENGTH_SHORT).show()
            return
        }

        // Save updated data in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", newName)
        editor.putInt("USER_AGE", newAge)
        editor.apply()

        // Show a confirmation message
        Toast.makeText(this, "Details updated successfully", Toast.LENGTH_SHORT).show()

        // Refresh the Dashboard
        val intent = Intent(this, Dashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()

    }

    private fun clearUserData() {
        // Clear SharedPreferences for user details
        val userDetailsEditor = sharedPreferences.edit()
        userDetailsEditor.clear()
        userDetailsEditor.apply()


        // Clear mood, timer, and other EmoFitPrefs data
        val emoFitPreferences = getSharedPreferences("EmoFitPrefs", Context.MODE_PRIVATE)
        val emoFitEditor = emoFitPreferences.edit()
        emoFitEditor.clear()
        emoFitEditor.apply()

        // Redirect to the User Details page
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
