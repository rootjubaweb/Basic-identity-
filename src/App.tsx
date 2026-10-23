import React, { useState, useEffect } from 'react';
import { auth, db, handleFirestoreError, OperationType } from './lib/firebase';
import { onAuthStateChanged, User as FirebaseUser } from 'firebase/auth';
import { collection, query, where, onSnapshot } from 'firebase/firestore';
import { AndroidPolicy } from './types/android';
import { Header } from './components/Header';
import { StepWizard } from './components/StepWizard';
import { TokenExtractorTool } from './components/TokenExtractorTool';
import { PolicyCustomizer } from './components/PolicyCustomizer';
import { QrCodeCard } from './components/QrCodeCard';
import { PhoneGuide } from './components/PhoneGuide';
import { ScriptCenter } from './components/ScriptCenter';
import { Troubleshooting } from './components/Troubleshooting';
import { SavedTokensModal } from './components/SavedTokensModal';
import {
  ShieldCheck,
  Smartphone,
  Terminal,
  QrCode,
  Sparkles,
  HelpCircle,
  CheckCircle,
  ExternalLink,
} from 'lucide-react';

export default function App() {
  const [currentUser, setCurrentUser] = useState<FirebaseUser | null>(null);
  const [activeTab, setActiveTab] = useState<string>('wizard');
  const [projectId, setProjectId] = useState<string>('android-work-profile');
  const [enterpriseToken, setEnterpriseToken] = useState<string>('');
  const [enrollmentToken, setEnrollmentToken] = useState<string>('');
  const [savedTokensCount, setSavedTokensCount] = useState<number>(0);

  const [policy, setPolicy] = useState<AndroidPolicy>({
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

  // Track Firebase auth state
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setCurrentUser(user);
    });
    return () => unsubscribe();
  }, []);

  // Track saved tokens count in Firestore
  useEffect(() => {
    if (!currentUser) {
      setSavedTokensCount(0);
      return;
    }
    const q = query(collection(db, 'enrollments'), where('userId', '==', currentUser.uid));
    const unsubscribe = onSnapshot(
      q,
      (snapshot) => {
        setSavedTokensCount(snapshot.size);
      },
      (error) => {
        console.error('Snapshot error for saved count:', error);
      }
    );
    return () => unsubscribe();
  }, [currentUser]);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans selection:bg-emerald-500 selection:text-white">
      {/* Top Header */}
      <Header
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        currentUser={currentUser}
        savedTokensCount={savedTokensCount}
      />

      {/* Main Workspace Container */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
        
        {/* Hero Section */}
        <div className="bg-gradient-to-b from-slate-900 to-slate-900/60 border border-slate-800 rounded-3xl p-6 sm:p-8 space-y-6">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-3 max-w-3xl">
              {/* Unboxed metadata kicker */}
              <div className="flex items-center gap-2 text-xs text-slate-400 font-medium">
                <span className="text-emerald-400 font-semibold">Google Android Management API</span>
                <span aria-hidden="true">·</span>
                <span>تجاوز إعداد ملف العمل</span>
                <span aria-hidden="true">·</span>
                <span>مجاني 100%</span>
                <span aria-hidden="true">·</span>
                <span>رموز QR و DPC</span>
              </div>

              <h1 className="text-2xl sm:text-3xl lg:text-4xl font-extrabold text-white tracking-tight leading-tight">
                مولّد رمز المشرف لملف العمل في أندرويد
              </h1>

              <p className="text-sm sm:text-base text-slate-400 leading-relaxed">
                أنشئ مؤسستك السحابية الافتراضية، وخصّص سياسات التطبيقات وقفل الشاشة، واستخرج رمز المشرف
                (Enrollment Token) الرسمي مجاناً عبر منصة Google Cloud و Android Management API لتجاوز
                شاشة إعداد ملف العمل على أي هاتف في دقائق.
              </p>
            </div>

            {/* Quick Actions in Hero */}
            <div className="flex flex-col sm:flex-row md:flex-col gap-2.5 shrink-0">
              <button
                type="button"
                onClick={() => setActiveTab('wizard')}
                className="px-5 py-2.5 text-xs font-bold text-white bg-emerald-600 hover:bg-emerald-500 rounded-xl transition-colors flex items-center justify-center gap-2 shadow-sm whitespace-nowrap"
              >
                <Smartphone className="w-4 h-4" />
                <span>بدء الدليل التفاعلي</span>
              </button>

              <button
                type="button"
                onClick={() => setActiveTab('extractor')}
                className="px-5 py-2.5 text-xs font-semibold text-slate-200 bg-slate-800 hover:bg-slate-700 border border-slate-700 rounded-xl transition-colors flex items-center justify-center gap-2 whitespace-nowrap"
              >
                <Sparkles className="w-4 h-4 text-emerald-400" />
                <span>مستخرج الرمز السريع</span>
              </button>
            </div>
          </div>

          {/* 4-Column Feature/KPI Strip */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 pt-4 border-t border-slate-800/80">
            <div className="p-3 bg-slate-950/60 rounded-xl border border-slate-800/80 space-y-1">
              <div className="text-[11px] text-slate-400">المنصة الرسمية</div>
              <div className="text-xs font-bold text-white">Google Cloud Console</div>
              <div className="text-[10px] text-slate-500 font-mono">Android Management API v1</div>
            </div>

            <div className="p-3 bg-slate-950/60 rounded-xl border border-slate-800/80 space-y-1">
              <div className="text-[11px] text-slate-400">بيئة التشغيل السحابية</div>
              <div className="text-xs font-bold text-white">Google Colab Notebook</div>
              <div className="text-[10px] text-slate-500 font-mono">بدون تثبيت أي برامج</div>
            </div>

            <div className="p-3 bg-slate-950/60 rounded-xl border border-slate-800/80 space-y-1">
              <div className="text-[11px] text-slate-400">سياسة المتجر الافتراضية</div>
              <div className="text-xs font-bold text-emerald-400 font-mono">BLACKLIST</div>
              <div className="text-[10px] text-slate-500">سماح بكافة التطبيقات بحرية</div>
            </div>

            <div className="p-3 bg-slate-950/60 rounded-xl border border-slate-800/80 space-y-1">
              <div className="text-[11px] text-slate-400">طريقة المسح السريع</div>
              <div className="text-xs font-bold text-white">DPC QR (6 نقرات)</div>
              <div className="text-[10px] text-slate-500">تنزيل أداة المشرف تلقائياً</div>
            </div>
          </div>
        </div>

        {/* Tab Viewport */}
        <div>
          {activeTab === 'wizard' && (
            <StepWizard
              projectId={projectId}
              setProjectId={setProjectId}
              enterpriseToken={enterpriseToken}
              setEnterpriseToken={setEnterpriseToken}
              enrollmentToken={enrollmentToken}
              setEnrollmentToken={setEnrollmentToken}
              policy={policy}
              setPolicy={setPolicy}
              currentUser={currentUser}
              onOpenQr={() => setActiveTab('qr')}
              onOpenPolicy={() => setActiveTab('policy')}
            />
          )}

          {activeTab === 'extractor' && (
            <TokenExtractorTool
              projectId={projectId}
              policy={policy}
              onApplyToken={(tok) => {
                setEnterpriseToken(tok);
              }}
            />
          )}

          {activeTab === 'policy' && (
            <PolicyCustomizer policy={policy} setPolicy={setPolicy} />
          )}

          {activeTab === 'qr' && (
            <QrCodeCard enrollmentToken={enrollmentToken} />
          )}

          {activeTab === 'scripts' && (
            <ScriptCenter
              projectId={projectId}
              enterpriseToken={enterpriseToken}
              policy={policy}
            />
          )}

          {activeTab === 'phone' && <PhoneGuide />}

          {activeTab === 'faq' && <Troubleshooting />}

          {activeTab === 'saved' && (
            <SavedTokensModal
              currentUser={currentUser}
              onSelectToken={(tok, proj, pol) => {
                setEnrollmentToken(tok);
                if (proj) setProjectId(proj);
                if (pol) setPolicy(pol);
                setActiveTab('qr');
              }}
              onClose={() => setActiveTab('wizard')}
            />
          )}
        </div>
      </main>

      {/* Clean Footer */}
      <footer className="mt-16 border-t border-slate-800 bg-slate-900/40 text-xs text-slate-500 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <span className="font-semibold text-slate-400">Android Enterprise Token Master</span>
            <span>·</span>
            <span>مبني لمطوري ومسؤولي أندرويد عبر Android Management API</span>
          </div>
          <div className="flex items-center gap-4">
            <a
              href="https://developers.google.com/android/management"
              target="_blank"
              rel="noreferrer"
              className="hover:text-slate-300 transition-colors"
            >
              توثيق Google الرسمي
            </a>
            <a
              href="https://colab.research.google.com"
              target="_blank"
              rel="noreferrer"
              className="hover:text-slate-300 transition-colors"
            >
              Google Colab
            </a>
            <a
              href="https://console.cloud.google.com"
              target="_blank"
              rel="noreferrer"
              className="hover:text-slate-300 transition-colors"
            >
              Google Cloud Console
            </a>
          </div>
        </div>
      </footer>
    </div>
  );
}
