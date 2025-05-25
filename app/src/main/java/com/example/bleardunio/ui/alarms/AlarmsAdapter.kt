package com.example.bleardunio.ui.alarms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bleardunio.R

/**
 * Adapter for displaying alarms in a RecyclerView.
 * Each alarm contains a time and medication name.
 */
class AlarmsAdapter(
    private var alarms: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAlarmTime: TextView = itemView.findViewById(R.id.tvAlarmTime)
        val tvAlarmName: TextView = itemView.findViewById(R.id.tvAlarmName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        
        // Parse the alarm format: "time | medication"
        val parts = alarm.split(" | ")
        
        if (parts.size >= 2) {
            val time = parts[0]
            val medication = parts[1]
            
            holder.tvAlarmTime.text = time
            holder.tvAlarmName.text = medication
        } else {
            // Fallback if the format is unexpected
            holder.tvAlarmTime.text = alarm
            holder.tvAlarmName.text = ""
        }
        
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = alarms.size

    /**
     * Updates the adapter with a new list of alarms
     */
    fun updateAlarms(newAlarms: List<String>) {
        this.alarms = newAlarms
        notifyDataSetChanged()
    }
} 