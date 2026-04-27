import org.json.JSONObject

data class Appointment(
    val doctorName: String,
    val doctorSpecialty: String,
    val date: String,
    val time: String
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("doctorName", doctorName)
        put("doctorSpecialty", doctorSpecialty)
        put("date", date)
        put("time", time)
    }

    companion object {
        fun fromJson(json: JSONObject): Appointment = Appointment(
            doctorName = json.getString("doctorName"),
            doctorSpecialty = json.getString("doctorSpecialty"),
            date = json.getString("date"),
            time = json.getString("time")
        )
    }
}