package com.example.bleardunio

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import java.io.IOException
import java.util.UUID

class MainActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: MainActivity? = null
    }

    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val deviceName = "HC-05"
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null

    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Set the static instance
        instance = this

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        
        // Get bottom navigation view
        bottomNavView = findViewById(R.id.bottom_navigation)

        // Initialize the bottom navigation
        setupBottomNavigation()
    }

    fun getAlarms(): MutableList<String> {
        val shared = getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val json = shared.getString("alarmList", "[]")
        val jsonArray = JSONArray(json)
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

    fun saveAlarms(alarms: List<String>) {
        val shared = getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        for (alarm in alarms) {
            jsonArray.put(alarm)
        }
        shared.edit().putString("alarmList", jsonArray.toString()).apply()
    }

    fun deleteAlarm(position: Int) {
        val alarms = getAlarms()
        if (position < alarms.size) {
            alarms.removeAt(position)
            saveAlarms(alarms)
            Toast.makeText(this, getString(R.string.success_alarm_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    fun sendAlarmToArduino(alarm: String) {
        // Check permissions first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, 1)
            return
        }
        
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val device = pairedDevices?.firstOrNull { it.name == deviceName }

        if (device != null) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                bluetoothSocket?.connect()

                // The alarm parameter now contains both time and medication name separated by ':'
                val command = "SET:$alarm\n"
                bluetoothSocket?.outputStream?.write(command.toByteArray())

                Toast.makeText(this, "Alarm gönderildi: $command", Toast.LENGTH_SHORT).show()
                bluetoothSocket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Bluetooth gönderim hatası!", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "HC-05 bağlı değil", Toast.LENGTH_LONG).show()
        }
    }

    private fun hasPermissions(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setupBottomNavigation() {
        // Properly get NavController from NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup action bar with nav controller
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_alarms,
                R.id.navigation_profile,
                R.id.navigation_settings
            )
        )

        // Hide the action bar since we have a custom design
        supportActionBar?.hide()

        // Connect the bottom navigation with the navigation controller
        bottomNavView.setupWithNavController(navController)
    }

    /**
     * Method to navigate to the alarms tab
     * Used by HomeFragment to navigate when "Add Alarm" is clicked
     */
    fun navigateToAlarms() {
        bottomNavView.selectedItemId = R.id.navigation_alarms
    }

    /**
     * Method to open the Add Alarm Activity
     */
    fun openAddAlarmActivity() {
        val intent = Intent(this, AddAlarmActivity::class.java)
        startActivity(intent)
    }

    /**
     * Method to navigate to the home tab
     */
    fun navigateToHome() {
        bottomNavView.selectedItemId = R.id.navigation_home
    }

    /**
     * Override onSupportNavigateUp to handle navigation properly
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
