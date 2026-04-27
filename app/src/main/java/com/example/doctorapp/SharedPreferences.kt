import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

class AppointmentPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("appointments_prefs", Context.MODE_PRIVATE)

    fun saveAppointments(appointments: List<Appointment>) {
        val jsonArray = JSONArray()
        appointments.forEach { jsonArray.put(it.toJson()) }
        prefs.edit().putString(KEY_APPOINTMENTS, jsonArray.toString()).apply()
    }

    fun loadAppointments(): List<Appointment> {
        val jsonString = prefs.getString(KEY_APPOINTMENTS, null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<Appointment>()
        for (i in 0 until jsonArray.length()) {
            list.add(Appointment.fromJson(jsonArray.getJSONObject(i)))
        }
        return list
    }

    fun addAppointment(appointment: Appointment) {
        val appointments = loadAppointments().toMutableList()
        appointments.add(appointment)
        saveAppointments(appointments)
    }

    companion object {
        private const val KEY_APPOINTMENTS = "appointments"
    }
}