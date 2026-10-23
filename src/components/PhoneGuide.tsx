import React, { useState } from 'react';
import { Smartphone, CheckCircle2, AlertTriangle, ShieldCheck, HelpCircle, ChevronLeft } from 'lucide-react';

export const PhoneGuide: React.FC = () => {
  const [selectedBrand, setSelectedBrand] = useState<'samsung' | 'xiaomi' | 'pixel' | 'all'>('samsung');

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-start gap-4">
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
            <Smartphone className="w-6 h-6" />
          </div>
          <div className="space-y-1">
            <h2 className="text-lg font-bold text-white">
              دليل التفعيل على الهاتف وتجاوز شاشة ملف العمل
            </h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              شرح تفصيلي لكيفية إدخال رمز المشرف (Enrollment Token) الذي قمت بتوليده في هاتفك،
              لإنشاء ملف العمل (Work Profile) بسلاسة، أو إزالة أي ملف عمل معلق سابق.
            </p>
          </div>
        </div>
      </div>

      {/* Brand Tabs */}
      <div className="flex items-center gap-2 p-1 bg-slate-900 border border-slate-800 rounded-xl overflow-x-auto">
        <button
          type="button"
          onClick={() => setSelectedBrand('samsung')}
          className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all whitespace-nowrap shrink-0 ${
            selectedBrand === 'samsung'
              ? 'bg-emerald-600 text-white shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          سامسونج (Samsung One UI)
        </button>
        <button
          type="button"
          onClick={() => setSelectedBrand('xiaomi')}
          className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all whitespace-nowrap shrink-0 ${
            selectedBrand === 'xiaomi'
              ? 'bg-emerald-600 text-white shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          شاومي وريدمي وبوكو (MIUI / HyperOS)
        </button>
        <button
          type="button"
          onClick={() => setSelectedBrand('pixel')}
          className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all whitespace-nowrap shrink-0 ${
            selectedBrand === 'pixel'
              ? 'bg-emerald-600 text-white shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          جوجل بكسل وهواتف أندرويد الخام
        </button>
        <button
          type="button"
          onClick={() => setSelectedBrand('all')}
          className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all whitespace-nowrap shrink-0 ${
            selectedBrand === 'all'
              ? 'bg-emerald-600 text-white shadow-sm'
              : 'text-slate-400 hover:text-white'
          }`}
        >
          كافة أجهزة أندرويد العامة
        </button>
      </div>

      {/* Steps Content Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        
        {/* Step 1 */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
          <div className="flex items-center gap-3">
            <span className="w-7 h-7 rounded-lg bg-emerald-500/20 text-emerald-400 font-mono font-bold text-sm flex items-center justify-center">
              1
            </span>
            <h3 className="text-sm font-semibold text-white">الوصول لشاشة إدخال الرمز</h3>
          </div>
          <p className="text-xs text-slate-400 leading-relaxed">
            عندما يطلب منك الهاتف إعداد ملف عمل، أو عند استخدام ميزة إضافة حساب عمل
            (Settings &gt; Accounts &gt; Add work profile)، ستظهر لك شاشة بعنوان:
          </p>
          <div className="p-3 bg-slate-950 border border-slate-800 rounded-lg text-xs font-medium text-slate-300">
            «أدخل رمز المشرف المقدم من مسؤول تقنية المعلومات» (Enter Enrollment Token)
          </div>
          <p className="text-xs text-slate-400">
            أو في شاشات الإعداد الأولى عند النقر 6 مرات لمسح كود QR.
          </p>
        </div>

        {/* Step 2 */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
          <div className="flex items-center gap-3">
            <span className="w-7 h-7 rounded-lg bg-emerald-500/20 text-emerald-400 font-mono font-bold text-sm flex items-center justify-center">
              2
            </span>
            <h3 className="text-sm font-semibold text-white">لصق رمز المشرف</h3>
          </div>
          <p className="text-xs text-slate-400 leading-relaxed">
            اضغط على حقل الإدخال النصي، وقم بلصق الرمز الطويل الذي تم استخراجه من الكود:
          </p>
          <div className="p-2.5 bg-slate-950 border border-emerald-900/40 rounded-lg text-xs font-mono text-emerald-300 break-all select-all">
            value: "ENTERPRISE_ENROLLMENT_TOKEN..."
          </div>
          <p className="text-xs text-slate-400">
            ثم اضغط على زر <strong className="text-slate-200">التالي (Next)</strong> في أسفل الشاشة.
          </p>
        </div>

        {/* Step 3 */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
          <div className="flex items-center gap-3">
            <span className="w-7 h-7 rounded-lg bg-emerald-500/20 text-emerald-400 font-mono font-bold text-sm flex items-center justify-center">
              3
            </span>
            <h3 className="text-sm font-semibold text-white">تنزيل أداة إدارة أندرويد</h3>
          </div>
          <p className="text-xs text-slate-400 leading-relaxed">
            سيبدأ هاتفك فوراً في الاتصال بسيرفرات Google Android Enterprise، والتحقق من صلاحية
            المؤسسة التي أنشأتها عبر الـ API.
          </p>
          <p className="text-xs text-slate-400 leading-relaxed">
            سيقوم الهاتف تلقائياً بتنزيل تطبيق{' '}
            <strong className="text-emerald-400">Android Device Policy</strong> الرسمي من جوجل
            وتثبيته في ثوانٍ معدودة.
          </p>
        </div>

        {/* Step 4 */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
          <div className="flex items-center gap-3">
            <span className="w-7 h-7 rounded-lg bg-emerald-500/20 text-emerald-400 font-mono font-bold text-sm flex items-center justify-center">
              4
            </span>
            <h3 className="text-sm font-semibold text-white">اكتمال إنشاء ملف العمل بنجاح</h3>
          </div>
          <p className="text-xs text-slate-400 leading-relaxed">
            ستظهر لك رسالة «تم إعداد ملف العمل»، وستجد في درج التطبيقات تبويباً منفصلاً يحمل أيقونة
            حقيبة العمل الزرقاء الصغيرة بجوار التطبيقات، مع متجر Google Play مخصص ومستقل تماماً!
          </p>
          <div className="flex items-center gap-2 text-xs text-emerald-400 font-medium">
            <CheckCircle2 className="w-4 h-4 shrink-0" />
            <span>تم تجاوز الشاشة بنجاح وبدون أي قيود مزعجة!</span>
          </div>
        </div>
      </div>

      {/* Brand Specific Tips */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 space-y-4">
        <h3 className="text-sm font-bold text-white flex items-center gap-2">
          <ShieldCheck className="w-4 h-4 text-emerald-400" />
          <span>
            نصائح وحلول لأجهزة{' '}
            {selectedBrand === 'samsung'
              ? 'سامسونج جالكسي'
              : selectedBrand === 'xiaomi'
              ? 'شاومي وريدمي'
              : selectedBrand === 'pixel'
              ? 'جوجل بكسل'
              : 'أندرويد المختلفة'}
          </span>
        </h3>

        {selectedBrand === 'samsung' && (
          <div className="text-xs text-slate-400 space-y-2 leading-relaxed">
            <p>
              • <strong>تعارض Samsung Knox:</strong> إذا كان الهاتف يحتوي بالفعل على ملف عمل قديم،
              توجه إلى:
              <br />
              <code className="text-slate-200 bg-slate-950 px-2 py-0.5 rounded font-mono mt-1 inline-block">
                الضبط &gt; الحسابات والنسخ الاحتياطي &gt; إدارة الحسابات &gt; ملف العمل &gt; إزالة
                ملف العمل
              </code>
            </p>
            <p>
              • هواتف سامسونج تدعم الانتقال السلس بين الملف الشخصي وملف العمل بزر تشغيل/إيقاف في
              لوحة الإشعارات السريعة (Work mode).
            </p>
          </div>
        )}

        {selectedBrand === 'xiaomi' && (
          <div className="text-xs text-slate-400 space-y-2 leading-relaxed">
            <p>
              • في واجهات MIUI / HyperOS، تأكد من تفعيل خيار "تثبيت التطبيقات عبر USB أو المصادر
              الموثوقة" إذا رغبت في تثبيت APK داخل ملف العمل.
            </p>
            <p>
              • تأكد من عدم تفعيل ميزة "الواجهة الثانية (Second Space)" بالتزامن مع ملف العمل لتجنب
              استهلاك الرام المفرط.
            </p>
          </div>
        )}

        {selectedBrand === 'pixel' && (
          <div className="text-xs text-slate-400 space-y-2 leading-relaxed">
            <p>
              • أجهزة Google Pixel توفر أفضل وأسرع تجربة مدمجة لـ Android Management API.
            </p>
            <p>
              • تظهر حقيبة العمل في أسفل درج التطبيقات مع إمكانية إيقاف تشغيل ملف العمل بلمسة واحدة
              لراحة البال عند انتهاء أوقات الدوام.
            </p>
          </div>
        )}

        {selectedBrand === 'all' && (
          <div className="text-xs text-slate-400 space-y-2 leading-relaxed">
            <p>
              • أي هاتف يعمل بنظام أندرويد 7.0 فأحدث ومزود بخدمات جوجل يدعم هذه الطريقة الرسمية
              مجاناً بدون أي اشتراكات مدفوعة في خدمات خارجية.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
