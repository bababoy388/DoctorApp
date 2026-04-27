package com.example.doctorapp

import Appointment
import AppointmentPreferences
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppointmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_appointments)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val prefs = AppointmentPreferences(this)
        val appointments = prefs.loadAppointments()

        val rvAppointments = findViewById<RecyclerView>(R.id.rvAppointments)
        rvAppointments.adapter = AppointmentAdapter(appointments)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.appointment -> {
                    startActivity(Intent(this, AppointmentsActivity::class.java))
                    overridePendingTransition(0, 0) // отключаем анимацию
                    true
                }

                R.id.signup -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0) // отключаем анимацию
                    true
                }

                R.id.profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0) // отключаем анимацию
                    true
                }

                else -> true
            }
        }
    }

    // Адаптер внутри активности (можно вынести отдельно)
    class AppointmentAdapter(
        private val appointments: List<Appointment>
    ) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvDoctorName: TextView = view.findViewById(R.id.tvDoctorName)
            val tvSpecialty: TextView = view.findViewById(R.id.tvSpecialty)
            val tvDate: TextView = view.findViewById(R.id.tvDate)
            val tvTime: TextView = view.findViewById(R.id.tvTime)
            // ivDoctor можно оставить без изменений
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_appointment, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val appointment = appointments[position]
            holder.tvDoctorName.text = appointment.doctorName
            holder.tvSpecialty.text = appointment.doctorSpecialty
            holder.tvDate.text = appointment.date
            holder.tvTime.text = appointment.time
        }

        override fun getItemCount() = appointments.size
    }
}