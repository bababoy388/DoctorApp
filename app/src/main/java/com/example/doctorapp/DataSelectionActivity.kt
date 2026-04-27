package com.example.doctorapp

import Appointment
import AppointmentPreferences
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DataSelectionActivity : AppCompatActivity() {

    private var selectedDate: String? = null
    private lateinit var doctorName: String
    private lateinit var doctorSpecialty: String
    private lateinit var prefs: AppointmentPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_date_selection)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        prefs = AppointmentPreferences(this)
        doctorName = intent.getStringExtra("DOCTOR_NAME") ?: "Неизвестно"
        doctorSpecialty = intent.getStringExtra("DOCTOR_SPECIALTY") ?: ""

        val nextButton = findViewById<Button>(R.id.nextButton)
        val timeChipGroup = findViewById<ChipGroup>(R.id.timeChipGroup)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth.${month + 1}.$year"
        }

        timeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            nextButton.isEnabled = checkedIds.isNotEmpty()
        }

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

        nextButton.setOnClickListener {
            val selectedChipId = timeChipGroup.checkedChipId
            val selectedTime = if (selectedChipId != -1) {
                findViewById<Chip>(selectedChipId).text.toString()
            } else "не выбрано"

            val appointment = Appointment(
                doctorName = doctorName,
                doctorSpecialty = doctorSpecialty,
                date = selectedDate ?: "дата не выбрана",
                time = selectedTime
            )

            prefs.addAppointment(appointment)

            startActivity(Intent(this, AppointmentsActivity::class.java))
            overridePendingTransition(0, 0) // отключаем анимацию
        }
    }
}