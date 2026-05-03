package com.example.doctorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DoctorsActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var rvDoctors: RecyclerView
    private lateinit var adapter: DoctorAdapter
    private var allDoctors = listOf<Doctor>()
    private var filteredDoctors = mutableListOf<Doctor>()

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

        // Инициализация списка врачей
        allDoctors = listOf(
            Doctor("Анна Иванова", "Терапевт", R.drawable.doctor1, 4.8f),
            Doctor("Михаил Петров", "Хирург", R.drawable.doctor1, 4.9f),
            Doctor("Елена Смирнова", "Педиатр", R.drawable.doctor1, 4.7f),
            Doctor("Александр Козлов", "Офтальмолог", R.drawable.doctor1, 4.9f),
            Doctor("Ольга Новикова", "Дерматолог", R.drawable.doctor1, 4.8f)
        )
        filteredDoctors = allDoctors.toMutableList()

        // RecyclerView
        rvDoctors = findViewById(R.id.rvDoctors)
        adapter = DoctorAdapter(filteredDoctors) { doctor ->
            onDoctorClick(doctor)
        }
        rvDoctors.adapter = adapter

        // Поиск
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterDoctors()
            }
        })

        // Чипы категорий
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategories)
        val categories = listOf("Все", "Терапевт", "Хирург", "Педиатр", "Офтальмолог", "Дерматолог")
        for (cat in categories) {
            val chip = Chip(this).apply {
                text = cat
                isCheckable = true
                id = View.generateViewId() // уникальный ID для каждого чипа
                setChipBackgroundColorResource(android.R.color.transparent)
                chipStrokeWidth = 1f
                chipStrokeColor = resources.getColorStateList(R.color.green, null)
            }
            chipGroup.addView(chip)
        }
        // Выбрать "Все" по умолчанию
        chipGroup.check(chipGroup.getChildAt(0).id)

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            filterDoctors()
        }

        // Нижняя навигация
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.signup // т.к. мы находимся в разделе "Запись"
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.appointment -> {
                    startActivity(Intent(this, AppointmentsActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    })
                    true
                }
                R.id.signup -> {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    })
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    })
                    true
                }
                else -> true
            }
        }
    }

    private fun filterDoctors() {
        val searchText = findViewById<EditText>(R.id.searchEditText).text.toString().trim().lowercase()
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategories)
        val selectedChipId = chipGroup.checkedChipId
        val selectedCategory = if (selectedChipId != -1) {
            findViewById<Chip>(selectedChipId)?.text?.toString() ?: "Все"
        } else {
            "Все"
        }

        filteredDoctors.clear()
        for (doctor in allDoctors) {
            val matchesSearch = doctor.name.lowercase().contains(searchText) ||
                    doctor.specialty.lowercase().contains(searchText)
            val matchesCategory = selectedCategory == "Все" || doctor.specialty == selectedCategory
            if (matchesSearch && matchesCategory) {
                filteredDoctors.add(doctor)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun onDoctorClick(doctor: Doctor) {
        if (isProfileComplete()) {
            val intent = Intent(this, DataSelectionActivity::class.java).apply {
                putExtra("DOCTOR_NAME", doctor.name)
                putExtra("DOCTOR_SPECIALTY", doctor.specialty)
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Пожалуйста, заполните профиль (ФИО, email, телефон, дата рождения, паспорт, полис)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isProfileComplete(): Boolean {
        val fullName = sharedPrefs.getString("full_name", "")
        val email = sharedPrefs.getString("email", "")
        val phone = sharedPrefs.getString("phone", "")
        val birthDate = sharedPrefs.getString("birth_date", "")
        val passport = sharedPrefs.getString("passport", "")
        val insurance = sharedPrefs.getString("insurance", "")

        return !(fullName.isNullOrEmpty() || email.isNullOrEmpty() || phone.isNullOrEmpty() ||
                birthDate.isNullOrEmpty() || passport.isNullOrEmpty() || insurance.isNullOrEmpty())
    }

    // Data-класс для врача
    data class Doctor(
        val name: String,
        val specialty: String,
        val imageRes: Int,
        val rating: Float
    )

    // Адаптер
    class DoctorAdapter(
        private var doctors: List<Doctor>,
        private val onItemClick: (Doctor) -> Unit
    ) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.tvDoctorName)
            val specialty: TextView = view.findViewById(R.id.tvSpecialty)
            val rating: TextView = view.findViewById(R.id.tvRating)
            val image: ImageView = view.findViewById(R.id.ivDoctor)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_doctor, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val doctor = doctors[position]
            holder.name.text = doctor.name
            holder.specialty.text = doctor.specialty
            holder.rating.text = String.format("%.1f", doctor.rating)
            holder.image.setImageResource(doctor.imageRes)
            holder.itemView.setOnClickListener { onItemClick(doctor) }
        }

        override fun getItemCount() = doctors.size
    }
}