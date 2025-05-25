package com.example.bleardunio.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bleardunio.MainActivity
import com.example.bleardunio.R
import com.example.bleardunio.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var logoRotationAnimator: ObjectAnimator? = null
    private var bottomIconRotationAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimations()
        setupListeners()
    }

    private fun setupAnimations() {
        // Initial setup - hide elements for animation
        binding.layoutLogo.alpha = 0f
        binding.layoutLogo.scaleX = 0.5f
        binding.layoutLogo.scaleY = 0.5f
        binding.cardLoginForm.alpha = 0f
        binding.cardLoginForm.translationY = 300f

        // Animate background card with wave effect
        val backgroundSlide = ObjectAnimator.ofFloat(binding.cardBackground, "translationY", -400f, 0f)
        backgroundSlide.duration = 1000
        backgroundSlide.interpolator = DecelerateInterpolator()
        backgroundSlide.start()

        // Logo entrance animation with enhanced effects
        Handler(Looper.getMainLooper()).postDelayed({
            // Logo fade in and scale with bounce
            val logoFadeIn = ObjectAnimator.ofFloat(binding.layoutLogo, "alpha", 0f, 1f)
            val logoScaleX = ObjectAnimator.ofFloat(binding.layoutLogo, "scaleX", 0.5f, 1.1f, 1f)
            val logoScaleY = ObjectAnimator.ofFloat(binding.layoutLogo, "scaleY", 0.5f, 1.1f, 1f)
            
            logoFadeIn.duration = 800
            logoScaleX.duration = 800
            logoScaleY.duration = 800
            
            logoScaleX.interpolator = BounceInterpolator()
            logoScaleY.interpolator = BounceInterpolator()
            
            // Create animator set for synchronized animation
            val logoAnimatorSet = AnimatorSet()
            logoAnimatorSet.playTogether(logoFadeIn, logoScaleX, logoScaleY)
            logoAnimatorSet.start()
            
            // Start continuous logo rotation after entrance
            Handler(Looper.getMainLooper()).postDelayed({
                startContinuousLogoRotation()
            }, 500)
            
        }, 200)

        // Login form entrance animation with improved effects
        Handler(Looper.getMainLooper()).postDelayed({
            val formFadeIn = ObjectAnimator.ofFloat(binding.cardLoginForm, "alpha", 0f, 1f)
            val formSlideUp = ObjectAnimator.ofFloat(binding.cardLoginForm, "translationY", 300f, 0f)
            val formScaleX = ObjectAnimator.ofFloat(binding.cardLoginForm, "scaleX", 0.9f, 1f)
            val formScaleY = ObjectAnimator.ofFloat(binding.cardLoginForm, "scaleY", 0.9f, 1f)
            
            formFadeIn.duration = 900
            formSlideUp.duration = 900
            formScaleX.duration = 900
            formScaleY.duration = 900
            
            formSlideUp.interpolator = OvershootInterpolator(0.2f)
            
            val formAnimatorSet = AnimatorSet()
            formAnimatorSet.playTogether(formFadeIn, formSlideUp, formScaleX, formScaleY)
            formAnimatorSet.start()
        }, 500)

        // Bottom decorative elements animation with continuous rotation
        Handler(Looper.getMainLooper()).postDelayed({
            // Start bottom icon continuous rotation
            startBottomIconRotation()
            
            // Pulse animation for decorative lines
            startDecorativeLinesPulse()
        }, 1000)
    }

    private fun startContinuousLogoRotation() {
        logoRotationAnimator = ObjectAnimator.ofFloat(binding.ivLogo, "rotation", 0f, 360f)
        logoRotationAnimator?.duration = 4000
        logoRotationAnimator?.repeatCount = ValueAnimator.INFINITE
        logoRotationAnimator?.interpolator = LinearInterpolator()
        logoRotationAnimator?.start()
    }

    private fun startBottomIconRotation() {
        bottomIconRotationAnimator = ObjectAnimator.ofFloat(binding.ivBottomIcon, "rotation", 0f, 360f)
        bottomIconRotationAnimator?.duration = 6000
        bottomIconRotationAnimator?.repeatCount = ValueAnimator.INFINITE
        bottomIconRotationAnimator?.interpolator = LinearInterpolator()
        bottomIconRotationAnimator?.start()
        
        // Additional scale pulse for bottom icon
        val iconPulse = ObjectAnimator.ofFloat(binding.ivBottomIcon, "scaleX", 1.0f, 1.3f, 1.0f)
        val iconPulseY = ObjectAnimator.ofFloat(binding.ivBottomIcon, "scaleY", 1.0f, 1.3f, 1.0f)
        iconPulse.duration = 2000
        iconPulseY.duration = 2000
        iconPulse.repeatCount = ValueAnimator.INFINITE
        iconPulseY.repeatCount = ValueAnimator.INFINITE
        iconPulse.start()
        iconPulseY.start()
    }

    private fun startDecorativeLinesPulse() {
        // Find decorative lines and animate them
        val decorativeParent = binding.ivBottomIcon.parent as android.view.ViewGroup
        
        // Create subtle alpha pulse for the entire decorative section
        val alphaPulse = ObjectAnimator.ofFloat(decorativeParent, "alpha", 0.6f, 1.0f, 0.6f)
        alphaPulse.duration = 3000
        alphaPulse.repeatCount = ValueAnimator.INFINITE
        alphaPulse.start()
    }

    private fun setupListeners() {
        // Login button with enhanced animation
        binding.btnLogin.setOnClickListener {
            animateButtonPress(it) {
                if (validateInputs()) {
                    performLogin()
                }
            }
        }

        // Input field focus animations with improved effects
        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            animateInputField(binding.tilEmail, hasFocus)
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            animateInputField(binding.tilPassword, hasFocus)
        }
    }

    private fun animateInputField(inputLayout: com.google.android.material.textfield.TextInputLayout, hasFocus: Boolean) {
        val scale = if (hasFocus) 1.03f else 1.0f
        val elevation = if (hasFocus) 12f else 6f
        
        val scaleX = ObjectAnimator.ofFloat(inputLayout, "scaleX", inputLayout.scaleX, scale)
        val scaleY = ObjectAnimator.ofFloat(inputLayout, "scaleY", inputLayout.scaleY, scale)
        val elevationAnim = ObjectAnimator.ofFloat(inputLayout, "elevation", elevation)
        
        scaleX.duration = 250
        scaleY.duration = 250
        elevationAnim.duration = 250
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, elevationAnim)
        animatorSet.start()
    }

    private fun animateButtonPress(view: android.view.View, onComplete: () -> Unit) {
        // Enhanced press animation with rotation
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.92f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.92f)
        val rotateDown = ObjectAnimator.ofFloat(view, "rotation", 0f, 2f)
        
        scaleDownX.duration = 120
        scaleDownY.duration = 120
        rotateDown.duration = 120
        
        val downAnimatorSet = AnimatorSet()
        downAnimatorSet.playTogether(scaleDownX, scaleDownY, rotateDown)
        downAnimatorSet.start()
        
        // Scale back up and execute action
        Handler(Looper.getMainLooper()).postDelayed({
            val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.92f, 1.0f)
            val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.92f, 1.0f)
            val rotateUp = ObjectAnimator.ofFloat(view, "rotation", 2f, 0f)
            
            scaleUpX.duration = 120
            scaleUpY.duration = 120
            rotateUp.duration = 120
            
            val upAnimatorSet = AnimatorSet()
            upAnimatorSet.playTogether(scaleUpX, scaleUpY, rotateUp)
            upAnimatorSet.start()
            
            onComplete()
        }, 120)
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Reset errors
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        // Email validation
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_email_required)
            animateErrorField(binding.tilEmail)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            animateErrorField(binding.tilEmail)
            isValid = false
        }

        // Password validation
        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_required)
            animateErrorField(binding.tilPassword)
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_password_short)
            animateErrorField(binding.tilPassword)
            isValid = false
        }

        return isValid
    }

    private fun animateErrorField(inputLayout: com.google.android.material.textfield.TextInputLayout) {
        // Enhanced shake animation with color pulse
        val shake = ObjectAnimator.ofFloat(inputLayout, "translationX", 0f, 30f, -30f, 25f, -25f, 15f, -15f, 8f, -8f, 0f)
        val pulse = ObjectAnimator.ofFloat(inputLayout, "scaleX", 1.0f, 1.05f, 1.0f)
        val pulseY = ObjectAnimator.ofFloat(inputLayout, "scaleY", 1.0f, 1.05f, 1.0f)
        
        shake.duration = 700
        pulse.duration = 300
        pulseY.duration = 300
        
        val errorAnimatorSet = AnimatorSet()
        errorAnimatorSet.playTogether(shake, pulse, pulseY)
        errorAnimatorSet.start()
    }

    private fun performLogin() {
        // Enhanced loading animation
        binding.btnLogin.isEnabled = false
        binding.progressLoading.visibility = android.view.View.VISIBLE
        
        // Animate button text change with fade
        val textFadeOut = ObjectAnimator.ofFloat(binding.btnLogin, "alpha", 1.0f, 0.7f)
        textFadeOut.duration = 200
        textFadeOut.start()
        
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnLogin.text = "Giriş yapılıyor..."
            val textFadeIn = ObjectAnimator.ofFloat(binding.btnLogin, "alpha", 0.7f, 1.0f)
            textFadeIn.duration = 200
            textFadeIn.start()
        }, 200)
        
        // Enhanced loading spinner animation
        val loadingRotation = ObjectAnimator.ofFloat(binding.progressLoading, "rotation", 0f, 360f)
        val loadingPulse = ObjectAnimator.ofFloat(binding.progressLoading, "scaleX", 1.0f, 1.4f, 1.0f)
        val loadingPulseY = ObjectAnimator.ofFloat(binding.progressLoading, "scaleY", 1.0f, 1.4f, 1.0f)
        
        loadingRotation.duration = 1000
        loadingPulse.duration = 1200
        loadingPulseY.duration = 1200
        
        loadingRotation.repeatCount = ValueAnimator.INFINITE
        loadingPulse.repeatCount = ValueAnimator.INFINITE
        loadingPulseY.repeatCount = ValueAnimator.INFINITE
        
        loadingRotation.interpolator = LinearInterpolator()
        
        loadingRotation.start()
        loadingPulse.start()
        loadingPulseY.start()
        
        // Simulate network delay with enhanced completion animation
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop loading animations
            loadingRotation.cancel()
            loadingPulse.cancel()
            loadingPulseY.cancel()
            binding.progressLoading.visibility = android.view.View.GONE
            
            // Success animation
            showEnhancedSuccessAnimation()
            
            // Navigate after success animation
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                
                // Custom transition
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }, 1000)
            
        }, 2000)
    }

    private fun showEnhancedSuccessAnimation() {
        // Change button appearance for success with animation
        val textFadeOut = ObjectAnimator.ofFloat(binding.btnLogin, "alpha", 1.0f, 0.0f)
        textFadeOut.duration = 200
        textFadeOut.start()
        
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnLogin.text = "✓ Başarılı!"
            binding.btnLogin.backgroundTintList = android.content.res.ColorStateList.valueOf(
                resources.getColor(android.R.color.holo_green_dark)
            )
            
            val textFadeIn = ObjectAnimator.ofFloat(binding.btnLogin, "alpha", 0.0f, 1.0f)
            textFadeIn.duration = 200
            textFadeIn.start()
        }, 200)
        
        // Enhanced success bounce animation with rotation
        val bounce = ObjectAnimator.ofFloat(binding.btnLogin, "scaleX", 1.0f, 1.2f, 1.0f)
        val bounceY = ObjectAnimator.ofFloat(binding.btnLogin, "scaleY", 1.0f, 1.2f, 1.0f)
        val successRotation = ObjectAnimator.ofFloat(binding.btnLogin, "rotation", 0f, 360f)
        
        bounce.duration = 600
        bounceY.duration = 600
        successRotation.duration = 600
        
        bounce.interpolator = BounceInterpolator()
        bounceY.interpolator = BounceInterpolator()
        
        val successAnimatorSet = AnimatorSet()
        successAnimatorSet.playTogether(bounce, bounceY, successRotation)
        successAnimatorSet.start()
        
        // Enhanced card pulse effect with glow simulation
        val cardPulse = ObjectAnimator.ofFloat(binding.cardLoginForm, "scaleX", 1.0f, 1.05f, 1.0f)
        val cardPulseY = ObjectAnimator.ofFloat(binding.cardLoginForm, "scaleY", 1.0f, 1.05f, 1.0f)
        val cardElevation = ObjectAnimator.ofFloat(binding.cardLoginForm, "elevation", 20f, 30f, 20f)
        
        cardPulse.duration = 400
        cardPulseY.duration = 400
        cardElevation.duration = 400
        
        val cardAnimatorSet = AnimatorSet()
        cardAnimatorSet.playTogether(cardPulse, cardPulseY, cardElevation)
        cardAnimatorSet.start()
        
        Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up animations
        logoRotationAnimator?.cancel()
        bottomIconRotationAnimator?.cancel()
    }
}