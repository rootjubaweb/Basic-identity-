import { AndroidPolicy, AndroidDpcQrBundle } from '../types/android';

/**
 * Extracts the enterpriseToken parameter from a URL or raw string input.
 * Supports query params, URL fragments, or direct token paste.
 */
export function extractEnterpriseToken(input: string): {
  success: boolean;
  token: string | null;
  error?: string;
} {
  if (!input || typeof input !== 'string') {
    return { success: false, token: null, error: 'المدخل فارغ' };
  }

  const cleaned = input.trim();
  if (cleaned.length === 0) {
    return { success: false, token: null, error: 'يرجى إدخال الرابط أو الرمز' };
  }

  // Check if it's already a clean raw token without URL symbols
  if (!cleaned.includes('/') && !cleaned.includes('?') && !cleaned.includes('&') && !cleaned.includes('=')) {
    if (cleaned.length < 10) {
      return { success: false, token: null, error: 'الرمز قصير جداً، يرجى التأكد من نسخه بالكامل' };
    }
    return { success: true, token: cleaned };
  }

  // Try parsing as URL or looking for enterpriseToken parameter
  try {
    // Look for enterpriseToken=... in query or fragment
    const tokenRegex = /(?:[?&#]enterpriseToken=)([^&#\s"']+)/i;
    const match = cleaned.match(tokenRegex);
    if (match && match[1]) {
      const decoded = decodeURIComponent(match[1].trim());
      if (decoded.length > 5) {
        return { success: true, token: decoded };
      }
    }

    // Try standard URL parsing if it has a protocol
    const urlString = cleaned.startsWith('http://') || cleaned.startsWith('https://') 
      ? cleaned 
      : `https://${cleaned}`;
    
    const parsedUrl = new URL(urlString);
    const paramVal = parsedUrl.searchParams.get('enterpriseToken') || parsedUrl.searchParams.get('token');
    if (paramVal) {
      return { success: true, token: paramVal.trim() };
    }

    // If fragment contains enterpriseToken
    if (parsedUrl.hash) {
      const hashParams = new URLSearchParams(parsedUrl.hash.replace(/^#/, ''));
      const hashVal = hashParams.get('enterpriseToken') || hashParams.get('token');
      if (hashVal) {
        return { success: true, token: hashVal.trim() };
      }
    }
  } catch {
    // fallback regex search
  }

  // Fallback: If it starts with ya29. or resembles google auth/enterprise token
  const googleTokenMatch = cleaned.match(/(ya29\.[a-zA-Z0-9_-]+)/);
  if (googleTokenMatch && googleTokenMatch[1]) {
    return { success: true, token: googleTokenMatch[1] };
  }

  return {
    success: false,
    token: null,
    error: 'لم نتمكن من العثور على enterpriseToken في الرابط المُدخل. تأكد من نسخ رابط localhost بالكامل من شريط العنوان بعد الموافقة.',
  };
}

/**
 * Validates Google Cloud Project ID
 */
export function validateProjectId(id: string): { valid: boolean; message?: string } {
  const trimmed = id.trim();
  if (!trimmed) {
    return { valid: false, message: 'معرف المشروع مطلوب' };
  }
  if (trimmed.length < 6 || trimmed.length > 30) {
    return { valid: false, message: 'معرف مشروع Google Cloud يجب أن يكون بين 6 و 30 حرفاً' };
  }
  if (!/^[a-z][a-z0-9-]*[a-z0-9]$/.test(trimmed)) {
    return {
      valid: false,
      message: 'معرف المشروع يجب أن يبدأ بحرف صغير بالإنجليزية ويحتوي فقط على حروف صغيرة وأرقام وشُرطات (-)',
    };
  }
  return { valid: true };
}

/**
 * Generates the policy body dictionary for Android Management API
 */
export function buildPolicyBody(policy: AndroidPolicy): Record<string, unknown> {
  const body: Record<string, unknown> = {
    playStoreMode: policy.playStoreMode,
    passwordPolicies: [
      {
        passwordScope: policy.passwordScope,
      },
    ],
  };

  if (policy.cameraDisabled) {
    body.cameraDisabled = true;
  }
  if (policy.screenCaptureDisabled) {
    body.screenCaptureDisabled = true;
  }
  if (policy.installUnknownSourcesAllowed) {
    body.installUnknownSourcesAllowed = true;
  }
  if (policy.crossProfileCopyPasteAllowed) {
    body.crossProfileCopyPaste = 'COPY_PASTE_TO_WORK_PROFILE_ONLY';
  } else {
    body.crossProfileCopyPaste = 'CROSS_PROFILE_COPY_PASTE_ALLOWED';
  }
  if (policy.modifyAccountsDisabled) {
    body.modifyAccountsDisabled = true;
  }
  if (policy.factoryResetDisabled) {
    body.factoryResetDisabled = true;
  }
  if (policy.autoTimeRequired) {
    body.autoTimeRequired = true;
  }

  return body;
}

/**
 * Generates Cell 1 code for Google Colab
 */
export function generateColabStep1Code(projectId: string): string {
  const pId = projectId.trim() || 'YOUR_PROJECT_ID';
  return `from google.colab import auth
from googleapiclient.discovery import build

# 1. تسجيل الدخول بحساب جوجل الخاص بك
auth.authenticate_user()

# 2. ضع معرف المشروع (Project ID) الخاص بك هنا
PROJECT_ID = "${pId}" 

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
print("\\n2. بعد الموافقة، سيحولك المتصفح لصفحة لا تعمل؛")
print("انسخ الكود الموجود بعد كلمة enterpriseToken= من شريط العنوان في الأعلى.")`;
}

/**
 * Generates Cell 2 code for Google Colab
 */
export function generateColabStep2Code(
  projectId: string,
  enterpriseToken: string,
  policy: AndroidPolicy
): string {
  const pId = projectId.trim() || 'YOUR_PROJECT_ID';
  const token = enterpriseToken.trim() || 'ENTER_YOUR_TOKEN_HERE';
  const policyObj = buildPolicyBody(policy);
  const formattedPolicy = JSON.stringify(policyObj, null, 4);

  return `ENTERPRISING_TOKEN = "${token}"

# 1. إكمال إنشاء المؤسسة وربطها بالمشروع
enterprise = androidmanagement.enterprises().create(
    projectId="${pId}",
    signupUrlName=signup_url['name'],
    enterpriseToken=ENTERPRISING_TOKEN,
    body={}
).execute()

enterprise_name = enterprise['name']
print(f"✅ تم إنشاء المؤسسة بنجاح: {enterprise_name}")

# 2. إعداد سياسة أندرويد لملف العمل
policy_body = ${formattedPolicy}

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

print("\\n" + "=" * 50)
print("🎉 رمز المشرف الخاص بك هو:")
print(token['value'])
print("=" * 50)
print("\\n📌 ادخل هذا الرمز في شاشة إعداد ملف العمل على هاتفك لتفعيله فوراً.")`;
}

/**
 * Generates an all-in-one standalone Python script
 */
export function generateStandalonePythonScript(projectId: string, policy: AndroidPolicy): string {
  const pId = projectId.trim() || 'YOUR_PROJECT_ID';
  const policyObj = buildPolicyBody(policy);
  const formattedPolicy = JSON.stringify(policyObj, null, 4);

  return `#!/usr/bin/env python3
"""
Android Enterprise Token Generator
Script to generate Android Management API Enrollment Token for Work Profile setup.
Requirements: pip install google-api-python-client google-auth-oauthlib google-auth-httplib2
"""

import os
import sys
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build

SCOPES = ['https://www.googleapis.com/auth/androidmanagement']
PROJECT_ID = "${pId}"

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

    print(f"\\nافتح الرابط التالي في متصفحك للموافقة:")
    print(signup_url['url'])
    print("\\nبعد الموافقة، انسخ قيمة enterpriseToken من شريط العنوان:")
    
    enterprise_token = input("\\nضع رمز enterpriseToken هنا: ").strip()

    # 3. إنشاء المؤسسة
    print("\\n[2/4] جاري إنشاء وتفعيل المؤسسة...")
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
    policy_body = ${formattedPolicy}
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

    print("\\n" + "=" * 50)
    print("✅ تم توليد رمز المشرف بنجاح:")
    print(token['value'])
    print("=" * 50)

if __name__ == '__main__':
    main()
`;
}

/**
 * Generates ready-to-import Jupyter/Colab .ipynb format JSON
 */
export function generateIpynbContent(projectId: string, policy: AndroidPolicy): string {
  const cell1Code = generateColabStep1Code(projectId);
  const cell2Code = generateColabStep2Code(projectId, 'ENTER_YOUR_TOKEN_HERE', policy);

  const notebook = {
    nbformat: 4,
    nbformat_minor: 0,
    metadata: {
      colab: {
        provenance: [],
        toc_visible: true,
      },
      kernelspec: {
        name: 'python3',
        display_name: 'Python 3',
      },
      language_info: {
        name: 'python',
      },
    },
    cells: [
      {
        cell_type: 'markdown',
        metadata: {},
        source: [
          '# 🚀 مولّد رمز المشرف (Android Enterprise Enrollment Token Generator)\n',
          'لتجاوز شاشة إعداد ملف العمل وإنشاء Work Profile مجاناً عبر Android Management API.\n',
        ],
      },
      {
        cell_type: 'code',
        execution_count: null,
        metadata: {},
        outputs: [],
        source: cell1Code.split('\n').map((l, i, arr) => (i === arr.length - 1 ? l : l + '\n')),
      },
      {
        cell_type: 'markdown',
        metadata: {},
        source: [
          '### 📋 الخطوة التالية:\n',
          'انسخ `enterpriseToken` من رابط `localhost` بعد الموافقة وضعه في الخلية أدناه ثم شغلها.\n',
        ],
      },
      {
        cell_type: 'code',
        execution_count: null,
        metadata: {},
        outputs: [],
        source: cell2Code.split('\n').map((l, i, arr) => (i === arr.length - 1 ? l : l + '\n')),
      },
    ],
  };

  return JSON.stringify(notebook, null, 2);
}

/**
 * Generates official Android Enterprise Cloud DPC Provisioning QR Bundle
 */
export function generateAndroidDpcQrPayload(enrollmentToken: string): AndroidDpcQrBundle {
  const token = enrollmentToken.trim();
  return {
    'android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME':
      'com.google.android.apps.work.clouddpc/.receivers.CloudDeviceAdminReceiver',
    'android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM':
      'I5YvS0O5hXY46mb01BlRjq4oUC9W7Dp50ed857nvg6Y=',
    'android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION':
      'https://play.google.com/managed/downloadManagingApp?hl=en',
    'android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE': {
      'com.google.android.apps.work.clouddpc.EXTRA_ENROLLMENT_TOKEN': token,
    },
    'android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED': true,
  };
}
