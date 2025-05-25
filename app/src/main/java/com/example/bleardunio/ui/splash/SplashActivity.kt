package com.example.bleardunio.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.bleardunio.R
import com.example.bleardunio.ui.auth.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_TIMEOUT = 2000L // 2 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Handler to redirect after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if user is logged in here if needed
            val isLoggedIn = false // Replace with your login check logic

            // Redirect to appropriate screen
            if (isLoggedIn) {
                // If user is logged in, go to MainActivity
                startActivity(Intent(this, com.example.bleardunio.MainActivity::class.java))
            } else {
                // If user is not logged in, go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            
            // Finish this activity so user can't go back to splash
            finish()
        }, SPLASH_TIMEOUT)
    }
} 