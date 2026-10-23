# Android Enterprise Token Master | مولد رمز المشرف

تطبيق أندرويد متكامل ومبني بأحدث تقنيات **Kotlin** و **Jetpack Compose** و **Material Design 3** لتسهيل إنشاء وإدارة رمز المشرف (**Enrollment Token**) وتجاوز شاشات إعداد ملف العمل في أندرويد مجاناً عبر **Google Cloud** و **Android Management API**.

---

## 📱 المميزات الرئيسية (Core Features)

1. **الدليل التفاعلي خطوة بخطوة (Step-by-Step Wizard)**:
   - **الخطوة 1**: فحص والتحقق من معرف مشروع Google Cloud (Project ID) وتفعيل واجهة البرمجة مجاناً.
   - **الخطوة 2**: تشغيل الخلية الأولى في Google Colab وتوليد رابط تسجيل المؤسسة التلقائي (`signupUrls`).
   - **الخطوة 3**: استخراج رمز `enterpriseToken` تلقائياً عبر المستخرج الذكي وتطبيق سياسة ملف العمل وتوليد كود الخلية الثانية.
   - **الخطوة 4**: عرض رمز المشرف النهائي (`Enrollment Token`) وحفظه ومشاركته.

2. **المستخرج الذكي السريع (Smart Token Extractor)**:
   - فك وقراءة روابط إعادة التوجيه `localhost` واستخراج `enterpriseToken` أو مفاتيح `ya29` ورموز المصادقة مع فحص الأخطاء.

3. **مخصص السياسات (Android Management Policy Customizer)**:
   - التحكم في وضع متجر Play (`BLACKLIST` للسماح بجميع التطبيقات بحرية أو `WHITELIST`).
   - تخصيص أمان كلمة المرور والكاميرا ولقطات الشاشة ومشاركة الحافظة وتثبيت الحزم الخارجية (Unknown Sources APK) وإعادة ضبط المصنع مع معاينة JSON حية.

4. **حزمة ومولد رمز الاستجابة السريعة (Cloud DPC QR Code)**:
   - توليد كود QR لحزمة Google Cloud DPC الرسمية للمسح المباشر من شاشة الترحيب عبر طريقة الـ 6 نقرات.
   - إمكانية التبديل بين حزمة DPC الكاملة والرمز الخام.

5. **مركز الأكواد والنصوص البرمجية (Script Center)**:
   - أكواد جاهزة لـ Google Colab.
   - سكربت بايثون مستقل مع مصادقة Google OAuth.
   - أوامر cURL مباشرة مع Google Cloud SDK.

6. **دليل تفعيل الهاتف واستكشاف الأخطاء (Phone Guide & Troubleshooting)**:
   - شرح تفصيلي لطريقة الـ 6 نقرات وطريقة كود `afw#setup`.
   - حلول سريعة لأكثر من 5 مشكلات وأخطاء شائعة (انتهاء صلاحية الرمز، واجهة البرمجة غير مفعلة، المتجر المقيد، الخ).

7. **سجل الرموز المحفوظة محلياً (Saved Enrollments)**:
   - حفظ الرموز المولدة مع التوقيت ونوع السياسة ومعرف المشروع للرجوع إليها وإعادة عرض رمز QR في أي وقت.

---

## 🛠️ البنية التقنية (Tech Stack)

- **اللغة**: Kotlin 2.1
- **واجهة المستخدم**: Jetpack Compose مع Material 3 Design
- **إدارة الحالة**: StateFlow و Android ViewModel
- **المعالجة غير المتزامنة**: Kotlin Coroutines
- **توليد QR**: مكتبة ZXing Core
- **التخزين المحلي**: Android SharedPreferences و DataStore
- **نظام البناء**: Gradle 8.8+ مع Kotlin DSL Version Catalog (`libs.versions.toml`)

---

## 🚀 كيفية التشغيل في Android Studio

1. افتح المشروع في **Android Studio (Ladybug / Meerkat أو أحدث)**.
2. دع Gradle يقوم بمزامنة التبعيات من `libs.versions.toml`.
3. اختر جهاز محاكي (Emulator) أو هاتف حقيقي بنظام Android 8.0 فما فوق.
4. اضغط **Run (`Shift + F10`)**.
