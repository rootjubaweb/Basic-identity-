import React, { useState } from 'react';
import { extractEnterpriseToken, generateColabStep2Code } from '../utils/tokenParser';
import { AndroidPolicy } from '../types/android';
import { Copy, Check, Sparkles, AlertCircle, ArrowLeft, Terminal } from 'lucide-react';

interface TokenExtractorToolProps {
  projectId: string;
  policy: AndroidPolicy;
  onApplyToken: (token: string) => void;
}

export const TokenExtractorTool: React.FC<TokenExtractorToolProps> = ({
  projectId,
  policy,
  onApplyToken,
}) => {
  const [inputUrl, setInputUrl] = useState('');
  const [extractedToken, setExtractedToken] = useState<string | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [copiedToken, setCopiedToken] = useState(false);
  const [copiedCode, setCopiedCode] = useState(false);

  const handleExtract = (text: string) => {
    setInputUrl(text);
    if (!text.trim()) {
      setExtractedToken(null);
      setErrorMsg(null);
      return;
    }

    const res = extractEnterpriseToken(text);
    if (res.success && res.token) {
      setExtractedToken(res.token);
      setErrorMsg(null);
      onApplyToken(res.token);
    } else {
      setExtractedToken(null);
      setErrorMsg(res.error || 'تعذر استخراج رمز المؤسسة');
    }
  };

  const handleCopyToken = () => {
    if (!extractedToken) return;
    navigator.clipboard.writeText(extractedToken);
    setCopiedToken(true);
    setTimeout(() => setCopiedToken(false), 2000);
  };

  const cell2Code = extractedToken
    ? generateColabStep2Code(projectId || 'YOUR_PROJECT_ID', extractedToken, policy)
    : '';

  const handleCopyCode = () => {
    if (!cell2Code) return;
    navigator.clipboard.writeText(cell2Code);
    setCopiedCode(true);
    setTimeout(() => setCopiedCode(false), 2000);
  };

  const sampleDemoUrl =
    'https://localhost/?enterpriseToken=ya29.a0AfH6SMB_SAMPLE_ENTERPRISE_TOKEN_9998877';

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-start gap-4">
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
            <Sparkles className="w-6 h-6" />
          </div>
          <div className="space-y-1">
            <h2 className="text-lg font-bold text-white">
              مستخرج رمز المؤسسة الذكي (Enterprise Token Extractor)
            </h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              بعد الموافقة على إنشاء المؤسسة، يحوّلك متصفح جوجل إلى صفحة خطأ بعنوان{' '}
              <span className="font-mono text-emerald-400">https://localhost/?enterpriseToken=...</span>.
              فقط الصق الرابط الكامل هنا، وستقوم الأداة بفصل الرمز الصافي وتجهيز كود استخراج رمز المشرف
              تلقائياً!
            </p>
          </div>
        </div>
      </div>

      {/* Input Form */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="flex items-center justify-between">
          <label className="text-sm font-semibold text-slate-200">
            ألصق الرابط من شريط عنوان المتصفح:
          </label>
          <button
            type="button"
            onClick={() => handleExtract(sampleDemoUrl)}
            className="text-xs text-emerald-400 hover:text-emerald-300 transition-colors"
          >
            تجربة رابط افتراضي للتوضيح
          </button>
        </div>

        <div className="relative">
          <textarea
            value={inputUrl}
            onChange={(e) => handleExtract(e.target.value)}
            placeholder="مثال: https://localhost/?enterpriseToken=ya29.a0AfH6SM... أو الصق الرمز مباشرة"
            rows={3}
            dir="ltr"
            className="w-full bg-slate-950 border border-slate-700 rounded-xl p-3.5 text-xs text-slate-200 font-mono placeholder:text-slate-600 focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-colors"
          />
        </div>

        {errorMsg && (
          <div className="p-3 bg-red-950/40 border border-red-800/60 rounded-lg flex items-center gap-2 text-xs text-red-300">
            <AlertCircle className="w-4 h-4 shrink-0 text-red-400" />
            <span>{errorMsg}</span>
          </div>
        )}

        {extractedToken && (
          <div className="space-y-4 pt-2">
            <div className="p-4 bg-emerald-950/30 border border-emerald-800/60 rounded-xl space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-emerald-300 flex items-center gap-1.5">
                  <Check className="w-4 h-4 text-emerald-400" />
                  تم استخراج رمز المؤسسة بنجاح!
                </span>
                <button
                  type="button"
                  onClick={handleCopyToken}
                  className="px-2.5 py-1 text-xs font-medium bg-emerald-500/20 hover:bg-emerald-500/30 text-emerald-300 rounded-md transition-colors flex items-center gap-1"
                >
                  {copiedToken ? (
                    <>
                      <Check className="w-3.5 h-3.5" />
                      <span>تم النسخ!</span>
                    </>
                  ) : (
                    <>
                      <Copy className="w-3.5 h-3.5" />
                      <span>نسخ الرمز</span>
                    </>
                  )}
                </button>
              </div>

              <div
                dir="ltr"
                className="bg-slate-950 border border-slate-800 rounded-lg p-2.5 text-xs text-emerald-200 font-mono break-all select-all"
              >
                {extractedToken}
              </div>
            </div>

            {/* Generated Colab Cell 2 Ready Code */}
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <Terminal className="w-4 h-4 text-slate-400" />
                  <span className="text-xs font-semibold text-slate-300">
                    كود الخلية الثانية المجهز لـ Google Colab:
                  </span>
                </div>
                <button
                  type="button"
                  onClick={handleCopyCode}
                  className="px-3 py-1.5 text-xs font-medium bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg transition-colors flex items-center gap-1.5 shadow-sm"
                >
                  {copiedCode ? (
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

              <pre
                dir="ltr"
                className="bg-slate-950 border border-slate-800 rounded-xl p-4 text-xs text-slate-300 font-mono overflow-x-auto max-h-72 leading-relaxed"
              >
                {cell2Code}
              </pre>
            </div>
          </div>
        )}
      </div>

      {/* Helpful Instructions */}
      <div className="bg-slate-900/60 border border-slate-800 rounded-xl p-5">
        <h3 className="text-sm font-semibold text-slate-200 mb-2">كيف تعمل هذه الخطوة؟</h3>
        <ul className="text-xs text-slate-400 space-y-1.5 leading-relaxed">
          <li className="flex items-start gap-2">
            <span className="text-emerald-400 font-bold">1.</span>
            <span>
              عند تشغيل الكود الأول في Google Colab، يطبع لك رابطاً طويلاً على خدمة{' '}
              <strong className="text-slate-300">enterprise.google.com</strong>.
            </span>
          </li>
          <li className="flex items-start gap-2">
            <span className="text-emerald-400 font-bold">2.</span>
            <span>
              تفتح ذلك الرابط وتضغط موافقة وربط، وبمجرد الانتهاء يحوّلك المتصفح إلى رابط{' '}
              <code className="text-emerald-400 font-mono bg-slate-950 px-1 py-0.5 rounded">
                https://localhost/?enterpriseToken=...
              </code>
            </span>
          </li>
          <li className="flex items-start gap-2">
            <span className="text-emerald-400 font-bold">3.</span>
            <span>
              صفحة <code className="text-slate-300 font-mono">localhost</code> ستظهر رسالة "تعذر
              الاتصال بالخادم" - وهذا طبيعي جداً لأن حاسوبك لا يشغل خادماً محلياً؛ ما تحتاجه فقط هو
              نسخ الرابط من شريط العنوان في الأعلى ولصقه هنا!
            </span>
          </li>
        </ul>
      </div>
    </div>
  );
};
