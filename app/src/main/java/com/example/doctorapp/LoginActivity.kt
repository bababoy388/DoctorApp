package com.example.doctorapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enableEdgeToEdge()

        val register = findViewById<TextView>(R.id.router_register)
        val btnLog = findViewById<TextView>(R.id.button)

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLog.setOnClickListener {
            val intent = Intent(this, Onboarding1Activity::class.java)
            startActivity(intent)
        }

    }
}