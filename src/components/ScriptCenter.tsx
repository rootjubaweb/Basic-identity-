import React, { useState } from 'react';
import { AndroidPolicy } from '../types/android';
import {
  generateColabStep1Code,
  generateColabStep2Code,
  generateStandalonePythonScript,
  generateIpynbContent,
} from '../utils/tokenParser';
import { Terminal, Copy, Check, Download, ExternalLink, Code2 } from 'lucide-react';

interface ScriptCenterProps {
  projectId: string;
  enterpriseToken: string;
  policy: AndroidPolicy;
}

export const ScriptCenter: React.FC<ScriptCenterProps> = ({
  projectId,
  enterpriseToken,
  policy,
}) => {
  const [activeTab, setActiveTab] = useState<'colab1' | 'colab2' | 'python' | 'curl'>('colab1');
  const [copied, setCopied] = useState<string | null>(null);

  const cell1 = generateColabStep1Code(projectId);
  const cell2 = generateColabStep2Code(projectId, enterpriseToken, policy);
  const pythonScript = generateStandalonePythonScript(projectId, policy);

  const curlExample = `# 1. إنشاء رابط التسجيل باستخدام REST API
curl -X POST "https://androidmanagement.googleapis.com/v1/signupUrls?projectId=${projectId || 'YOUR_PROJECT_ID'}&callbackUrl=https://localhost" \\
  -H "Authorization: Bearer $(gcloud auth print-access-token)" \\
  -H "Content-Type: application/json"

# 2. إنشاء المؤسسة بعد الحصول على enterpriseToken
curl -X POST "https://androidmanagement.googleapis.com/v1/enterprises?projectId=${projectId || 'YOUR_PROJECT_ID'}&signupUrlName=ENTERPRISE_SIGNUP_URL_NAME&enterpriseToken=${enterpriseToken || 'ENTER_YOUR_TOKEN_HERE'}" \\
  -H "Authorization: Bearer $(gcloud auth print-access-token)" \\
  -H "Content-Type: application/json" \\
  -d "{}"`;

  const handleCopy = (text: string, id: string) => {
    navigator.clipboard.writeText(text);
    setCopied(id);
    setTimeout(() => setCopied(null), 2000);
  };

  const handleDownloadIpynb = () => {
    const ipynbStr = generateIpynbContent(projectId, policy);
    const blob = new Blob([ipynbStr], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `android_enterprise_generator_${projectId || 'project'}.ipynb`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleDownloadPy = () => {
    const blob = new Blob([pythonScript], { type: 'text/x-python' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `android_token_gen.py`;
    a.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-start gap-4">
            <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
              <Code2 className="w-6 h-6" />
            </div>
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-white">مكتبة الأكواد والسكريبتات البرمجية</h2>
              <p className="text-sm text-slate-400 leading-relaxed">
                جميع الأكواد المحدثة بمعرف مشروعك وسياساتك جاهزة للتشغيل بنقرة واحدة، سواء عبر
                Google Colab أو كملف بايثون مستقل على حاسوبك أو أوامر cURL.
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <button
              type="button"
              onClick={handleDownloadIpynb}
              className="px-3.5 py-2 text-xs font-semibold bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg transition-colors flex items-center gap-1.5 shadow-sm whitespace-nowrap shrink-0"
            >
              <Download className="w-3.5 h-3.5" />
              <span>تنزيل ملف .ipynb لـ Colab</span>
            </button>
            <a
              href="https://colab.research.google.com/#create=true"
              target="_blank"
              rel="noreferrer"
              className="px-3 py-2 text-xs font-medium bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 rounded-lg transition-colors flex items-center gap-1.5 whitespace-nowrap shrink-0"
            >
              <span>فتح Colab جديد</span>
              <ExternalLink className="w-3.5 h-3.5" />
            </a>
          </div>
        </div>
      </div>

      {/* Code Viewer */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl overflow-hidden">
        {/* Sub Header Tabs */}
        <div className="flex items-center justify-between border-b border-slate-800 px-4 py-2 bg-slate-950/60">
          <div className="flex items-center gap-1">
            <button
              type="button"
              onClick={() => setActiveTab('colab1')}
              className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
                activeTab === 'colab1'
                  ? 'bg-emerald-500/15 text-emerald-300 font-semibold'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              الخلية 1 (Colab Step 1)
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('colab2')}
              className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
                activeTab === 'colab2'
                  ? 'bg-emerald-500/15 text-emerald-300 font-semibold'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              الخلية 2 (Colab Step 2)
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('python')}
              className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
                activeTab === 'python'
                  ? 'bg-emerald-500/15 text-emerald-300 font-semibold'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              سكريبت بايثون مستقل (.py)
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('curl')}
              className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
                activeTab === 'curl'
                  ? 'bg-emerald-500/15 text-emerald-300 font-semibold'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              أوامر cURL / REST API
            </button>
          </div>

          <div>
            {activeTab === 'colab1' && (
              <button
                type="button"
                onClick={() => handleCopy(cell1, 'c1')}
                className="px-3 py-1 text-xs font-medium text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 rounded-md transition-colors flex items-center gap-1.5"
              >
                {copied === 'c1' ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                <span>{copied === 'c1' ? 'تم النسخ!' : 'نسخ الكود'}</span>
              </button>
            )}
            {activeTab === 'colab2' && (
              <button
                type="button"
                onClick={() => handleCopy(cell2, 'c2')}
                className="px-3 py-1 text-xs font-medium text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 rounded-md transition-colors flex items-center gap-1.5"
              >
                {copied === 'c2' ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                <span>{copied === 'c2' ? 'تم النسخ!' : 'نسخ الكود'}</span>
              </button>
            )}
            {activeTab === 'python' && (
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={handleDownloadPy}
                  className="px-2.5 py-1 text-xs font-medium text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 rounded-md transition-colors flex items-center gap-1"
                >
                  <Download className="w-3.5 h-3.5" />
                  <span>تنزيل .py</span>
                </button>
                <button
                  type="button"
                  onClick={() => handleCopy(pythonScript, 'py')}
                  className="px-3 py-1 text-xs font-medium text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 rounded-md transition-colors flex items-center gap-1.5"
                >
                  {copied === 'py' ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{copied === 'py' ? 'تم النسخ!' : 'نسخ الكود'}</span>
                </button>
              </div>
            )}
            {activeTab === 'curl' && (
              <button
                type="button"
                onClick={() => handleCopy(curlExample, 'curl')}
                className="px-3 py-1 text-xs font-medium text-slate-300 hover:text-white bg-slate-800 hover:bg-slate-700 rounded-md transition-colors flex items-center gap-1.5"
              >
                {copied === 'curl' ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                <span>{copied === 'curl' ? 'تم النسخ!' : 'نسخ الأوامر'}</span>
              </button>
            )}
          </div>
        </div>

        {/* Code Content */}
        <div className="p-4 bg-slate-950">
          <pre
            dir="ltr"
            className="text-xs text-slate-300 font-mono overflow-x-auto leading-relaxed max-h-[500px]"
          >
            {activeTab === 'colab1' && cell1}
            {activeTab === 'colab2' && cell2}
            {activeTab === 'python' && pythonScript}
            {activeTab === 'curl' && curlExample}
          </pre>
        </div>
      </div>
    </div>
  );
};
