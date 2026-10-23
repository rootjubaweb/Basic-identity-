import React, { useState } from 'react';
import { HelpCircle, ChevronDown, ChevronUp, AlertCircle, CheckCircle2, ShieldAlert } from 'lucide-react';

interface FaqItem {
  question: string;
  answer: string | React.ReactNode;
}

export const Troubleshooting: React.FC = () => {
  const [openIndex, setOpenIndex] = useState<number | null>(0);

  const faqs: FaqItem[] = [
    {
      question: 'لماذا يعطيني المتصفح صفحة بيضاء أو خطأ "لا يمكن الوصول إلى الموقع الإلكتروني (localhost)" بعد الموافقة؟',
      answer: (
        <div className="space-y-2">
          <p>
            <strong className="text-emerald-400">هذا طبيعي 100% وهو ليس خطأ!</strong>
          </p>
          <p>
            الرابط المسمى <code className="text-slate-200 bg-slate-950 px-1 py-0.5 rounded font-mono">callbackUrl="https://localhost"</code> هو مجرد عنوان استرجاع تطلبه جوجل لترسل إليه رمز الموافقة في شريط العنوان (URL Parameters).
          </p>
          <p>
            حاسوبك لا يشغل خادم ويب محلي يستقبل هذا الرابط، والمطلوب منك فقط هو النظر إلى شريط العنوان في أعلى المتصفح، ونسخ الرابط بالكامل الذي يبدأ بـ{' '}
            <code className="text-emerald-300 font-mono">https://localhost/?enterpriseToken=...</code> ولصقه في أداتنا أو في الخلية الثانية.
          </p>
        </div>
      ),
    },
    {
      question: 'رسالة خطأ في Colab: "Android Management API has not been used in project ... or it is disabled"',
      answer: (
        <div className="space-y-2">
          <p>هذا يعني أنك أنشأت مشروعاً في Google Cloud ولكنك نسيت الضغط على زر "تفعيل (Enable)" للـ API.</p>
          <p>
            <strong>الحل:</strong> افتح{' '}
            <a
              href="https://console.cloud.google.com/apis/library/androidmanagement.googleapis.com"
              target="_blank"
              rel="noreferrer"
              className="text-emerald-400 underline hover:text-emerald-300"
            >
              صفحة Android Management API في Google Cloud Console
            </a>
            ، وتأكد من اختيار مشروعك في القائمة المنسدلة العلوية، ثم اضغط على زر الزر الأزرق الكبير{' '}
            <strong className="text-white">تفعيل (Enable)</strong>، ثم أعد تشغيل الخلية في Colab.
          </p>
        </div>
      ),
    },
    {
      question: 'خطأ: "The enterprise token is invalid or has expired"',
      answer: (
        <div className="space-y-2">
          <p>
            رمز <code className="text-emerald-400 font-mono">enterpriseToken</code> مؤقت ومحدد بوقت زمني قصير جداً للاستخدام لمرة واحدة (One-Time Token).
          </p>
          <p>
            إذا تأخرت في تشغيل الخلية الثانية أو نسخت الرمز ناقصاً، سيعطيك هذا الخطأ.
            <br />
            <strong>الحل:</strong> ببساطة أعد تشغيل الخلية الأولى (Cell 1)، واضغط على الرابط الجديد الذي سيظهر لك ووافق عليه وانسخ الرمز الجديد فوراً إلى الخلية الثانية.
          </p>
        </div>
      ),
    },
    {
      question: 'هل هذه الطريقة مجانية بالكامل أم تتطلب إضافة بطاقة بنكية (Credit Card)؟',
      answer: (
        <p>
          <strong>مجانية 100%!</strong> تمنح شركة Google كل حساب مطور إمكانية استخدام Android Management API وإدارة عدد من الأجهزة مجاناً بدون الحاجة لربط بطاقة ائتمانية أو تفعيل فوترة (Billing Account) لإنشاء المؤسسات التجريبية وتوليد رموز المشرف.
        </p>
      ),
    },
    {
      question: 'الهاتف يقول: "يوجد بالفعل ملف عمل على هذا الجهاز" أو تعارض Knox في هواتف سامسونج',
      answer: (
        <div className="space-y-2">
          <p>نظام أندرويد يسمح بملف عمل واحد فقط (One Work Profile) لكل هاتف.</p>
          <p>
            إذا كان لديك ملف عمل قديم معلق أو حساب عمل سابق، يجب حذفه أولاً عبر:
            <br />
            <span className="text-slate-200 font-medium">الضبط &gt; الحسابات &gt; ملف العمل &gt; حذف/إزالة ملف العمل</span>.
            <br />
            بعدها يمكنك إدخال رمز المشرف الجديد بدون أي تعارض.
          </p>
        </div>
      ),
    },
    {
      question: 'ما هي مدة صلاحية رمز المشرف (Enrollment Token) الذي تم توليده؟',
      answer: (
        <p>
          الرمز الذي تنتجه دالة <code className="text-emerald-400 font-mono">enrollmentTokens().create()</code> يظل صالحاً لمدة تصل إلى 30 يوماً بشكل افتراضي من تاريخ توليده، ويمكن إدخاله في أي وقت خلال هذه الفترة على الهاتف لتنشيط ملف العمل.
        </p>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-start gap-4">
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
            <HelpCircle className="w-6 h-6" />
          </div>
          <div className="space-y-1">
            <h2 className="text-lg font-bold text-white">مركز حل المشاكل والأسئلة الشائعة</h2>
            <p className="text-sm text-slate-400 leading-relaxed">
              حلول مباشرة لأبرز الأخطاء والاستفسارات التقنية المتعلقة بـ Google Cloud و Google Colab
              وشاشات إعداد هواتف أندرويد.
            </p>
          </div>
        </div>
      </div>

      {/* Accordion FAQ list */}
      <div className="space-y-3">
        {faqs.map((faq, idx) => {
          const isOpen = openIndex === idx;
          return (
            <div
              key={idx}
              className="bg-slate-900 border border-slate-800 rounded-xl overflow-hidden transition-colors"
            >
              <button
                type="button"
                onClick={() => setOpenIndex(isOpen ? null : idx)}
                className="w-full p-4 text-right flex items-center justify-between gap-4 hover:bg-slate-850 transition-colors"
              >
                <span className="text-sm font-semibold text-slate-200">{faq.question}</span>
                <span className="text-slate-400 shrink-0">
                  {isOpen ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                </span>
              </button>

              {isOpen && (
                <div className="p-4 pt-1 border-t border-slate-800/80 text-xs text-slate-400 leading-relaxed bg-slate-950/40">
                  {faq.answer}
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};
