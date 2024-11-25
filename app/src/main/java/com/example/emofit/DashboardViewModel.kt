package com.example.emofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username



    // Function to set the welcome message with the username
    fun setUsername(name: String) {
        _username.value = name
    }
}