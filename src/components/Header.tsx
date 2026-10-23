import React from 'react';
import { ShieldCheck, User, LogOut, ExternalLink } from 'lucide-react';
import { auth, loginWithGoogle, logoutUser } from '../lib/firebase';
import { User as FirebaseUser } from 'firebase/auth';

interface HeaderProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
  currentUser: FirebaseUser | null;
  savedTokensCount: number;
}

export const Header: React.FC<HeaderProps> = ({
  activeTab,
  setActiveTab,
  currentUser,
  savedTokensCount,
}) => {
  return (
    <header className="sticky top-0 z-40 bg-slate-900/90 backdrop-blur-md border-b border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between gap-8">
        
        {/* Zone 1: Brand Wordmark */}
        <div 
          onClick={() => setActiveTab('wizard')}
          className="flex items-center gap-2.5 cursor-pointer whitespace-nowrap shrink-0 group select-none"
        >
          <div className="w-9 h-9 rounded-lg bg-emerald-500/10 border border-emerald-500/30 flex items-center justify-center text-emerald-400 group-hover:border-emerald-500/60 transition-colors">
            <ShieldCheck className="w-5 h-5" />
          </div>
          <div className="flex flex-col">
            <span className="text-base font-bold text-white tracking-tight leading-tight">
              Android Token Master
            </span>
            <span className="text-[11px] text-emerald-400 font-medium">
              مشرف أندرويد المؤسسي
            </span>
          </div>
        </div>

        {/* Zone 2: Navigation Links (single line, clean typography) */}
        <nav className="hidden md:flex items-center gap-6 text-sm font-medium text-slate-300">
          <button
            onClick={() => setActiveTab('wizard')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'wizard'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            دليل التوليد
          </button>
          <button
            onClick={() => setActiveTab('extractor')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'extractor'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            مستخرج الرمز السريع
          </button>
          <button
            onClick={() => setActiveTab('policy')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'policy'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            تخصيص السياسة
          </button>
          <button
            onClick={() => setActiveTab('qr')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'qr'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            رمز QR للهاتف
          </button>
          <button
            onClick={() => setActiveTab('scripts')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'scripts'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            مكتبة الأكواد
          </button>
          <button
            onClick={() => setActiveTab('phone')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'phone'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            تفعيل الهاتف
          </button>
          <button
            onClick={() => setActiveTab('faq')}
            className={`transition-colors whitespace-nowrap shrink-0 pb-0.5 ${
              activeTab === 'faq'
                ? 'text-emerald-400 border-b-2 border-emerald-400 font-semibold'
                : 'hover:text-white'
            }`}
          >
            الأسئلة والحلول
          </button>
        </nav>

        {/* Zone 3: Primary Action & Auth */}
        <div className="flex items-center gap-3 shrink-0">
          <a
            href="https://colab.research.google.com"
            target="_blank"
            rel="noreferrer"
            className="hidden sm:inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium text-slate-300 bg-slate-800/80 hover:bg-slate-700/80 border border-slate-700 rounded-lg transition-colors whitespace-nowrap shrink-0"
          >
            <span>فتح Google Colab</span>
            <ExternalLink className="w-3.5 h-3.5" />
          </a>

          {currentUser ? (
            <div className="flex items-center gap-2">
              <button
                onClick={() => setActiveTab('saved')}
                className="px-3 py-1.5 text-xs font-medium text-slate-200 bg-slate-800 hover:bg-slate-700 rounded-lg border border-slate-700 transition-colors whitespace-nowrap shrink-0 flex items-center gap-1.5"
                title="رموز المشرف المحفوظة"
              >
                <span>الرموز المحفوظة</span>
                <span className="bg-emerald-500/20 text-emerald-300 text-[10px] px-1.5 py-0.2 rounded font-mono">
                  {savedTokensCount}
                </span>
              </button>
              <button
                onClick={() => logoutUser()}
                className="p-1.5 text-slate-400 hover:text-red-400 transition-colors rounded-lg hover:bg-slate-800"
                title="تسجيل الخروج"
                aria-label="تسجيل الخروج"
              >
                <LogOut className="w-4 h-4" />
              </button>
            </div>
          ) : (
            <button
              onClick={() => loginWithGoogle()}
              className="px-3.5 py-1.5 text-xs font-semibold text-white bg-emerald-600 hover:bg-emerald-500 rounded-lg shadow-sm transition-colors whitespace-nowrap shrink-0 flex items-center gap-1.5"
            >
              <User className="w-3.5 h-3.5" />
              <span>تسجيل الدخول للحفظ</span>
            </button>
          )}
        </div>
      </div>
    </header>
  );
};
