import React, { useState } from 'react';
import { AndroidPolicy } from '../types/android';
import {
  validateProjectId,
  extractEnterpriseToken,
  generateColabStep1Code,
  generateColabStep2Code,
} from '../utils/tokenParser';
import { db, auth, handleFirestoreError, OperationType } from '../lib/firebase';
import { collection, doc, setDoc } from 'firebase/firestore';
import { User } from 'firebase/auth';
import {
  ExternalLink,
  Copy,
  Check,
  ChevronLeft,
  ChevronRight,
  ShieldAlert,
  Sparkles,
  Smartphone,
  Play,
  Save,
  QrCode,
  SlidersHorizontal,
} from 'lucide-react';

interface StepWizardProps {
  projectId: string;
  setProjectId: (id: string) => void;
  enterpriseToken: string;
  setEnterpriseToken: (token: string) => void;
  enrollmentToken: string;
  setEnrollmentToken: (token: string) => void;
  policy: AndroidPolicy;
  setPolicy: React.Dispatch<React.SetStateAction<AndroidPolicy>>;
  currentUser: User | null;
  onOpenQr: () => void;
  onOpenPolicy: () => void;
}

export const StepWizard: React.FC<StepWizardProps> = ({
  projectId,
  setProjectId,
  enterpriseToken,
  setEnterpriseToken,
  enrollmentToken,
  setEnrollmentToken,
  policy,
  setPolicy,
  currentUser,
  onOpenQr,
  onOpenPolicy,
}) => {
  const [currentStep, setCurrentStep] = useState<number>(1);
  const [copiedStep1, setCopiedStep1] = useState(false);
  const [copiedStep2, setCopiedStep2] = useState(false);
  const [copiedFinalToken, setCopiedFinalToken] = useState(false);
  const [inputUrl, setInputUrl] = useState('');
  const [extractError, setExtractError] = useState<string | null>(null);
  const [savingToken, setSavingToken] = useState(false);
  const [savedSuccess, setSavedSuccess] = useState(false);

  const projectValidation = validateProjectId(projectId || '');
  const step1Code = generateColabStep1Code(projectId);
  const step2Code = generateColabStep2Code(projectId, enterpriseToken, policy);

  const handleCopyStep1 = () => {
    navigator.clipboard.writeText(step1Code);
    setCopiedStep1(true);
    setTimeout(() => setCopiedStep1(false), 2000);
  };

  const handleCopyStep2 = () => {
    navigator.clipboard.writeText(step2Code);
    setCopiedStep2(true);
    setTimeout(() => setCopiedStep2(false), 2000);
  };

  const handleCopyFinalToken = () => {
    if (!enrollmentToken) return;
    navigator.clipboard.writeText(enrollmentToken);
    setCopiedFinalToken(true);
    setTimeout(() => setCopiedFinalToken(false), 2000);
  };

  const handleUrlChange = (val: string) => {
    setInputUrl(val);
    if (!val.trim()) {
      setExtractError(null);
      return;
    }
    const res = extractEnterpriseToken(val);
    if (res.success && res.token) {
      setEnterpriseToken(res.token);
      setExtractError(null);
    } else {
      setExtractError(res.error || 'تعذر استخراج الرمز من الرابط');
    }
  };

  const handleSaveToFirestore = async () => {
    if (!currentUser) {
      alert('يرجى تسجيل الدخول بحساب Google لحفظ الرمز في حسابك السحابي.');
      return;
    }
    if (!enrollmentToken.trim()) {
      alert('يرجى كتابة أو توليد رمز المشرف أولاً.');
      return;
    }

    setSavingToken(true);
    const id = `token_${Date.now()}`;
    const docData = {
      id,
      userId: currentUser.uid,
      projectId: projectId || 'default-project',
      enrollmentToken: enrollmentToken.trim(),
      policy,
      createdAt: new Date().toISOString(),
      notes: `تم التوليد بنجاح لملف العمل (${policy.playStoreMode})`,
    };

    try {
      await setDoc(doc(db, 'enrollments', id), docData);
      setSavedSuccess(true);
      setTimeout(() => setSavedSuccess(false), 3000);
    } catch (err) {
      console.error('Error saving enrollment to firestore:', err);
      handleFirestoreError(err, OperationType.WRITE, `enrollments/${id}`);
    } finally {
      setSavingToken(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Steps Navigation Bar */}
      <div className="bg-slate-900 border border-slate-800 rounded-2xl p-4">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
          {[
            { num: 1, title: 'الخطوة الأولى', desc: 'Google Cloud API' },
            { num: 2, title: 'الخطوة الثانية', desc: 'سكريبت Google Colab' },
            { num: 3, title: 'الخطوة الثالثة', desc: 'استخراج enterpriseToken' },
            { num: 4, title: 'الخطوة الرابعة', desc: 'رمز المشرف والتفعيل' },
          ].map((s) => {
            const isActive = currentStep === s.num;
            const isDone = currentStep > s.num;
            return (
              <button
                key={s.num}
                type="button"
                onClick={() => setCurrentStep(s.num)}
                className={`p-3 rounded-xl border text-right transition-all flex items-start gap-3 ${
                  isActive
                    ? 'bg-emerald-950/40 border-emerald-500 shadow-sm'
                    : isDone
                    ? 'bg-slate-950/60 border-slate-800 hover:border-slate-700'
                    : 'bg-slate-950/30 border-transparent hover:border-slate-800 opacity-60'
                }`}
              >
                <div
                  className={`w-6 h-6 rounded-lg font-mono font-bold text-xs flex items-center justify-center shrink-0 ${
                    isActive
                      ? 'bg-emerald-500 text-slate-950'
                      : isDone
                      ? 'bg-emerald-500/20 text-emerald-400'
                      : 'bg-slate-800 text-slate-400'
                  }`}
                >
                  {isDone ? <Check className="w-3.5 h-3.5" /> : s.num}
                </div>
                <div className="overflow-hidden">
                  <div className={`text-xs font-bold truncate ${isActive ? 'text-white' : 'text-slate-300'}`}>
                    {s.title}
                  </div>
                  <div className="text-[11px] text-slate-400 truncate">{s.desc}</div>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Step 1: Google Cloud Console */}
      {currentStep === 1 && (
        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-6">
          <div className="space-y-1">
            <div className="text-xs font-semibold text-emerald-400">الخطوة الأولى</div>
            <h2 className="text-lg font-bold text-white">
              تفعيل Android Management API في Google Cloud
            </h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              Google Cloud توفر خدمة Android Management API الرسمية لإدارة ملفات العمل والأجهزة مجاناً.
              كل ما تحتاجه هو حساب Gmail وإنشاء مشروع سحابي جديد.
            </p>
          </div>

          <div className="space-y-4">
            <div className="bg-slate-950 border border-slate-800 rounded-xl p-4 space-y-3">
              <h3 className="text-xs font-bold text-slate-200">الخطوات في Google Cloud:</h3>
              <ol className="text-xs text-slate-400 space-y-2 list-decimal list-inside leading-relaxed">
                <li>
                  افتح متصفح الإنترنت وانتقل إلى{' '}
                  <a
                    href="https://console.cloud.google.com"
                    target="_blank"
                    rel="noreferrer"
                    className="text-emerald-400 underline font-medium hover:text-emerald-300"
                  >
                    Google Cloud Console
                  </a>{' '}
                  وسجّل الدخول بحساب Gmail الشخصي.
                </li>
                <li>
                  اضغط على قائمة المشاريع في الأعلى واختر <strong className="text-slate-200">New Project</strong>، وسمِّه بأي اسم تريده (مثلاً: <code className="text-emerald-400 font-mono">android-work-profile</code>).
                </li>
                <li>
                  انسخ <strong className="text-slate-200">معرف المشروع (Project ID)</strong> الذي يظهر لك وضعه في الحقل أدناه.
                </li>
                <li>
                  ابحث في شريط البحث العلوي عن <strong className="text-slate-200">Android Management API</strong> واضغط على زر{' '}
                  <strong className="text-emerald-400">تفعيل (Enable)</strong>.
                </li>
              </ol>

              <div className="pt-2">
                <a
                  href="https://console.cloud.google.com/apis/library/androidmanagement.googleapis.com"
                  target="_blank"
                  rel="noreferrer"
                  className="inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium bg-emerald-600/20 text-emerald-300 hover:bg-emerald-600/30 border border-emerald-500/30 rounded-lg transition-colors"
                >
                  <span>الانتقال المباشر لصفحة Android Management API</span>
                  <ExternalLink className="w-3.5 h-3.5" />
                </a>
              </div>
            </div>

            {/* Project ID Input Box */}
            <div className="space-y-2">
              <label className="text-xs font-semibold text-slate-200 block">
                معرف المشروع الخاص بك (Project ID):
              </label>
              <div className="flex gap-2">
                <input
                  type="text"
                  dir="ltr"
                  value={projectId}
                  onChange={(e) => setProjectId(e.target.value.toLowerCase().replace(/\s+/g, ''))}
                  placeholder="مثال: my-android-project-12345"
                  className="flex-1 bg-slate-950 border border-slate-700 rounded-xl px-3.5 py-2 text-xs text-white font-mono placeholder:text-slate-600 focus:outline-none focus:border-emerald-500"
                />
                <button
                  type="button"
                  onClick={() => setProjectId('android-work-profile-test')}
                  className="px-3 py-2 text-xs text-slate-400 hover:text-white bg-slate-800 rounded-xl border border-slate-700 transition-colors whitespace-nowrap shrink-0"
                >
                  استخدام معرف تجريبي
                </button>
              </div>

              {projectId && !projectValidation.valid && (
                <p className="text-[11px] text-amber-400 flex items-center gap-1 mt-1">
                  <ShieldAlert className="w-3.5 h-3.5" />
                  <span>{projectValidation.message}</span>
                </p>
              )}
            </div>
          </div>

          <div className="flex justify-end pt-4 border-t border-slate-800">
            <button
              type="button"
              onClick={() => setCurrentStep(2)}
              className="px-5 py-2.5 text-xs font-semibold bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl transition-colors flex items-center gap-1.5 shadow-sm"
            >
              <span>متابعة إلى الخطوة الثانية</span>
              <ChevronLeft className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}

      {/* Step 2: Google Colab Setup */}
      {currentStep === 2 && (
        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-6">
          <div className="space-y-1">
            <div className="text-xs font-semibold text-emerald-400">الخطوة الثانية</div>
            <h2 className="text-lg font-bold text-white">تشغيل سكريبت التوليد عبر Google Colab</h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              بدون الحاجة لتثبيت بايثون أو برامج معقدة على حاسوبك، يمكنك تشغيل الكود في ثوانٍ عبر
              مفكرة Google Colab السحابية المجانية.
            </p>
          </div>

          <div className="space-y-4">
            <div className="flex flex-wrap items-center gap-2">
              <a
                href="https://colab.research.google.com/#create=true"
                target="_blank"
                rel="noreferrer"
                className="inline-flex items-center gap-1.5 px-3.5 py-2 text-xs font-semibold bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl transition-colors shadow-sm"
              >
                <span>فتح Google Colab في تبويب جديد</span>
                <ExternalLink className="w-3.5 h-3.5" />
              </a>

              <button
                type="button"
                onClick={handleCopyStep1}
                className="inline-flex items-center gap-1.5 px-3.5 py-2 text-xs font-medium bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 rounded-xl transition-colors"
              >
                {copiedStep1 ? (
                  <>
                    <Check className="w-3.5 h-3.5 text-emerald-400" />
                    <span>تم نسخ كود الخلية الأولى!</span>
                  </>
                ) : (
                  <>
                    <Copy className="w-3.5 h-3.5" />
                    <span>نسخ كود الخلية الأولى</span>
                  </>
                )}
              </button>
            </div>

            <div className="space-y-2">
              <div className="flex items-center justify-between text-xs text-slate-400">
                <span>الصق هذا الكود في أول خلية (Cell 1) في Colab ثم اضغط زر التشغيل (Run):</span>
                {projectId && (
                  <span className="text-emerald-400 font-mono">
                    معرف المشروع: {projectId}
                  </span>
                )}
              </div>

              <pre
                dir="ltr"
                className="bg-slate-950 border border-slate-800 rounded-xl p-4 text-xs text-slate-300 font-mono overflow-x-auto leading-relaxed max-h-72 select-all"
              >
                {step1Code}
              </pre>
            </div>

            <div className="bg-slate-950 border border-slate-800/80 rounded-xl p-4 space-y-2 text-xs text-slate-400 leading-relaxed">
              <div className="font-semibold text-white">ماذا سيحدث عند تشغيل هذا الكود؟</div>
              <p>
                1. سيطلب منك Colab الموافقة على تسجيل الدخول بحساب Google.
              </p>
              <p>
                2. سيطبع الكود رابط تسجيل المؤسسة (signupUrl). اضغط على ذلك الرابط واضغط موافقة.
              </p>
              <p>
                3. بعد الموافقة، سيحولك المتصفح لصفحة localhost تظهر رسالة تعذر الاتصال؛ انسخ الرابط كاملاً من شريط العنوان وانتقل للخطوة الثالثة!
              </p>
            </div>
          </div>

          <div className="flex justify-between pt-4 border-t border-slate-800">
            <button
              type="button"
              onClick={() => setCurrentStep(1)}
              className="px-4 py-2 text-xs font-medium text-slate-400 hover:text-white transition-colors flex items-center gap-1"
            >
              <ChevronRight className="w-4 h-4" />
              <span>الرجوع للخطوة 1</span>
            </button>

            <button
              type="button"
              onClick={() => setCurrentStep(3)}
              className="px-5 py-2.5 text-xs font-semibold bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl transition-colors flex items-center gap-1.5 shadow-sm"
            >
              <span>متابعة إلى الخطوة الثالثة</span>
              <ChevronLeft className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}

      {/* Step 3: Extract Enterprise Token & Run Step 2 */}
      {currentStep === 3 && (
        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-6">
          <div className="space-y-1">
            <div className="text-xs font-semibold text-emerald-400">الخطوة الثالثة</div>
            <h2 className="text-lg font-bold text-white">استخراج رمز المؤسسة (enterpriseToken) وتجهيز الكود</h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              انسخ الرابط الذي ظهر لك في شريط العنوان بعد الموافقة (يبدأ بـ https://localhost/?enterpriseToken=...) والصقه هنا ليتم استخراج الرمز وتوليد كود الخلية الثانية تلقائياً.
            </p>
          </div>

          <div className="space-y-4">
            <div className="space-y-2">
              <label className="text-xs font-semibold text-slate-200 block">
                الصق رابط localhost أو رمز enterpriseToken مباشرة:
              </label>
              <textarea
                dir="ltr"
                rows={3}
                value={inputUrl}
                onChange={(e) => handleUrlChange(e.target.value)}
                placeholder="https://localhost/?enterpriseToken=ya29.a0AfH6SM..."
                className="w-full bg-slate-950 border border-slate-700 rounded-xl p-3 text-xs text-white font-mono placeholder:text-slate-600 focus:outline-none focus:border-emerald-500"
              />
              {extractError && (
                <p className="text-[11px] text-red-400 flex items-center gap-1">
                  <ShieldAlert className="w-3.5 h-3.5 shrink-0" />
                  <span>{extractError}</span>
                </p>
              )}
            </div>

            {enterpriseToken && (
              <div className="p-3 bg-emerald-950/30 border border-emerald-800/60 rounded-xl space-y-1">
                <div className="text-xs font-semibold text-emerald-300 flex items-center gap-1">
                  <Check className="w-3.5 h-3.5 text-emerald-400" />
                  <span>تم استخراج رمز المؤسسة بنجاح:</span>
                </div>
                <div dir="ltr" className="text-xs font-mono text-emerald-200 break-all select-all">
                  {enterpriseToken}
                </div>
              </div>
            )}

            <div className="space-y-2 pt-2">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-300">
                  كود الخلية الثانية (Cell 2) المجهز بالرمز والسياسة:
                </span>
                <div className="flex items-center gap-2">
                  <button
                    type="button"
                    onClick={onOpenPolicy}
                    className="text-xs text-emerald-400 hover:text-emerald-300 flex items-center gap-1 transition-colors"
                  >
                    <SlidersHorizontal className="w-3.5 h-3.5" />
                    <span>تخصيص السياسة أولاً ({policy.playStoreMode})</span>
                  </button>
                  <button
                    type="button"
                    onClick={handleCopyStep2}
                    className="px-3 py-1.5 text-xs font-medium bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg transition-colors flex items-center gap-1 shadow-sm"
                  >
                    {copiedStep2 ? (
                      <>
                        <Check className="w-3.5 h-3.5" />
                        <span>تم نسخ الكود!</span>
                      </>
                    ) : (
                      <>
                        <Copy className="w-3.5 h-3.5" />
                        <span>نسخ كود الخلية 2</span>
                      </>
                    )}
                  </button>
                </div>
              </div>

              <pre
                dir="ltr"
                className="bg-slate-950 border border-slate-800 rounded-xl p-4 text-xs text-slate-300 font-mono overflow-x-auto leading-relaxed max-h-72 select-all"
              >
                {step2Code}
              </pre>
            </div>
          </div>

          <div className="flex justify-between pt-4 border-t border-slate-800">
            <button
              type="button"
              onClick={() => setCurrentStep(2)}
              className="px-4 py-2 text-xs font-medium text-slate-400 hover:text-white transition-colors flex items-center gap-1"
            >
              <ChevronRight className="w-4 h-4" />
              <span>الرجوع للخطوة 2</span>
            </button>

            <button
              type="button"
              onClick={() => setCurrentStep(4)}
              className="px-5 py-2.5 text-xs font-semibold bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl transition-colors flex items-center gap-1.5 shadow-sm"
            >
              <span>متابعة إلى استخراج الرمز النهائي والتفعيل</span>
              <ChevronLeft className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}

      {/* Step 4: Final Enrollment Token & Phone Setup */}
      {currentStep === 4 && (
        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-6">
          <div className="space-y-1">
            <div className="text-xs font-semibold text-emerald-400">الخطوة الرابعة</div>
            <h2 className="text-lg font-bold text-white">
              استخراج رمز المشرف النهائي (Enrollment Token) والتفعيل على الهاتف
            </h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              بعد تشغيل الخلية الثانية في Colab، يطبع لك الكود رمز المشرف الفعلي تحت عبارة
              «رمز المشرف الخاص بك هو:». ضعه هنا لنسخه أو إنشاء كود QR أو حفظه.
            </p>
          </div>

          <div className="space-y-4">
            <div className="space-y-2">
              <label className="text-xs font-semibold text-slate-200 block">
                رمز المشرف الناتج من Colab (أو أدخل الرمز الذي حصلت عليه):
              </label>
              <div className="flex gap-2">
                <input
                  type="text"
                  dir="ltr"
                  value={enrollmentToken}
                  onChange={(e) => setEnrollmentToken(e.target.value.trim())}
                  placeholder="مثال: ENROLLMENT_TOKEN_VALUE_XYZ..."
                  className="flex-1 bg-slate-950 border border-slate-700 rounded-xl px-3.5 py-2.5 text-xs text-emerald-300 font-mono placeholder:text-slate-600 focus:outline-none focus:border-emerald-500"
                />
                <button
                  type="button"
                  onClick={() =>
                    setEnrollmentToken(
                      'ENR_' + Math.random().toString(36).substring(2, 10).toUpperCase() + '_WORK_PROFILE'
                    )
                  }
                  className="px-3 py-2 text-xs text-slate-400 hover:text-white bg-slate-800 rounded-xl border border-slate-700 transition-colors whitespace-nowrap shrink-0"
                >
                  توليد رمز تجريبي
                </button>
              </div>
            </div>

            {/* Quick Action Buttons */}
            <div className="flex flex-wrap items-center gap-3 pt-2">
              <button
                type="button"
                onClick={handleCopyFinalToken}
                disabled={!enrollmentToken}
                className="px-4 py-2.5 text-xs font-bold bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 text-white rounded-xl transition-colors flex items-center gap-2 shadow-sm"
              >
                {copiedFinalToken ? (
                  <>
                    <Check className="w-4 h-4" />
                    <span>تم نسخ رمز المشرف بنجاح!</span>
                  </>
                ) : (
                  <>
                    <Copy className="w-4 h-4" />
                    <span>نسخ رمز المشرف بنقرة واحدة</span>
                  </>
                )}
              </button>

              <button
                type="button"
                onClick={onOpenQr}
                className="px-4 py-2.5 text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 rounded-xl transition-colors flex items-center gap-2"
              >
                <QrCode className="w-4 h-4 text-emerald-400" />
                <span>عرض رمز الاستجابة السريعة (QR Code)</span>
              </button>

              {currentUser && (
                <button
                  type="button"
                  onClick={handleSaveToFirestore}
                  disabled={savingToken || !enrollmentToken}
                  className="px-4 py-2.5 text-xs font-medium bg-slate-800 hover:bg-slate-700 disabled:opacity-50 text-slate-200 border border-slate-700 rounded-xl transition-colors flex items-center gap-2"
                >
                  {savedSuccess ? (
                    <>
                      <Check className="w-4 h-4 text-emerald-400" />
                      <span>تم الحفظ في حسابك!</span>
                    </>
                  ) : (
                    <>
                      <Save className="w-4 h-4 text-emerald-400" />
                      <span>حفظ الرمز في حسابي</span>
                    </>
                  )}
                </button>
              )}
            </div>

            {/* Instructions to finish phone setup */}
            <div className="bg-slate-950 border border-slate-800 rounded-xl p-5 space-y-3">
              <div className="flex items-center gap-2">
                <Smartphone className="w-4 h-4 text-emerald-400" />
                <h3 className="text-xs font-bold text-white">خطوات التفعيل على هاتفك الآن:</h3>
              </div>
              <ol className="text-xs text-slate-400 space-y-2 list-decimal list-inside leading-relaxed">
                <li>
                  ارجع إلى شاشة إعداد ملف العمل على هاتفك التي تطالبك برمز المشرف.
                </li>
                <li>
                  اضغط على زر <strong className="text-slate-200">التالي (Next)</strong>، ثم ألصق الرمز النصي الذي نسخته أعلاه.
                </li>
                <li>
                  سيبدأ هاتفك فوراً في التحقق من الرمز، وتنزيل أداة إدارة أندرويد (Google Cloud Device Policy)، وإنشاء ملف العمل بنجاح تام!
                </li>
              </ol>
            </div>
          </div>

          <div className="flex justify-between pt-4 border-t border-slate-800">
            <button
              type="button"
              onClick={() => setCurrentStep(3)}
              className="px-4 py-2 text-xs font-medium text-slate-400 hover:text-white transition-colors flex items-center gap-1"
            >
              <ChevronRight className="w-4 h-4" />
              <span>الرجوع للخطوة 3</span>
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
