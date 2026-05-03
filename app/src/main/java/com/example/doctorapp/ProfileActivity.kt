package com.example.doctorapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    // Поля ввода
    private lateinit var editFullName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPhone: TextInputEditText
    private lateinit var editBirthDate: TextInputEditText
    private lateinit var editPassportNumber: TextInputEditText
    private lateinit var editInsuranceNumber: TextInputEditText
    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        sharedPrefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)

        // Инициализация View
        editFullName = findViewById(R.id.editFullName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        editBirthDate = findViewById(R.id.editBirthDate)
        editPassportNumber = findViewById(R.id.editPassportNumber)
        editInsuranceNumber = findViewById(R.id.editInsuranceNumber)
        saveButton = findViewById(R.id.saveButton)

        // Загружаем сохранённые данные
        loadUserData()

        // Настройка выбора даты
        setupDatePicker()

        // Обработчик сохранения
        saveButton.setOnClickListener {
            saveUserData()
        }

        // Нижняя навигация
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.profile
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
                    // уже на этом экране, можно ничего не делать или перезапустить
                    true
                }

                else -> true
            }
        }
    }

    private fun loadUserData() {
        editFullName.setText(sharedPrefs.getString("full_name", ""))
        editEmail.setText(sharedPrefs.getString("email", ""))
        editPhone.setText(sharedPrefs.getString("phone", ""))
        editBirthDate.setText(sharedPrefs.getString("birth_date", ""))
        editPassportNumber.setText(sharedPrefs.getString("passport", ""))
        editInsuranceNumber.setText(sharedPrefs.getString("insurance", ""))
    }

    private fun saveUserData() {
        val fullName = editFullName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val phone = editPhone.text.toString().trim()
        val birthDate = editBirthDate.text.toString().trim()
        val passport = editPassportNumber.text.toString().trim()
        val insurance = editInsuranceNumber.text.toString().trim()

        with(sharedPrefs.edit()) {
            putString("full_name", fullName)
            putString("email", email)
            putString("phone", phone)
            putString("birth_date", birthDate)
            putString("passport", passport)
            putString("insurance", insurance)
            apply()
        }

        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
    }

    private fun setupDatePicker() {
        editBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
                editBirthDate.setText(formattedDate)
            }, year, month, day).show()
        }
    }
}