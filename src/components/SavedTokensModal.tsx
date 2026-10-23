import React, { useEffect, useState } from 'react';
import { db, auth, handleFirestoreError, OperationType } from '../lib/firebase';
import { collection, query, where, onSnapshot, deleteDoc, doc } from 'firebase/firestore';
import { User } from 'firebase/auth';
import { AndroidPolicy } from '../types/android';
import { Bookmark, Trash2, Copy, Check, ExternalLink, Calendar, ShieldCheck, AlertCircle } from 'lucide-react';

interface SavedRecord {
  id: string;
  projectId: string;
  enterpriseName?: string;
  enrollmentToken: string;
  policy?: AndroidPolicy;
  notes?: string;
  createdAt: string;
}

interface SavedTokensModalProps {
  currentUser: User | null;
  onSelectToken: (token: string, projectId: string, policy?: AndroidPolicy) => void;
  onClose: () => void;
}

export const SavedTokensModal: React.FC<SavedTokensModalProps> = ({
  currentUser,
  onSelectToken,
  onClose,
}) => {
  const [tokens, setTokens] = useState<SavedRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [copiedId, setCopiedId] = useState<string | null>(null);

  useEffect(() => {
    if (!currentUser) {
      setTokens([]);
      setLoading(false);
      return;
    }

    const q = query(collection(db, 'enrollments'), where('userId', '==', currentUser.uid));
    const unsubscribe = onSnapshot(
      q,
      (snapshot) => {
        const items: SavedRecord[] = [];
        snapshot.forEach((docSnap) => {
          items.push({ id: docSnap.id, ...docSnap.data() } as SavedRecord);
        });
        items.sort((a, b) => (b.createdAt || '').localeCompare(a.createdAt || ''));
        setTokens(items);
        setLoading(false);
      },
      (error) => {
        console.error('Error fetching saved tokens:', error);
        handleFirestoreError(error, OperationType.GET, 'enrollments');
        setLoading(false);
      }
    );

    return () => unsubscribe();
  }, [currentUser]);

  const handleDelete = async (id: string) => {
    if (!window.confirm('هل أنت متأكد من حذف هذا الرمز من قائمتك المحفوظة؟')) return;
    try {
      await deleteDoc(doc(db, 'enrollments', id));
    } catch (error) {
      handleFirestoreError(error, OperationType.DELETE, `enrollments/${id}`);
    }
  };

  const handleCopy = (token: string, id: string) => {
    navigator.clipboard.writeText(token);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-start justify-between gap-4">
          <div className="flex items-start gap-4">
            <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 rounded-xl shrink-0">
              <Bookmark className="w-6 h-6" />
            </div>
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-white">رموز المشرف المحفوظة في حسابك</h2>
              <p className="text-sm text-slate-400 leading-relaxed">
                قائمة بالرموز والمشاريع التي قمت بتوليدها وحفظها في قاعدة بياناتك السحابية الخاصة
                للوصول إليها في أي وقت ومن أي جهاز.
              </p>
            </div>
          </div>
        </div>
      </div>

      {!currentUser && (
        <div className="p-6 bg-slate-900 border border-slate-800 rounded-xl text-center space-y-3">
          <AlertCircle className="w-8 h-8 text-amber-400 mx-auto" />
          <h3 className="text-sm font-bold text-white">يرجى تسجيل الدخول بحساب Google</h3>
          <p className="text-xs text-slate-400 max-w-md mx-auto">
            قم بتسجيل الدخول من الزر في أعلى الصفحة لمزامنة وحفظ رموز المشرف الخاصة بك بأمان في السحابة.
          </p>
        </div>
      )}

      {currentUser && loading && (
        <div className="p-10 text-center text-xs text-slate-400">
          جاري تحميل الرموز المحفوظة...
        </div>
      )}

      {currentUser && !loading && tokens.length === 0 && (
        <div className="p-8 bg-slate-900 border border-slate-800 rounded-xl text-center space-y-2">
          <ShieldCheck className="w-8 h-8 text-slate-500 mx-auto" />
          <h3 className="text-sm font-semibold text-slate-300">لا توجد رموز محفوظة بعد</h3>
          <p className="text-xs text-slate-500">
            عند اكتمال توليد أي رمز في الخطوة الرابعة، اضغط على زر «حفظ الرمز في حسابي».
          </p>
        </div>
      )}

      {currentUser && !loading && tokens.length > 0 && (
        <div className="space-y-3">
          {tokens.map((item) => (
            <div
              key={item.id}
              className="bg-slate-900 border border-slate-800 rounded-xl p-4 flex flex-col sm:flex-row sm:items-center justify-between gap-4 hover:border-slate-700 transition-colors"
            >
              <div className="space-y-1.5 flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <span className="text-xs font-semibold text-white">
                    مشروع: {item.projectId}
                  </span>
                  {item.createdAt && (
                    <span className="text-[10px] text-slate-500 flex items-center gap-1 font-mono">
                      <Calendar className="w-3 h-3" />
                      {new Date(item.createdAt).toLocaleDateString('ar-EG', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric',
                      })}
                    </span>
                  )}
                </div>

                <div
                  dir="ltr"
                  className="text-xs font-mono text-emerald-300 truncate bg-slate-950 px-2 py-1 rounded border border-slate-800/80"
                >
                  {item.enrollmentToken}
                </div>

                {item.notes && (
                  <p className="text-[11px] text-slate-400 line-clamp-1">{item.notes}</p>
                )}
              </div>

              <div className="flex items-center gap-2 shrink-0">
                <button
                  type="button"
                  onClick={() => handleCopy(item.enrollmentToken, item.id)}
                  className="px-2.5 py-1.5 text-xs font-medium bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-lg transition-colors flex items-center gap-1"
                >
                  {copiedId === item.id ? (
                    <>
                      <Check className="w-3.5 h-3.5 text-emerald-400" />
                      <span>تم!</span>
                    </>
                  ) : (
                    <>
                      <Copy className="w-3.5 h-3.5" />
                      <span>نسخ</span>
                    </>
                  )}
                </button>

                <button
                  type="button"
                  onClick={() => onSelectToken(item.enrollmentToken, item.projectId, item.policy)}
                  className="px-3 py-1.5 text-xs font-medium bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg transition-colors flex items-center gap-1 shadow-sm"
                >
                  <span>عرض في QR</span>
                </button>

                <button
                  type="button"
                  onClick={() => handleDelete(item.id)}
                  className="p-1.5 text-slate-500 hover:text-red-400 hover:bg-slate-800 rounded-lg transition-colors"
                  title="حذف الرمز"
                  aria-label="حذف"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
