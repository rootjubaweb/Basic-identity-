import React, { useState } from 'react';
import { AndroidPolicy } from '../types/android';
import { buildPolicyBody } from '../utils/tokenParser';
import { Sliders, Copy, Check, ShieldAlert, Smartphone, Lock, Eye, Download } from 'lucide-react';

interface PolicyCustomizerProps {
  policy: AndroidPolicy;
  setPolicy: React.Dispatch<React.SetStateAction<AndroidPolicy>>;
}

export const PolicyCustomizer: React.FC<PolicyCustomizerProps> = ({ policy, setPolicy }) => {
  const [copiedJson, setCopiedJson] = useState(false);

  const policyJson = JSON.stringify(buildPolicyBody(policy), null, 2);

  const handleCopyJson = () => {
    navigator.clipboard.writeText(policyJson);
    setCopiedJson(true);
    setTimeout(() => setCopiedJson(false), 2000);
  };

  const handleResetDefaults = () => {
    setPolicy({
      playStoreMode: 'BLACKLIST',
      passwordScope: 'SCOPE_PROFILE',
      cameraDisabled: false,
      screenCaptureDisabled: false,
      crossProfileCopyPasteAllowed: true,
      installUnknownSourcesAllowed: true,
      modifyAccountsDisabled: false,
      factoryResetDisabled: false,
      autoTimeRequired: false,
    });
  };

  return (
    <div className="space-y-6">
      {/* Intro Header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-start justify-between gap-4">
          <div className="flex items-start gap-4">
            <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
              <Sliders className="w-6 h-6" />
            </div>
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-white">
                تخصيص سياسة أندرويد لملف العمل (Android Policy Builder)
              </h2>
              <p className="text-sm text-slate-400 leading-relaxed">
                تتيح لك Google Android Management API ضبط القيود والصلاحيات على ملف العمل
                (Work Profile). الإعدادات الافتراضية هنا مهيأة لتعطيل القيود غير المرغوبة والسماح
                بكل التطبيقات ومشاركة الحافظة.
              </p>
            </div>
          </div>
          <button
            type="button"
            onClick={handleResetDefaults}
            className="text-xs text-slate-400 hover:text-white px-3 py-1.5 rounded-lg border border-slate-800 bg-slate-950 transition-colors whitespace-nowrap shrink-0"
          >
            استعادة الافتراضي
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Controls Column */}
        <div className="lg:col-span-7 space-y-4">
          
          {/* Card 1: Google Play Store Mode */}
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
            <div className="flex items-center gap-2">
              <Smartphone className="w-4 h-4 text-emerald-400" />
              <h3 className="text-sm font-semibold text-white">وضع متجر Google Play</h3>
            </div>
            <p className="text-xs text-slate-400">
              يحدد كيف يتعامل ملف العمل مع التطبيقات في متجر بلاي:
            </p>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-1">
              <button
                type="button"
                onClick={() => setPolicy((p) => ({ ...p, playStoreMode: 'BLACKLIST' }))}
                className={`p-3 rounded-lg border text-right transition-all ${
                  policy.playStoreMode === 'BLACKLIST'
                    ? 'bg-emerald-950/40 border-emerald-500 text-white'
                    : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'
                }`}
              >
                <div className="text-xs font-bold text-emerald-300">القائمة السوداء (BLACKLIST)</div>
                <div className="text-[11px] text-slate-400 mt-1">
                  السماح بكافة التطبيقات كالمعتاد (الخيار الموصى به لتجاوز إعداد العمل بحرية)
                </div>
              </button>

              <button
                type="button"
                onClick={() => setPolicy((p) => ({ ...p, playStoreMode: 'WHITELIST' }))}
                className={`p-3 rounded-lg border text-right transition-all ${
                  policy.playStoreMode === 'WHITELIST'
                    ? 'bg-emerald-950/40 border-emerald-500 text-white'
                    : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'
                }`}
              >
                <div className="text-xs font-bold text-emerald-300">القائمة البيضاء (WHITELIST)</div>
                <div className="text-[11px] text-slate-400 mt-1">
                  حظر كل التطبيقات عدا التطبيقات المعتمدة صراحة من المشرف
                </div>
              </button>
            </div>
          </div>

          {/* Card 2: Password Scope */}
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
            <div className="flex items-center gap-2">
              <Lock className="w-4 h-4 text-emerald-400" />
              <h3 className="text-sm font-semibold text-white">نطاق كلمة مرور القفل (Password Scope)</h3>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-1">
              <button
                type="button"
                onClick={() => setPolicy((p) => ({ ...p, passwordScope: 'SCOPE_PROFILE' }))}
                className={`p-3 rounded-lg border text-right transition-all ${
                  policy.passwordScope === 'SCOPE_PROFILE'
                    ? 'bg-emerald-950/40 border-emerald-500 text-white'
                    : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'
                }`}
              >
                <div className="text-xs font-bold text-emerald-300">ملف العمل فقط (SCOPE_PROFILE)</div>
                <div className="text-[11px] text-slate-400 mt-1">
                  تطبيق سياسة كلمة المرور على ملف العمل فقط، بدون المساس بقفل الهاتف الأساسي
                </div>
              </button>

              <button
                type="button"
                onClick={() => setPolicy((p) => ({ ...p, passwordScope: 'SCOPE_DEVICE' }))}
                className={`p-3 rounded-lg border text-right transition-all ${
                  policy.passwordScope === 'SCOPE_DEVICE'
                    ? 'bg-emerald-950/40 border-emerald-500 text-white'
                    : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'
                }`}
              >
                <div className="text-xs font-bold text-emerald-300">كامل الجهاز (SCOPE_DEVICE)</div>
                <div className="text-[11px] text-slate-400 mt-1">
                  إلزام الهاتف بأكمله بكلمة مرور مشددة (مناسب لمشرف الجهاز الكامل)
                </div>
              </button>
            </div>
          </div>

          {/* Card 3: Security & Usability Toggles */}
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-4">
            <div className="flex items-center gap-2">
              <Eye className="w-4 h-4 text-emerald-400" />
              <h3 className="text-sm font-semibold text-white">صلاحيات ومرونة الاستخدام</h3>
            </div>

            <div className="space-y-3 divide-y divide-slate-800/80">
              {/* Toggle 1: Cross-profile copy-paste */}
              <div className="flex items-center justify-between pt-2">
                <div>
                  <div className="text-xs font-semibold text-slate-200">
                    مشاركة النصوص والحافظة (Cross-Profile Copy/Paste)
                  </div>
                  <div className="text-[11px] text-slate-400">
                    السماح بنسخ النصوص والروابط بحرية بين الملف الشخصي وملف العمل
                  </div>
                </div>
                <button
                  type="button"
                  onClick={() =>
                    setPolicy((p) => ({
                      ...p,
                      crossProfileCopyPasteAllowed: !p.crossProfileCopyPasteAllowed,
                    }))
                  }
                  className={`w-11 h-6 rounded-full transition-colors relative flex items-center p-0.5 ${
                    policy.crossProfileCopyPasteAllowed ? 'bg-emerald-500' : 'bg-slate-700'
                  }`}
                >
                  <span
                    className={`w-5 h-5 bg-white rounded-full transition-transform ${
                      policy.crossProfileCopyPasteAllowed ? '-translate-x-5' : 'translate-x-0'
                    }`}
                  />
                </button>
              </div>

              {/* Toggle 2: Unknown sources APK */}
              <div className="flex items-center justify-between pt-3">
                <div>
                  <div className="text-xs font-semibold text-slate-200">
                    تثبيت التطبيقات من مصادر خارجية (APK)
                  </div>
                  <div className="text-[11px] text-slate-400">
                    السماح بتثبيت ملفات APK يدوياً داخل ملف العمل من خارج متجر Play
                  </div>
                </div>
                <button
                  type="button"
                  onClick={() =>
                    setPolicy((p) => ({
                      ...p,
                      installUnknownSourcesAllowed: !p.installUnknownSourcesAllowed,
                    }))
                  }
                  className={`w-11 h-6 rounded-full transition-colors relative flex items-center p-0.5 ${
                    policy.installUnknownSourcesAllowed ? 'bg-emerald-500' : 'bg-slate-700'
                  }`}
                >
                  <span
                    className={`w-5 h-5 bg-white rounded-full transition-transform ${
                      policy.installUnknownSourcesAllowed ? '-translate-x-5' : 'translate-x-0'
                    }`}
                  />
                </button>
              </div>

              {/* Toggle 3: Screen capture */}
              <div className="flex items-center justify-between pt-3">
                <div>
                  <div className="text-xs font-semibold text-slate-200">
                    السماح بلقطات الشاشة (Screen Capture)
                  </div>
                  <div className="text-[11px] text-slate-400">
                    تمكين تصوير الشاشة وتسجيل الفيديو داخل تطبيقات ملف العمل
                  </div>
                </div>
                <button
                  type="button"
                  onClick={() =>
                    setPolicy((p) => ({
                      ...p,
                      screenCaptureDisabled: !p.screenCaptureDisabled,
                    }))
                  }
                  className={`w-11 h-6 rounded-full transition-colors relative flex items-center p-0.5 ${
                    !policy.screenCaptureDisabled ? 'bg-emerald-500' : 'bg-slate-700'
                  }`}
                >
                  <span
                    className={`w-5 h-5 bg-white rounded-full transition-transform ${
                      !policy.screenCaptureDisabled ? '-translate-x-5' : 'translate-x-0'
                    }`}
                  />
                </button>
              </div>

              {/* Toggle 4: Camera */}
              <div className="flex items-center justify-between pt-3">
                <div>
                  <div className="text-xs font-semibold text-slate-200">
                    إتاحة الكاميرا في ملف العمل
                  </div>
                  <div className="text-[11px] text-slate-400">
                    السماح لتطبيقات العمل باستخدام كاميرا الهاتف لالتقاط الصور ومسح المستندات
                  </div>
                </div>
                <button
                  type="button"
                  onClick={() =>
                    setPolicy((p) => ({
                      ...p,
                      cameraDisabled: !p.cameraDisabled,
                    }))
                  }
                  className={`w-11 h-6 rounded-full transition-colors relative flex items-center p-0.5 ${
                    !policy.cameraDisabled ? 'bg-emerald-500' : 'bg-slate-700'
                  }`}
                >
                  <span
                    className={`w-5 h-5 bg-white rounded-full transition-transform ${
                      !policy.cameraDisabled ? '-translate-x-5' : 'translate-x-0'
                    }`}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Live Policy JSON Preview Column */}
        <div className="lg:col-span-5 space-y-4">
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3 sticky top-20">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-300">
                معاينة Policy JSON الفعلي:
              </span>
              <button
                type="button"
                onClick={handleCopyJson}
                className="px-2.5 py-1 text-xs font-medium bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-md transition-colors flex items-center gap-1.5"
              >
                {copiedJson ? (
                  <>
                    <Check className="w-3.5 h-3.5 text-emerald-400" />
                    <span>تم النسخ!</span>
                  </>
                ) : (
                  <>
                    <Copy className="w-3.5 h-3.5" />
                    <span>نسخ JSON</span>
                  </>
                )}
              </button>
            </div>

            <pre
              dir="ltr"
              className="bg-slate-950 border border-slate-800 rounded-lg p-3.5 text-xs text-emerald-300 font-mono overflow-x-auto max-h-96 leading-relaxed select-all"
            >
              {policyJson}
            </pre>

            <div className="p-3 bg-slate-950/80 border border-slate-800/80 rounded-lg text-[11px] text-slate-400 space-y-1">
              <div className="font-semibold text-slate-300">أين يتم تطبيق هذا الـ JSON؟</div>
              <p>
                يتم تمريره مباشرة داخل دالة{' '}
                <code className="text-emerald-400 font-mono">policies().patch()</code> في كود الخلية
                الثانية في Google Colab لتحديث السياسة المسماة <code className="text-emerald-400 font-mono">default</code>.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
