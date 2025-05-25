package com.example.bleardunio.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bleardunio.MainActivity
import com.example.bleardunio.R
import com.example.bleardunio.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Auto-fill the credentials
        binding.etEmail.setText("test123@gmail.com")
        binding.etPassword.setText("201050")

        setupListeners()
    }

    private fun setupListeners() {
        // Login button click
        binding.btnLogin.setOnClickListener {
            // Since credentials are pre-filled, we can just proceed to MainActivity
            // In a real app, you'd validate credentials against a database
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close LoginActivity so user can't go back
        }

        // Signup button click

    }
} 