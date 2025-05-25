package com.example.bleardunio

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bleardunio.databinding.ActivityAddAlarmBinding
import org.json.JSONArray

class AddAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimations()
        setupListeners()
    }

    private fun setupAnimations() {
        // Entrance animations
        val slideDown = ObjectAnimator.ofFloat(binding.cardHeader, "translationY", -200f, 0f)
        slideDown.duration = 600
        slideDown.interpolator = DecelerateInterpolator()
        slideDown.start()

        // Card entrance animation with bounce
        Handler(Looper.getMainLooper()).postDelayed({
            val scaleX = ObjectAnimator.ofFloat(binding.cardAlarm, "scaleX", 0.8f, 1.0f)
            val scaleY = ObjectAnimator.ofFloat(binding.cardAlarm, "scaleY", 0.8f, 1.0f)
            val alpha = ObjectAnimator.ofFloat(binding.cardAlarm, "alpha", 0f, 1f)
            
            scaleX.duration = 500
            scaleY.duration = 500
            alpha.duration = 500
            
            scaleX.interpolator = BounceInterpolator()
            scaleY.interpolator = BounceInterpolator()
            
            scaleX.start()
            scaleY.start()
            alpha.start()
        }, 200)

        // Clock icon animation
        Handler(Looper.getMainLooper()).postDelayed({
            val rotation = ObjectAnimator.ofFloat(binding.ivClockIcon, "rotation", 0f, 360f)
            rotation.duration = 1000
            rotation.start()
        }, 700)
    }

    private fun setupListeners() {
        // Close button with animation
        binding.btnClose.setOnClickListener {
            animateExit()
        }

        // Save button with press animation
        binding.btnSaveAlarm.setOnClickListener {
            // Button press animation
            val scaleDown = ObjectAnimator.ofFloat(it, "scaleX", 1.0f, 0.95f)
            val scaleDownY = ObjectAnimator.ofFloat(it, "scaleY", 1.0f, 0.95f)
            
            scaleDown.duration = 150
            scaleDownY.duration = 150
            
            scaleDown.start()
            scaleDownY.start()
            
            // Scale back up
            Handler(Looper.getMainLooper()).postDelayed({
                val scaleUp = ObjectAnimator.ofFloat(it, "scaleX", 0.95f, 1.0f)
                val scaleUpY = ObjectAnimator.ofFloat(it, "scaleY", 0.95f, 1.0f)
                
                scaleUp.duration = 150
                scaleUpY.duration = 150
                
                scaleUp.start()
                scaleUpY.start()
            }, 150)
            
            // Validate and save after animation
            Handler(Looper.getMainLooper()).postDelayed({
                if (validateAlarmInput()) {
                    saveAlarmWithAnimation()
                }
            }, 300)
        }
    }

    private fun animateExit() {
        val slideUp = ObjectAnimator.ofFloat(binding.cardAlarm, "translationY", 0f, -300f)
        val fadeOut = ObjectAnimator.ofFloat(binding.cardAlarm, "alpha", 1f, 0f)
        
        slideUp.duration = 300
        fadeOut.duration = 300
        
        slideUp.start()
        fadeOut.start()
        
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 300)
    }

    private fun validateAlarmInput(): Boolean {
        // For now, just ensure the alarm name is not empty
        val alarmName = binding.etAlarmName.text.toString().trim()
        if (alarmName.isEmpty()) {
            // Shake animation for error
            val shake = ObjectAnimator.ofFloat(binding.tilAlarmName, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
            shake.duration = 600
            shake.start()
            
            Toast.makeText(this, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveAlarmWithAnimation() {
        // Success animation
        val pulse = ObjectAnimator.ofFloat(binding.btnSaveAlarm, "scaleX", 1.0f, 1.1f, 1.0f)
        val pulseY = ObjectAnimator.ofFloat(binding.btnSaveAlarm, "scaleY", 1.0f, 1.1f, 1.0f)
        
        pulse.duration = 400
        pulseY.duration = 400
        
        pulse.start()
        pulseY.start()
        
        // Save the alarm
        saveAlarm()
        
        // Success message with animation
        Handler(Looper.getMainLooper()).postDelayed({
            showSuccessAnimation()
        }, 200)
        
        // Navigate back after success animation
        Handler(Looper.getMainLooper()).postDelayed({
            MainActivity.instance?.navigateToHome()
            finish()
        }, 1500)
    }

    private fun showSuccessAnimation() {
        // Change button text temporarily
        val originalText = binding.btnSaveAlarm.text
        binding.btnSaveAlarm.text = "✓ Kaydedildi!"
        
        // Green color animation
        val colorAnimation = ValueAnimator.ofArgb(
            resources.getColor(R.color.accent),
            resources.getColor(android.R.color.holo_green_dark)
        )
        colorAnimation.duration = 500
        colorAnimation.addUpdateListener { animator ->
            binding.btnSaveAlarm.backgroundTintList = 
                android.content.res.ColorStateList.valueOf(animator.animatedValue as Int)
        }
        colorAnimation.start()
        
        Toast.makeText(this, getString(R.string.success_alarm_saved), Toast.LENGTH_SHORT).show()
    }

    private fun saveAlarm() {
        // Get the time from the picker
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute
        val alarmTime = String.format("%02d:%02d", hour, minute)

        // Get the alarm name (medication name)
        val medicationName = binding.etAlarmName.text.toString().trim()

        // Save the alarm (in a real app you'd use a database or repository)
        val alarms = getAlarms().toMutableList()

        // For this demo, we'll just save the time and name as a formatted string
        alarms.add("$alarmTime | $medicationName")
        saveAlarms(alarms)

        // Send alarm to Arduino
        val mainActivity = MainActivity.instance
        mainActivity?.let {
            // Format as required for Arduino: time and medication name
            val alarmData = "$alarmTime:$medicationName"
            it.sendAlarmToArduino(alarmData)
        }
    }

    private fun getAlarms(): List<String> {
        val sharedPrefs = getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val json = sharedPrefs.getString("alarmList", "[]")
        val jsonArray = JSONArray(json)
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }

        return list
    }

    private fun saveAlarms(alarms: List<String>) {
        val sharedPrefs = getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val jsonArray = JSONArray()

        for (alarm in alarms) {
            jsonArray.put(alarm)
        }

        sharedPrefs.edit().putString("alarmList", jsonArray.toString()).apply()
    }
} 