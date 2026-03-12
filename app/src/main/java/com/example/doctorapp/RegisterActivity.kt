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

class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        enableEdgeToEdge()

        sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        // Поле логина (editText1) не используется
        editTextEmail = findViewById(R.id.editText2)
        editTextPassword = findViewById(R.id.editText3)

        val login = findViewById<TextView>(R.id.router_login)
        val btnReg = findViewById<TextView>(R.id.button)

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnReg.setOnClickListener {
            if (validateFields()) {
                checkUserAndProceed()
            }
        }
    }

    private fun validateFields(): Boolean {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        return when {
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

    private fun checkUserAndProceed() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        val savedEmail = sharedPref.getString("email", null)
        val savedPassword = sharedPref.getString("password", null)

        if (email == savedEmail && password == savedPassword) {
            // Данные верны – переходим на главный экран
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
        }
    }
}