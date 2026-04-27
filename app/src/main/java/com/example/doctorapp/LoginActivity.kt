package com.example.doctorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editTextLogin: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enableEdgeToEdge()

        sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        editTextLogin = findViewById(R.id.editText1)
        editTextEmail = findViewById(R.id.editText2)
        editTextPassword = findViewById(R.id.editText3)

        val register = findViewById<TextView>(R.id.router_register)
        val btnLog = findViewById<TextView>(R.id.button)

        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(0, 0) // отключаем анимацию
        }

        btnLog.setOnClickListener {
            if (validateFields()) {
                saveData()
                startActivity(Intent(this, Onboarding1Activity::class.java))
                overridePendingTransition(0, 0) // отключаем анимацию
            }
        }
    }

    private fun validateFields(): Boolean {
        val login = editTextLogin.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        return when {
            login.isEmpty() -> {
                Toast.makeText(this, "Поле «Логин» не заполнено", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Поле «Электронная почта» не заполнено", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Поле «Пароль» не заполнено", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun saveData() {
        val login = editTextLogin.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        with(sharedPref.edit()) {
            putString("login", login)
            putString("email", email)
            putString("password", password)
            apply()
        }
    }
}