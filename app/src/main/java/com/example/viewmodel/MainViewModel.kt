package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.EnrollmentRepository
import com.example.model.AndroidPolicy
import com.example.model.ExtractionResult
import com.example.model.PlayStoreMode
import com.example.model.SavedEnrollment
import com.example.util.TokenParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val titleAr: String, val titleEn: String) {
    WIZARD("الدليل التفاعلي", "Step Wizard"),
    EXTRACTOR("مستخرج الرمز", "Token Extractor"),
    POLICY("تخصيص السياسة", "Policy"),
    QR("رمز الاستجابة QR", "QR Code"),
    SCRIPTS("مركز الأكواد", "Scripts"),
    GUIDE("دليل الهاتف", "Phone Guide"),
    TROUBLESHOOT("حل المشكلات", "Troubleshoot"),
    SAVED("السجلات المحفوظة", "Saved")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EnrollmentRepository(application.applicationContext)

    val savedEnrollments: StateFlow<List<SavedEnrollment>> = repository.enrollmentsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeTab = MutableStateFlow(AppTab.WIZARD)
    val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

    private val _projectId = MutableStateFlow("android-work-profile")
    val projectId: StateFlow<String> = _projectId.asStateFlow()

    private val _enterpriseToken = MutableStateFlow("")
    val enterpriseToken: StateFlow<String> = _enterpriseToken.asStateFlow()

    private val _enrollmentToken = MutableStateFlow("")
    val enrollmentToken: StateFlow<String> = _enrollmentToken.asStateFlow()

    private val _policy = MutableStateFlow(AndroidPolicy())
    val policy: StateFlow<AndroidPolicy> = _policy.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun selectTab(tab: AppTab) {
        _activeTab.value = tab
    }

    fun setProjectId(id: String) {
        _projectId.value = id
    }

    fun setEnterpriseToken(token: String) {
        _enterpriseToken.value = token
    }

    fun setEnrollmentToken(token: String) {
        _enrollmentToken.value = token
    }

    fun updatePolicy(newPolicy: AndroidPolicy) {
        _policy.value = newPolicy
    }

    fun extractEnterpriseTokenFromInput(input: String): ExtractionResult {
        val res = TokenParser.extractEnterpriseToken(input)
        if (res.success && !res.token.isNullOrBlank()) {
            _enterpriseToken.value = res.token
        }
        return res
    }

    fun saveCurrentEnrollmentToken(onComplete: (Boolean) -> Unit = {}) {
        val token = _enrollmentToken.value.trim()
        if (token.isEmpty()) {
            _toastMessage.value = "يرجى إدخال رمز المشرف أولاً"
            onComplete(false)
            return
        }
        val summary = _policy.value.playStoreMode.name
        viewModelScope.launch {
            repository.saveEnrollment(
                projectId = _projectId.value,
                enrollmentToken = token,
                policySummary = summary
            )
            _toastMessage.value = "تم حفظ رمز المشرف بنجاح!"
            onComplete(true)
        }
    }

    fun deleteSavedEnrollment(id: String) {
        viewModelScope.launch {
            repository.deleteEnrollment(id)
            _toastMessage.value = "تم حذف الرمز من السجل"
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
