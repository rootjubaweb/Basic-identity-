package com.example.model

enum class PlayStoreMode {
    BLACKLIST,
    WHITELIST
}

enum class PasswordScope {
    SCOPE_PROFILE,
    SCOPE_DEVICE,
    SCOPE_UNSPECIFIED
}

data class AndroidPolicy(
    val playStoreMode: PlayStoreMode = PlayStoreMode.BLACKLIST,
    val passwordScope: PasswordScope = PasswordScope.SCOPE_PROFILE,
    val cameraDisabled: Boolean = false,
    val screenCaptureDisabled: Boolean = false,
    val crossProfileCopyPasteAllowed: Boolean = true,
    val installUnknownSourcesAllowed: Boolean = true,
    val modifyAccountsDisabled: Boolean = false,
    val factoryResetDisabled: Boolean = false,
    val autoTimeRequired: Boolean = false
)

data class SavedEnrollment(
    val id: String = java.util.UUID.randomUUID().toString(),
    val projectId: String,
    val enrollmentToken: String,
    val createdAt: Long = System.currentTimeMillis(),
    val policySummary: String = "BLACKLIST"
)

data class ExtractionResult(
    val success: Boolean,
    val token: String? = null,
    val error: String? = null
)

data class ProjectValidationResult(
    val valid: Boolean,
    val message: String? = null
)
