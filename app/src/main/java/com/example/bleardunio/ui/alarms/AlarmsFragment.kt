package com.example.bleardunio.ui.alarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bleardunio.MainActivity
import com.example.bleardunio.R
import com.example.bleardunio.databinding.FragmentAlarmsBinding
import org.json.JSONArray

class AlarmsFragment : Fragment() {

    private var _binding: FragmentAlarmsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        // Add alarm button click
        binding.btnSetAlarm.setOnClickListener {
            if (validateAlarmInput()) {
                saveAlarm()
                // Ana sayfaya yönlendir
                (activity as? MainActivity)?.navigateToHome()
            }
        }
    }
    
    private fun validateAlarmInput(): Boolean {
        // For now, just ensure the alarm name is not empty
        val alarmName = binding.etAlarmName.text.toString().trim()
        if (alarmName.isEmpty()) {
            Toast.makeText(context, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
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
        val mainActivity = activity as? MainActivity
        mainActivity?.let {
            // Format as required for Arduino: time and medication name
            val alarmData = "$alarmTime:$medicationName"
            it.sendAlarmToArduino(alarmData)
        }
        
        // Clear the input field
        binding.etAlarmName.setText("")
        
        // Show success message
        Toast.makeText(context, getString(R.string.success_alarm_saved), Toast.LENGTH_SHORT).show()
    }
    
    private fun getAlarms(): List<String> {
        val sharedPrefs = requireActivity().getSharedPreferences("alarms", 0)
        val json = sharedPrefs.getString("alarmList", "[]")
        val jsonArray = JSONArray(json)
        val list = mutableListOf<String>()
        
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        
        return list
    }
    
    private fun saveAlarms(alarms: List<String>) {
        val sharedPrefs = requireActivity().getSharedPreferences("alarms", 0)
        val jsonArray = JSONArray()
        
        for (alarm in alarms) {
            jsonArray.put(alarm)
        }
        
        sharedPrefs.edit().putString("alarmList", jsonArray.toString()).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}