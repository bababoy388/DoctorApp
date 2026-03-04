package com.example.doctorapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Onboarding2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_2)
        enableEdgeToEdge()

        val btnNext = findViewById<TextView>(R.id.button)

        btnNext.setOnClickListener {
            val intent = Intent(this, Onboarding3Activity::class.java)
            startActivity(intent)
        }

    }
}