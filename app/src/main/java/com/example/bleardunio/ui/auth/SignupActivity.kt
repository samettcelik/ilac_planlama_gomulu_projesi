package com.example.bleardunio.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bleardunio.MainActivity
import com.example.bleardunio.R
import com.example.bleardunio.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Back button click
        binding.ivBackButton.setOnClickListener {
            onBackPressed()
        }

        // Sign up button click
        binding.btnSignup.setOnClickListener {
            if (validateInputs()) {
                performSignup()
            }
        }

        // Already have account click
        binding.tvAlreadyHaveAccount.setOnClickListener {
            // Go back to login screen
            onBackPressed()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Reset errors
        binding.tilName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        // Name validation
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            binding.tilName.error = getString(R.string.error_name_required)
            isValid = false
        }

        // Email validation
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_email_required)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        // Password validation
        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_required)
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_password_short)
            isValid = false
        }

        // Confirm password validation
        val confirmPassword = binding.etConfirmPassword.text.toString()
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = getString(R.string.error_password_required)
            isValid = false
        } else if (confirmPassword != password) {
            binding.tilConfirmPassword.error = getString(R.string.error_passwords_dont_match)
            isValid = false
        }

        return isValid
    }

    private fun performSignup() {
        // Here you would typically make an API call to register the user
        // For this demo, we'll just simulate a successful registration
        
        // Get user data
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        
        // Simulate network delay
        binding.btnSignup.isEnabled = false
        binding.btnSignup.text = "Creating account..."
        
        // In a real app, you would make an API call here
        // For demo purposes, we'll just navigate to the main screen after a delay
        binding.btnSignup.postDelayed({
            // Show success message
            Toast.makeText(this, getString(R.string.success_signup), Toast.LENGTH_SHORT).show()
            
            // Navigate to main screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            
            // Finish this activity and all activities below it in the stack
            finishAffinity()
        }, 1500) // Simulate 1.5 second delay for "network request"
    }
} 