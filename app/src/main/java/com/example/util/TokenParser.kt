package com.example.util

import com.example.model.AndroidPolicy
import com.example.model.ExtractionResult
import com.example.model.PlayStoreMode
import com.example.model.ProjectValidationResult
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

object TokenParser {

    fun extractEnterpriseToken(input: String?): ExtractionResult {
        if (input.isNullOrBlank()) {
            return ExtractionResult(success = false, error = "المدخل فارغ")
        }

        val cleaned = input.trim()
        if (cleaned.isEmpty()) {
            return ExtractionResult(success = false, error = "يرجى إدخال الرابط أو الرمز")
        }

        // Direct token paste without URL delimiters
        if (!cleaned.contains("/") && !cleaned.contains("?") && !cleaned.contains("&") && !cleaned.contains("=")) {
            if (cleaned.length < 10) {
                return ExtractionResult(success = false, error = "الرمز قصير جداً، يرجى التأكد من نسخه بالكامل")
            }
            return ExtractionResult(success = true, token = cleaned)
        }

        // Regex for enterpriseToken parameter in query or fragment
        val pattern = Pattern.compile("(?:[?&#]enterpriseToken=)([^&#\\s\"']+)", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(cleaned)
        if (matcher.find()) {
            val tokenGroup = matcher.group(1)
            if (!tokenGroup.isNullOrBlank()) {
                val decoded = try {
                    URLDecoder.decode(tokenGroup.trim(), StandardCharsets.UTF_8.name())
                } catch (e: Exception) {
                    tokenGroup.trim()
                }
                if (decoded.length > 5) {
                    return ExtractionResult(success = true, token = decoded)
                }
            }
        }

        // Check fallback for token=...
        val tokenParamPattern = Pattern.compile("(?:[?&#]token=)([^&#\\s\"']+)", Pattern.CASE_INSENSITIVE)
        val tokenMatcher = tokenParamPattern.matcher(cleaned)
        if (tokenMatcher.find()) {
            val tokenVal = tokenMatcher.group(1)
            if (!tokenVal.isNullOrBlank() && tokenVal.length > 5) {
                return ExtractionResult(success = true, token = tokenVal.trim())
            }
        }

        // Check for Google OAuth / Enterprise Token pattern (ya29.)
        val googleTokenPattern = Pattern.compile("(ya29\\.[a-zA-Z0-9_-]+)")
        val googleMatcher = googleTokenPattern.matcher(cleaned)
        if (googleMatcher.find()) {
            val googleToken = googleMatcher.group(1)
            if (!googleToken.isNullOrBlank()) {
                return ExtractionResult(success = true, token = googleToken)
            }
        }

        return ExtractionResult(
            success = false,
            error = "لم نتمكن من العثور على enterpriseToken في الرابط المُدخل. تأكد من نسخ رابط localhost بالكامل من شريط العنوان بعد الموافقة."
        )
    }

    fun validateProjectId(id: String): ProjectValidationResult {
        val trimmed = id.trim()
        if (trimmed.isEmpty()) {
            return ProjectValidationResult(valid = false, message = "معرف المشروع مطلوب")
        }
        if (trimmed.length < 6 || trimmed.length > 30) {
            return ProjectValidationResult(valid = false, message = "معرف مشروع Google Cloud يجب أن يكون بين 6 و 30 حرفاً")
        }
        val regex = Regex("^[a-z][a-z0-9-]*[a-z0-9]$")
        if (!regex.matches(trimmed)) {
            return ProjectValidationResult(
                valid = false,
                message = "معرف المشروع يجب أن يبدأ بحرف صغير بالإنجليزية ويحتوي فقط على حروف صغيرة وأرقام وشُرطات (-)"
            )
        }
        return ProjectValidationResult(valid = true)
    }

    fun buildPolicyJson(policy: AndroidPolicy): String {
        val crossProfile = if (policy.crossProfileCopyPasteAllowed) {
            "\"COPY_PASTE_TO_WORK_PROFILE_ONLY\""
        } else {
            "\"CROSS_PROFILE_COPY_PASTE_ALLOWED\""
        }

        val entries = mutableListOf<String>()
        entries.add("    \"playStoreMode\": \"${policy.playStoreMode.name}\"")
        entries.add("    \"passwordPolicies\": [\n        {\n            \"passwordScope\": \"${policy.passwordScope.name}\"\n        }\n    ]")
        if (policy.cameraDisabled) entries.add("    \"cameraDisabled\": true")
        if (policy.screenCaptureDisabled) entries.add("    \"screenCaptureDisabled\": true")
        if (policy.installUnknownSourcesAllowed) entries.add("    \"installUnknownSourcesAllowed\": true")
        entries.add("    \"crossProfileCopyPaste\": $crossProfile")
        if (policy.modifyAccountsDisabled) entries.add("    \"modifyAccountsDisabled\": true")
        if (policy.factoryResetDisabled) entries.add("    \"factoryResetDisabled\": true")
        if (policy.autoTimeRequired) entries.add("    \"autoTimeRequired\": true")

        return "{\n" + entries.joinToString(",\n") + "\n}"
    }

    fun generateColabStep1Code(projectId: String): String {
        val pId = projectId.trim().ifEmpty { "YOUR_PROJECT_ID" }
        return """
from google.colab import auth
from googleapiclient.discovery import build

# 1. تسجيل الدخول بحساب جوجل الخاص بك
auth.authenticate_user()

# 2. ضع معرف المشروع (Project ID) الخاص بك هنا
PROJECT_ID = "$pId" 

# 3. بناء اتصال مع Android Management API
androidmanagement = build('androidmanagement', 'v1')

# 4. إنشاء رابط تسجيل المؤسسة المجانية
signup_url = androidmanagement.signupUrls().create(
    projectId=PROJECT_ID,
    callbackUrl="https://localhost"
).execute()

print("==================================================")
print("1. افتح هذا الرابط في المتصفح وأكمل خطوة الموافقة:")
print(signup_url['url'])
print("==================================================")
print("\n2. بعد الموافقة، سيحولك المتصفح لصفحة لا تعمل؛")
print("انسخ الكود الموجود بعد كلمة enterpriseToken= من شريط العنوان في الأعلى.")
""".trimIndent()
    }

    fun generateColabStep2Code(
        projectId: String,
        enterpriseToken: String,
        policy: AndroidPolicy
    ): String {
        val pId = projectId.trim().ifEmpty { "YOUR_PROJECT_ID" }
        val token = enterpriseToken.trim().ifEmpty { "ENTER_YOUR_TOKEN_HERE" }
        val policyJson = buildPolicyJson(policy)

        return """
ENTERPRISING_TOKEN = "$token"

# 1. إكمال إنشاء المؤسسة وربطها بالمشروع
enterprise = androidmanagement.enterprises().create(
    projectId="$pId",
    signupUrlName=signup_url['name'],
    enterpriseToken=ENTERPRISING_TOKEN,
    body={}
).execute()

enterprise_name = enterprise['name']
print(f"✅ تم إنشاء المؤسسة بنجاح: {enterprise_name}")

# 2. إعداد سياسة أندرويد لملف العمل
policy_body = $policyJson

androidmanagement.enterprises().policies().patch(
    name=f"{enterprise_name}/policies/default",
    body=policy_body
).execute()
print("✅ تم حفظ وتطبيق سياسة ملف العمل (default policy)")

# 3. توليد رمز المشرف الفعلي (Enrollment Token)
token = androidmanagement.enterprises().enrollmentTokens().create(
    parent=enterprise_name,
    body={"policyName": f"{enterprise_name}/policies/default"}
).execute()

print("\n" + "=" * 50)
print("🎉 رمز المشرف الخاص بك هو:")
print(token['value'])
print("=" * 50)
print("\n📌 ادخل هذا الرمز في شاشة إعداد ملف العمل على هاتفك لتفعيله فوراً.")
""".trimIndent()
    }

    fun generateStandalonePythonScript(projectId: String, policy: AndroidPolicy): String {
        val pId = projectId.trim().ifEmpty { "YOUR_PROJECT_ID" }
        val policyJson = buildPolicyJson(policy)

        return """
#!/usr/bin/env python3
""${'"'}
Android Enterprise Token Generator
Script to generate Android Management API Enrollment Token for Work Profile setup.
Requirements: pip install google-api-python-client google-auth-oauthlib google-auth-httplib2
""${'"'}

import os
import sys
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build

SCOPES = ['https://www.googleapis.com/auth/androidmanagement']
PROJECT_ID = "$pId"

def main():
    print("=== Android Enterprise Enrollment Token Generator ===")
    
    # 1. المصادقة عبر المتصفح
    flow = InstalledAppFlow.from_client_secrets_file(
        'client_secrets.json', SCOPES
    )
    creds = flow.run_local_server(port=0)
    androidmanagement = build('androidmanagement', 'v1', credentials=creds)

    # 2. إنشاء رابط التسجيل
    print("[1/4] جاري إنشاء رابط تسجيل المؤسسة...")
    signup_url = androidmanagement.signupUrls().create(
        projectId=PROJECT_ID,
        callbackUrl="https://localhost"
    ).execute()

    print(f"\nافتح الرابط التالي في متصفحك للموافقة:")
    print(signup_url['url'])
    print("\nبعد الموافقة، انسخ قيمة enterpriseToken من شريط العنوان:")
    
    enterprise_token = input("\nضع رمز enterpriseToken هنا: ").strip()

    # 3. إنشاء المؤسسة
    print("\n[2/4] جاري إنشاء وتفعيل المؤسسة...")
    enterprise = androidmanagement.enterprises().create(
        projectId=PROJECT_ID,
        signupUrlName=signup_url['name'],
        enterpriseToken=enterprise_token,
        body={}
    ).execute()
    enterprise_name = enterprise['name']
    print(f"المؤسسة: {enterprise_name}")

    # 4. تطبيق السياسة
    print("[3/4] جاري حفظ السياسة المخصصة...")
    policy_body = $policyJson
    androidmanagement.enterprises().policies().patch(
        name=f"{enterprise_name}/policies/default",
        body=policy_body
    ).execute()

    # 5. توليد رمز المشرف
    print("[4/4] جاري توليد رمز المشرف النهائي...")
    token = androidmanagement.enterprises().enrollmentTokens().create(
        parent=enterprise_name,
        body={"policyName": f"{enterprise_name}/policies/default"}
    ).execute()

    print("\n" + "=" * 50)
    print("✅ تم توليد رمز المشرف بنجاح:")
    print(token['value'])
    print("=" * 50)

if __name__ == '__main__':
    main()
""".trimIndent()
    }

    fun generateAndroidDpcQrPayload(enrollmentToken: String): String {
        val token = enrollmentToken.trim()
        return """{
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME": "com.google.android.apps.work.clouddpc/.receivers.CloudDeviceAdminReceiver",
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM": "I5YvS0O5hXY46mb01BlRjq4oUC9W7Dp50ed857nvg6Y=",
  "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION": "https://play.google.com/managed/downloadManagingApp?hl=en",
  "android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE": {
    "com.google.android.apps.work.clouddpc.EXTRA_ENROLLMENT_TOKEN": "$token"
  },
  "android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED": true
}"""
    }
}
