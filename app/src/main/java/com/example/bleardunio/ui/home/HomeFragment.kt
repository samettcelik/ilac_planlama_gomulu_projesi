package com.example.bleardunio.ui.home

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bleardunio.MainActivity
import com.example.bleardunio.R
import com.example.bleardunio.databinding.FragmentHomeBinding
import com.example.bleardunio.ui.alarms.AlarmsAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.min

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var alarmsAdapter: AlarmsAdapter
    private var clockHandler: Handler? = null
    private var clockRunnable: Runnable? = null
    private var nextAlarmTime: Calendar? = null
    private var isRoadMapVisible = false
    private var medicationProgress = 0
    private val maxMedications = 10

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            setupRecyclerView()
            setupListeners()
            setupAnimations()
            startClock()
            updateNextAlarm()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated", e)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            loadAlarms()
            startClock()
            animateCardEntrance()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume", e)
        }
    }

    override fun onPause() {
        super.onPause()
        stopClock()
    }

    private fun stopClock() {
        try {
            clockHandler?.removeCallbacks(clockRunnable ?: return)
            clockHandler = null
            clockRunnable = null
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping clock", e)
        }
    }

    private fun setupAnimations() {
        try {
            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            val scaleIn = AnimationUtils.loadAnimation(context, R.anim.scale_in)

            _binding?.let { binding ->
                binding.cardHeader.startAnimation(fadeIn)
                binding.cardNextMedication.startAnimation(slideUp)
                binding.btnAddAlarm.startAnimation(scaleIn)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in setupAnimations", e)
        }
    }

    private fun animateCardEntrance() {
        try {
            val handler = Handler(Looper.getMainLooper())
            
            handler.postDelayed({
                try {
                    _binding?.let { binding ->
                        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                        binding.cardAlarms.startAnimation(slideUp)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in delayed animation", e)
                }
            }, 300)
        } catch (e: Exception) {
            Log.e(TAG, "Error in animateCardEntrance", e)
        }
    }

    private fun startClock() {
        try {
            stopClock() // Stop any existing clock first
            
            clockHandler = Handler(Looper.getMainLooper())
            clockRunnable = object : Runnable {
                override fun run() {
                    try {
                        if (_binding != null && isAdded) {
                            updateClock()
                            updateCountdown()
                            checkMedicationTime()
                            clockHandler?.postDelayed(this, 1000)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in clock runnable", e)
                    }
                }
            }
            clockHandler?.post(clockRunnable ?: return)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting clock", e)
        }
    }

    private fun updateClock() {
        try {
            _binding?.let { binding ->
                val now = Calendar.getInstance()
                
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                binding.tvCurrentTime.text = timeFormat.format(now.time)
                
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("tr", "TR"))
                binding.tvCurrentDate.text = dateFormat.format(now.time)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating clock", e)
        }
    }

    private fun updateCountdown() {
        try {
            _binding?.let { binding ->
                nextAlarmTime?.let { alarmTime ->
                    val now = Calendar.getInstance()
                    val timeDiff = alarmTime.timeInMillis - now.timeInMillis
                    
                    if (timeDiff > 0) {
                        val hours = timeDiff / (1000 * 60 * 60)
                        val minutes = (timeDiff % (1000 * 60 * 60)) / (1000 * 60)
                        val seconds = (timeDiff % (1000 * 60)) / 1000
                        
                        when {
                            hours > 0 -> {
                                binding.tvTimeRemaining.text = String.format("%d:%02d", hours, minutes)
                            }
                            minutes > 0 -> {
                                binding.tvTimeRemaining.text = String.format("%d:%02d", minutes, seconds)
                            }
                            else -> {
                                binding.tvTimeRemaining.text = String.format("0:%02d", seconds)
                                // Pulse animation when less than 1 minute
                                if (seconds % 2 == 0L) {
                                    val pulseAnimation = ObjectAnimator.ofFloat(binding.llTimeRemaining, "scaleX", 1.0f, 1.05f, 1.0f)
                                    pulseAnimation.duration = 400
                                    pulseAnimation.start()
                                }
                            }
                        }
                    } else {
                        binding.tvTimeRemaining.text = "00:00"
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating countdown", e)
        }
    }

    private fun checkMedicationTime() {
        try {
            nextAlarmTime?.let { alarmTime ->
                val now = Calendar.getInstance()
                if (now.timeInMillis >= alarmTime.timeInMillis && medicationProgress < maxMedications) {
                    incrementMedicationProgress()
                    nextAlarmTime = Calendar.getInstance().apply {
                        add(Calendar.HOUR_OF_DAY, 1)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking medication time", e)
        }
    }

    private fun incrementMedicationProgress() {
        try {
            if (medicationProgress < maxMedications) {
                medicationProgress++
                updateMedicationProgress(medicationProgress, maxMedications)
                
                _binding?.let { binding ->
                    val successAnimation = ObjectAnimator.ofFloat(binding.cardNextMedication, "scaleX", 1.0f, 1.05f, 1.0f)
                    successAnimation.duration = 500
                    successAnimation.start()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error incrementing medication progress", e)
        }
    }

    private fun updateNextAlarm() {
        try {
            _binding?.let { binding ->
                binding.tvNextAlarmTime.text = "13:00"
                binding.tvMedicationName.text = getString(R.string.default_medication)
                
                nextAlarmTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 13)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating next alarm", e)
        }
    }

    private fun updateMedicationProgress(currentCount: Int, totalCount: Int = maxMedications) {
        try {
            _binding?.let { binding ->
                val progress = (currentCount * 100) / totalCount
                
                // Set the max value to the total count (number of alarms or maxMedications)
                binding.pbMedicationProgress.max = totalCount
                
                // Animate main progress bar
                val progressAnimator = ObjectAnimator.ofInt(binding.pbMedicationProgress, "progress", 0, currentCount)
                progressAnimator.duration = 1200
                progressAnimator.interpolator = DecelerateInterpolator()
                progressAnimator.start()
                
                // Update main card text with animation
                val countAnimator = ValueAnimator.ofInt(0, currentCount)
                countAnimator.duration = 1200
                countAnimator.addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Int
                    binding.tvMedicationCount.text = "$animatedValue / $totalCount İlaç Alındı"
                }
                countAnimator.start()
                
                // Update main card percentage with bounce effect
                binding.tvMedicationPercentage.text = "$progress%"
                val bounceAnimation = ObjectAnimator.ofFloat(binding.tvMedicationPercentage, "scaleX", 1.0f, 1.3f, 1.0f)
                bounceAnimation.duration = 400
                bounceAnimation.start()
                
                // Update detailed road map if visible
                if (isRoadMapVisible) {
                    updateDetailedProgress(currentCount, totalCount, progress)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating medication progress", e)
        }
    }

    private fun updateDetailedProgress(currentCount: Int, totalCount: Int, progress: Int) {
        try {
            _binding?.let { binding ->
                // Set the max value to the total count
                binding.pbDetailedMedicationProgress.max = totalCount
                
                // Animate detailed progress bar
                val detailedProgressAnimator = ObjectAnimator.ofInt(binding.pbDetailedMedicationProgress, "progress", 0, currentCount)
                detailedProgressAnimator.duration = 1200
                detailedProgressAnimator.interpolator = DecelerateInterpolator()
                detailedProgressAnimator.start()
                
                // Update detailed text
                val detailedCountAnimator = ValueAnimator.ofInt(0, currentCount)
                detailedCountAnimator.duration = 1200
                detailedCountAnimator.addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Int
                    binding.tvDetailedMedicationCount.text = "$animatedValue / $totalCount İlaç Alındı"
                }
                detailedCountAnimator.start()
                
                // Update detailed percentage
                binding.tvDetailedMedicationPercentage.text = "$progress%"
                val detailedBounceAnimation = ObjectAnimator.ofFloat(binding.tvDetailedMedicationPercentage, "scaleX", 1.0f, 1.3f, 1.0f)
                detailedBounceAnimation.duration = 400
                detailedBounceAnimation.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating detailed progress", e)
        }
    }

    private fun setupRecyclerView() {
        try {
            alarmsAdapter = AlarmsAdapter(emptyList()) { position ->
                showAlarmOptionsDialog(position)
            }
            
            _binding?.let { binding ->
                binding.rvActiveAlarms.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = alarmsAdapter
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView", e)
        }
    }

    private fun setupListeners() {
        try {
            _binding?.let { binding ->
                // Medication name click - toggle road map
                binding.tvMedicationName.setOnClickListener {
                    toggleMedicationRoadMap()
                }

                // Close road map
                binding.ivCloseRoadMap.setOnClickListener {
                    hideMedicationRoadMap()
                }

                // Add alarm button with scale animation
                binding.btnAddAlarm.setOnClickListener {
                    try {
                        val scaleDown = ObjectAnimator.ofFloat(it, "scaleX", 1.0f, 0.92f, 1.0f)
                        scaleDown.duration = 150
                        scaleDown.start()
                        
                        val scaleDownY = ObjectAnimator.ofFloat(it, "scaleY", 1.0f, 0.92f, 1.0f)
                        scaleDownY.duration = 150
                        scaleDownY.start()
                        
                        (activity as? MainActivity)?.openAddAlarmActivity()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in button click", e)
                        // Fallback - just open the activity without animation
                        (activity as? MainActivity)?.openAddAlarmActivity()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up listeners", e)
        }
    }

    private fun toggleMedicationRoadMap() {
        try {
            if (isRoadMapVisible) {
                hideMedicationRoadMap()
            } else {
                showMedicationRoadMap()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling medication road map", e)
        }
    }

    private fun showMedicationRoadMap() {
        try {
            if (!isRoadMapVisible) {
                isRoadMapVisible = true
                _binding?.let { binding ->
                    binding.cardMedicationRoadMap.visibility = View.VISIBLE
                    
                    // Animate in
                    val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                    binding.cardMedicationRoadMap.startAnimation(slideDown)
                    
                    // Update detailed progress immediately with correct values
                    val mainActivity = activity as? MainActivity
                    val alarms = mainActivity?.getAlarms() ?: emptyList()
                    val alarmCount = alarms.size
                    val totalForProgress = if (alarmCount > maxMedications) alarmCount else maxMedications
                    val progress = if (totalForProgress > 0) (alarmCount * 100) / totalForProgress else 0
                    
                    binding.pbDetailedMedicationProgress.max = totalForProgress
                    binding.pbDetailedMedicationProgress.progress = alarmCount
                    binding.tvDetailedMedicationCount.text = "$alarmCount / $totalForProgress İlaç Alındı"
                    binding.tvDetailedMedicationPercentage.text = "$progress%"
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing medication road map", e)
        }
    }

    private fun hideMedicationRoadMap() {
        try {
            if (isRoadMapVisible) {
                isRoadMapVisible = false
                
                _binding?.let { binding ->
                    // Animate out
                    val fadeOut = ObjectAnimator.ofFloat(binding.cardMedicationRoadMap, "alpha", 1.0f, 0.0f)
                    fadeOut.duration = 300
                    fadeOut.start()
                    
                    fadeOut.addUpdateListener {
                        if (it.animatedFraction == 1.0f) {
                            binding.cardMedicationRoadMap.visibility = View.GONE
                            binding.cardMedicationRoadMap.alpha = 1.0f
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error hiding medication road map", e)
        }
    }
    
    private fun loadAlarms() {
        try {
            val mainActivity = activity as? MainActivity
            val alarms = mainActivity?.getAlarms() ?: emptyList()
            val alarmCount = alarms.size
            
            _binding?.let { binding ->
                // Update alarm count badge with smooth animation
                binding.tvAlarmCount.text = alarmCount.toString()
                
                if (alarmCount > 0) {
                    val badgeAnimation = ObjectAnimator.ofFloat(binding.tvAlarmCount, "scaleX", 1.0f, 1.2f, 1.0f)
                    badgeAnimation.duration = 250
                    badgeAnimation.start()
                }
                
                if (alarms.isEmpty()) {
                    binding.llNoAlarms.visibility = View.VISIBLE
                    binding.rvActiveAlarms.visibility = View.GONE
                    
                    val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                    binding.llNoAlarms.startAnimation(fadeIn)
                    
                    // Update progress bars for no alarms
                    updateMedicationProgress(0, maxMedications)
                } else {
                    binding.llNoAlarms.visibility = View.GONE
                    binding.rvActiveAlarms.visibility = View.VISIBLE
                    
                    alarmsAdapter.updateAlarms(alarms)
                    
                    // Update medication progress based on alarms count
                    // Use actual alarm count as the progress, but keep maxMedications as total
                    val totalForProgress = if (alarmCount > maxMedications) alarmCount else maxMedications
                    medicationProgress = alarmCount
                    updateMedicationProgress(medicationProgress, totalForProgress)
                    
                    // Update next alarm info
                    try {
                        if (alarms.isNotEmpty()) {
                            val lastAlarm = alarms[alarms.size - 1]
                            val parts = lastAlarm.split("|")
                            if (parts.size == 2) {
                                val time = parts[0].trim()
                                val medication = parts[1].trim()
                                
                                binding.tvNextAlarmTime.text = time
                                binding.tvMedicationName.text = medication
                                
                                updateNextAlarmTime(time)
                            } else {
                                updateNextAlarm()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing alarm data", e)
                        updateNextAlarm()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading alarms", e)
        }
    }
    
    private fun updateNextAlarmTime(timeString: String) {
        try {
            val timeParts = timeString.split(":")
            if (timeParts.size == 2) {
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()
                
                nextAlarmTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating next alarm time", e)
            updateNextAlarm()
        }
    }
    
    private fun showAlarmOptionsDialog(position: Int) {
        try {
            val options = arrayOf(
                getString(R.string.edit_alarm),
                getString(R.string.delete_alarm)
            )
            
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.alarm_name))
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            (activity as? MainActivity)?.openAddAlarmActivity()
                        }
                        1 -> {
                            showDeleteConfirmation(position)
                        }
                    }
                }
                .show()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing alarm options dialog", e)
        }
    }
    
    private fun showDeleteConfirmation(position: Int) {
        try {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Alarmı Sil")
                .setMessage("Bu alarmı silmek istediğinizden emin misiniz?")
                .setPositiveButton("Sil") { _, _ ->
                    try {
                        (activity as? MainActivity)?.deleteAlarm(position)
                        loadAlarms()
                        
                        _binding?.let { binding ->
                            // Success animation
                            val scaleAnimation = ObjectAnimator.ofFloat(binding.cardAlarms, "scaleX", 1.0f, 0.98f, 1.0f)
                            scaleAnimation.duration = 200
                            scaleAnimation.start()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error deleting alarm", e)
                    }
                }
                .setNegativeButton("İptal", null)
                .show()
        } catch (e: Exception) {
            Log.e(TAG, "Error showing delete confirmation", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            stopClock()
            _binding = null
        } catch (e: Exception) {
            Log.e(TAG, "Error in onDestroyView", e)
        }
    }
} 