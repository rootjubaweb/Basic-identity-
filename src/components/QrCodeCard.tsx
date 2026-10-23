import React, { useEffect, useRef, useState } from 'react';
import QRCode from 'qrcode';
import { generateAndroidDpcQrPayload } from '../utils/tokenParser';
import { QrCode, Download, Copy, Check, Smartphone, Sparkles, Info } from 'lucide-react';

interface QrCodeCardProps {
  enrollmentToken: string;
}

export const QrCodeCard: React.FC<QrCodeCardProps> = ({ enrollmentToken }) => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [qrMode, setQrMode] = useState<'dpc' | 'raw'>('dpc');
  const [copiedToken, setCopiedToken] = useState(false);
  const [copiedPayload, setCopiedPayload] = useState(false);

  const tokenToUse = enrollmentToken.trim() || 'SAMPLE_ENROLLMENT_TOKEN_EXAMPLE_12345';
  const isDemo = !enrollmentToken.trim();

  const dpcPayload = JSON.stringify(generateAndroidDpcQrPayload(tokenToUse), null, 2);
  const currentQrString = qrMode === 'dpc' ? JSON.stringify(generateAndroidDpcQrPayload(tokenToUse)) : tokenToUse;

  useEffect(() => {
    if (canvasRef.current) {
      QRCode.toCanvas(
        canvasRef.current,
        currentQrString,
        {
          width: 260,
          margin: 2,
          color: {
            dark: '#0f172a',
            light: '#ffffff',
          },
          errorCorrectionLevel: 'M',
        },
        (error) => {
          if (error) console.error('QR Code Generation Error:', error);
        }
      );
    }
  }, [currentQrString]);

  const handleDownloadPng = () => {
    if (!canvasRef.current) return;
    const url = canvasRef.current.toDataURL('image/png');
    const a = document.createElement('a');
    a.href = url;
    a.download = `android-enterprise-qr-${Date.now()}.png`;
    a.click();
  };

  const handleCopyToken = () => {
    navigator.clipboard.writeText(tokenToUse);
    setCopiedToken(true);
    setTimeout(() => setCopiedToken(false), 2000);
  };

  const handleCopyPayload = () => {
    navigator.clipboard.writeText(dpcPayload);
    setCopiedPayload(true);
    setTimeout(() => setCopiedPayload(false), 2000);
  };

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-start gap-4">
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
            <QrCode className="w-6 h-6" />
          </div>
          <div className="space-y-1">
            <h2 className="text-lg font-bold text-white">
              مولد رمز الاستجابة السريعة (Android DPC QR Provisioning)
            </h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              يمكنك إدخال رمز المشرف يدوياً في شاشة إعداد ملف العمل، أو مسح رمز QR مباشرة
              باستخدام ميزة الـ 6 نقرات (6-Tap Setup) في هواتف أندرويد الحديثة لتثبيت تطبيق الإدارة
              تلقائياً دون كتابة الرمز الطويل يدوياً.
            </p>
          </div>
        </div>
      </div>

      {isDemo && (
        <div className="p-3 bg-amber-950/30 border border-amber-800/50 rounded-xl text-xs text-amber-300 flex items-center gap-2">
          <Info className="w-4 h-4 shrink-0 text-amber-400" />
          <span>
            هذا الرمز المعروض حالياً هو رمز تجريبي توضيحي. بمجرد توليد رمزك الخاص من الخطوة
            السابقة أو إدخاله، سيتحدث الـ QR Code فوراً برمزك الحقيقي.
          </span>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        {/* QR Display Card */}
        <div className="lg:col-span-5 bg-slate-900 border border-slate-800 rounded-xl p-6 flex flex-col items-center text-center space-y-4">
          
          {/* Mode Switcher */}
          <div className="w-full flex items-center p-1 bg-slate-950 border border-slate-800 rounded-lg">
            <button
              type="button"
              onClick={() => setQrMode('dpc')}
              className={`flex-1 py-1.5 text-xs font-medium rounded-md transition-all ${
                qrMode === 'dpc'
                  ? 'bg-emerald-600 text-white shadow-sm'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              حزمة DPC الرسمية (لـ 6 نقرات)
            </button>
            <button
              type="button"
              onClick={() => setQrMode('raw')}
              className={`flex-1 py-1.5 text-xs font-medium rounded-md transition-all ${
                qrMode === 'raw'
                  ? 'bg-emerald-600 text-white shadow-sm'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              الرمز الصافي (Raw Token)
            </button>
          </div>

          {/* White Canvas Container */}
          <div className="p-3 bg-white rounded-2xl shadow-xl border-4 border-slate-800">
            <canvas ref={canvasRef} className="block rounded-lg" />
          </div>

          <div className="w-full flex gap-2">
            <button
              type="button"
              onClick={handleDownloadPng}
              className="flex-1 py-2 text-xs font-medium bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg transition-colors flex items-center justify-center gap-1.5 shadow-sm"
            >
              <Download className="w-3.5 h-3.5" />
              <span>تنزيل صورة الـ QR</span>
            </button>
            <button
              type="button"
              onClick={handleCopyToken}
              className="py-2 px-3 text-xs font-medium bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-lg transition-colors flex items-center justify-center gap-1.5 border border-slate-700"
            >
              {copiedToken ? (
                <>
                  <Check className="w-3.5 h-3.5 text-emerald-400" />
                  <span>تم النسخ</span>
                </>
              ) : (
                <>
                  <Copy className="w-3.5 h-3.5" />
                  <span>نسخ الرمز</span>
                </>
              )}
            </button>
          </div>

          <div className="text-[11px] text-slate-400">
            متوافق مع أندرويد 7.0 وما فوق (بما فيها أندرويد 12 و 13 و 14 و 15).
          </div>
        </div>

        {/* Instructions & Token Details */}
        <div className="lg:col-span-7 space-y-4">
          
          {/* Active Token Box */}
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-300">
                رمز المشرف (Enrollment Token):
              </span>
              <button
                type="button"
                onClick={handleCopyToken}
                className="text-xs text-emerald-400 hover:text-emerald-300 flex items-center gap-1 transition-colors"
              >
                {copiedToken ? <Check className="w-3.5 h-3.5" /> : <Copy className="w-3.5 h-3.5" />}
                <span>{copiedToken ? 'تم النسخ!' : 'نسخ الرمز النصي'}</span>
              </button>
            </div>

            <div
              dir="ltr"
              className="bg-slate-950 border border-slate-800 rounded-lg p-3 text-xs text-emerald-300 font-mono break-all select-all leading-relaxed"
            >
              {tokenToUse}
            </div>
          </div>

          {/* How to use with 6-tap method */}
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
            <div className="flex items-center gap-2">
              <Smartphone className="w-4 h-4 text-emerald-400" />
              <h3 className="text-sm font-semibold text-white">
                طريقة التفعيل السحرية: 6 نقرات (6-Tap Provisioning)
              </h3>
            </div>

            <ol className="text-xs text-slate-400 space-y-2.5 leading-relaxed list-decimal list-inside">
              <li>
                عند إعادة تشغيل الهاتف بعد ضبط المصنع أو في شاشة الترحيب الأولى (Welcome /
                ابدأ).
              </li>
              <li>
                اضغط <strong className="text-slate-200">6 مرات سريعة متتالية</strong> بإصبعك على أي
                مساحة فارغة في شاشة الترحيب.
              </li>
              <li>
                ستفتح لك شاشة مخصصة وتطلب منك الاتصال بشبكة Wi-Fi ثم تنزيل ماسح رموز الاستجابة
                السريعة (QR Reader).
              </li>
              <li>
                وجّه كاميرا الهاتف نحو رمز الـ QR أعلاه (وضع حزمة DPC الرسمية)؛ وسيبدأ الهاتف فوراً
                في تنزيل أداة <strong className="text-slate-200">Google Cloud DPC</strong> وتطبيق
                السياسة وإنشاء الملف بنجاح دون أي كتابة يدوية!
              </li>
            </ol>
          </div>

          {/* DPC JSON Payload Inspection */}
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-300">
                حزمة DPC JSON المضمنة في الـ QR:
              </span>
              <button
                type="button"
                onClick={handleCopyPayload}
                className="text-xs text-slate-400 hover:text-white flex items-center gap-1 transition-colors"
              >
                {copiedPayload ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                <span>{copiedPayload ? 'تم النسخ!' : 'نسخ Payload'}</span>
              </button>
            </div>

            <pre
              dir="ltr"
              className="bg-slate-950 border border-slate-800 rounded-lg p-3 text-xs text-slate-400 font-mono overflow-x-auto max-h-48 leading-relaxed select-all"
            >
              {dpcPayload}
            </pre>
          </div>
        </div>
      </div>
    </div>
  );
};
