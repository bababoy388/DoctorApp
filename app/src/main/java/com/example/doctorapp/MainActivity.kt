package com.example.doctorapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Статус-бар и паддинги
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        // Список поликлиник Красноярска
        val clinics = listOf(
            Clinic(
                "Краевая клиническая больница",
                "ул. Партизана Железняка, 3А",
                "Крупнейший многопрофильный стационар, консультативная поликлиника",
                R.drawable.clinic1
            ),
            Clinic(
                "Городская поликлиника №4",
                "ул. Копылова, 8",
                "Обслуживает взрослое население, дневной стационар, лаборатория",
                R.drawable.clinic2
            ),
            Clinic(
                "Городская поликлиника №14",
                "ул. Мичурина, 6",
                "Детская и взрослая поликлиника, УЗИ, функциональная диагностика",
                R.drawable.clinic3
            ),
            Clinic(
                "Городская поликлиника №7",
                "ул. Ленина, 67",
                "Современная взрослая поликлиника, центр города",
                R.drawable.clinic4
            ),
            Clinic(
                "Студенческая поликлиника №1",
                "ул. Курчатова, 1",
                "Обслуживает студентов и преподавателей, узкие специалисты",
                R.drawable.clinic5
            )
        )

        // Инициализация RecyclerView
        val rvClinics = findViewById<RecyclerView>(R.id.rv_clinics)
        rvClinics.adapter = ClinicAdapter(clinics) { selectedClinic ->
            // При выборе поликлиники переходим к списку врачей
            val intent = Intent(this, DoctorsActivity::class.java).apply {
                putExtra("CLINIC_NAME", selectedClinic.name)
                putExtra("CLINIC_ADDRESS", selectedClinic.address)
            }
            startActivity(intent)
            overridePendingTransition(0, 0) // отключаем анимацию
        }

        // Нижняя навигация
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.signup
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
    }

    // Data-класс для поликлиники
    data class Clinic(
        val name: String,
        val address: String,
        val description: String,
        val imageRes: Int
    )

    // Адаптер для списка поликлиник
    class ClinicAdapter(
        private val clinics: List<Clinic>,
        private val onItemClick: (Clinic) -> Unit
    ) : RecyclerView.Adapter<ClinicAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.tv_clinic_name)
            val address: TextView = view.findViewById(R.id.tv_address)
            val description: TextView = view.findViewById(R.id.tv_description)
            val image: ImageView = view.findViewById(R.id.iv_clinic)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_clinic, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val clinic = clinics[position]
            holder.name.text = clinic.name
            holder.address.text = clinic.address
            holder.description.text = clinic.description
            holder.image.setImageResource(clinic.imageRes)
            holder.itemView.setOnClickListener { onItemClick(clinic) }
        }

        override fun getItemCount() = clinics.size
    }
}