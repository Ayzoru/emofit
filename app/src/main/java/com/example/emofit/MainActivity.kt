package com.example.emofit

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import soup.neumorphism.NeumorphButton

class MainActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var letsGetFitButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the layout first
        setContentView(R.layout.activity_main)

        val helpIcon: NeumorphButton = findViewById(R.id.btnHelp)

        helpIcon.setOnClickListener {
            val youtubeUrl = "https://www.youtube.com/watch?v=mLlPdT_Yz3o"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open the tutorial link", Toast.LENGTH_SHORT).show()
            }
        }


        // Initialize views
        nameInput = findViewById(R.id.editTextText)
        ageInput = findViewById(R.id.editTextText2)

        // Set max length of 12 for name input
        nameInput.filters = arrayOf(InputFilter.LengthFilter(10))

        // Set age input to accept only numbers
        ageInput.inputType = InputType.TYPE_CLASS_NUMBER

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE)

        // Check if user details are already saved
        if (isUserDetailsSaved()) {
            // Retrieve the saved username from SharedPreferences
            val savedName = sharedPreferences.getString("USER_NAME", null)
            savedName?.let {
                navigateToMainMenu(it)  // Pass the saved username
            }
            return
        }



        // Initialize views
        nameInput = findViewById(R.id.editTextText)
        ageInput = findViewById(R.id.editTextText2)
        genderSpinner = findViewById(R.id.genderSpinner)
        letsGetFitButton = findViewById(R.id.btnLetsGetFit)

        // Set up the Spinner with gender options
        val genderOptions = arrayOf("Select Gender", "Male", "Female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        // Set up button click listener
        letsGetFitButton.setOnClickListener {
            saveUserDetails()
        }
    }

    private fun isUserDetailsSaved(): Boolean {
        val name = sharedPreferences.getString("USER_NAME", null)
        val age = sharedPreferences.getInt("USER_AGE", -1)
        return !name.isNullOrEmpty() && age != -1
    }

    private fun saveUserDetails() {
        val name = nameInput.text.toString().trim()
        val ageString = ageInput.text.toString().trim()
        val gender = genderSpinner.selectedItem.toString()

        // Validation checks
        if (name.isEmpty() || ageString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if age is a valid number
        val age = ageString.toIntOrNull()
        if (age == null || age <= 0 || age > 120) {
            Toast.makeText(this, "Please enter a valid age between 1 and 120", Toast.LENGTH_SHORT).show()
            return
        }

        if (gender == "Select Gender") {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return
        }

        // Save details in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", name)
        editor.putInt("USER_AGE", age)
        editor.putString("USER_GENDER", gender)
        editor.apply()

        // Navigate to Main Menu
        navigateToMainMenu(name)
    }

    private fun navigateToMainMenu(name: String) {

        val intent = Intent(this, Dashboard::class.java)
        intent.putExtra("USERNAME", name) //Pass the username
        startActivity(intent)
        finish()
    }
}