package com.example.doctorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val isRegistered = sharedPref.getString("email", null) != null

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (isRegistered) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            overridePendingTransition(0, 0) // отключаем анимацию
            finish()
        }, 2000) // задержка 2 секунды
    }
}