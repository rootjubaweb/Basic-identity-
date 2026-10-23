package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.SavedEnrollment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class EnrollmentRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("saved_enrollments_prefs", Context.MODE_PRIVATE)

    private val _enrollmentsFlow = MutableStateFlow<List<SavedEnrollment>>(emptyList())
    val enrollmentsFlow: Flow<List<SavedEnrollment>> = _enrollmentsFlow.asStateFlow()

    init {
        loadEnrollments()
    }

    private fun loadEnrollments() {
        val jsonString = prefs.getString(KEY_SAVED_TOKENS, null) ?: "[]"
        val list = mutableListOf<SavedEnrollment>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    SavedEnrollment(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        projectId = obj.optString("projectId", ""),
                        enrollmentToken = obj.optString("enrollmentToken", ""),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                        policySummary = obj.optString("policySummary", "BLACKLIST")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _enrollmentsFlow.value = list
    }

    private fun persist(list: List<SavedEnrollment>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("projectId", item.projectId)
                put("enrollmentToken", item.enrollmentToken)
                put("createdAt", item.createdAt)
                put("policySummary", item.policySummary)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_SAVED_TOKENS, jsonArray.toString()).apply()
        _enrollmentsFlow.value = list
    }

    fun saveEnrollment(projectId: String, enrollmentToken: String, policySummary: String) {
        val current = _enrollmentsFlow.value.toMutableList()
        val newRecord = SavedEnrollment(
            projectId = projectId.ifBlank { "android-work-profile" },
            enrollmentToken = enrollmentToken.trim(),
            policySummary = policySummary
        )
        // Add at the beginning of the list
        current.add(0, newRecord)
        persist(current)
    }

    fun deleteEnrollment(id: String) {
        val updated = _enrollmentsFlow.value.filterNot { it.id == id }
        persist(updated)
    }

    companion object {
        private const val KEY_SAVED_TOKENS = "key_saved_enrollment_tokens"
    }
}
