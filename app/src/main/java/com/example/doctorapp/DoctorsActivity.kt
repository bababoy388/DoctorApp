package com.example.doctorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class DoctorsActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctors)

        sharedPrefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val doctor1 = findViewById<LinearLayout>(R.id.doctor1)
        val doctor2 = findViewById<LinearLayout>(R.id.doctor2)
        val doctor3 = findViewById<LinearLayout>(R.id.doctor3)
        val doctor4 = findViewById<LinearLayout>(R.id.doctor4)
        val doctor5 = findViewById<LinearLayout>(R.id.doctor5)

        fun getDoctorName(layout: LinearLayout): String {
            val innerLayout = layout.getChildAt(1) as LinearLayout
            val nameView = innerLayout.getChildAt(0) as TextView
            return nameView.text.toString()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.appointment -> {
                    startActivity(Intent(this, AppointmentsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.signup -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> true
            }
        }

        fun getDoctorSpecialty(layout: LinearLayout): String {
            val innerLayout = layout.getChildAt(1) as LinearLayout
            val specialtyView = innerLayout.getChildAt(1) as TextView
            return specialtyView.text.toString()
        }

        // Проверяем, заполнен ли профиль
        fun isProfileComplete(): Boolean {
            val fullName = sharedPrefs.getString("full_name", "")
            val email = sharedPrefs.getString("email", "")
            val phone = sharedPrefs.getString("phone", "")
            val birthDate = sharedPrefs.getString("birth_date", "")
            val passport = sharedPrefs.getString("passport", "")
            val insurance = sharedPrefs.getString("insurance", "")

            return !(fullName.isNullOrEmpty() || email.isNullOrEmpty() || phone.isNullOrEmpty() ||
                    birthDate.isNullOrEmpty() || passport.isNullOrEmpty() || insurance.isNullOrEmpty())
        }

        val clickListener: (String, String) -> View.OnClickListener = { name, specialty ->
            View.OnClickListener {
                if (isProfileComplete()) {
                    val intent = Intent(this, DataSelectionActivity::class.java).apply {
                        putExtra("DOCTOR_NAME", name)
                        putExtra("DOCTOR_SPECIALTY", specialty)
                    }
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                } else {
                    Toast.makeText(
                        this,
                        "Пожалуйста, заполните профиль (ФИО, email, телефон, дата рождения, паспорт, полис)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        doctor1.setOnClickListener(clickListener(getDoctorName(doctor1), getDoctorSpecialty(doctor1)))
        doctor2.setOnClickListener(clickListener(getDoctorName(doctor2), getDoctorSpecialty(doctor2)))
        doctor3.setOnClickListener(clickListener(getDoctorName(doctor3), getDoctorSpecialty(doctor3)))
        doctor4.setOnClickListener(clickListener(getDoctorName(doctor4), getDoctorSpecialty(doctor4)))
        doctor5.setOnClickListener(clickListener(getDoctorName(doctor5), getDoctorSpecialty(doctor5)))
    }
}