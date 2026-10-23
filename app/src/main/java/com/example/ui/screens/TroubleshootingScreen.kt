package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.Amber400
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald950
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950

data class FaqItem(val title: String, val problem: String, val solution: String)

@Composable
fun TroubleshootingScreen(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        FaqItem(
            title = "خطأ: enterpriseToken منتهي الصلاحية أو غير صالح",
            problem = "يظهر لك خطأ Invalid enterpriseToken عند تشغيل الخلية الثانية في Google Colab.",
            solution = "رمز enterpriseToken صالح لمدة ساعة واحدة فقط بعد موافقتك في المتصفح. إذا تأخرت في نسخه، أعد تشغيل الخلية الأولى (Cell 1) وافتح رابط التسجيل الجديد فوراً."
        ),
        FaqItem(
            title = "خطأ: Android Management API has not been used or is disabled",
            problem = "الخلية الأولى تظهر رسالة خطأ 403 API_NOT_ENABLED.",
            solution = "افتح Google Cloud Console وتأكد من اختيار نفس المشروع (Project ID)، ثم اذهب إلى صفحة APIs & Services واضغط 'Enable APIs and Services' وابحث عن 'Android Management API' وفعّلها."
        ),
        FaqItem(
            title = "لا يظهر ماسح QR بعد النقر 6 مرات على الشاشة",
            problem = "الهاتف لا يستجيب للنقر في شاشة البداية.",
            solution = "تأكد من النقر في أول شاشة تظهر بعد الفورمات (شاشة اختيار اللغة Welcome)، وانقر 6 مرات متتالية وسريعة دون توقف في منطقة بيضاء خالية. إذا لم يستجب جهازك، استخدم طريقة afw#setup اليدوية."
        ),
        FaqItem(
            title = "المتصفح يظهر 'لا يمكن الوصول إلى الموقع' بعد الموافقة",
            problem = "عند الموافقة على إنشاء المؤسسة، يحولك المتصفح إلى https://localhost وتظهر صفحة خطأ.",
            solution = "هذا هو السلوك الطبيعي والمطلوب 100%! لا تضغط إعادة تحميل. انظر إلى شريط العنوان في أعلى المتصفح، وانسخ الرابط كاملاً، فستجد رمز enterpriseToken ملحقاً به."
        ),
        FaqItem(
            title = "التطبيقات داخل ملف العمل لا تظهر في Google Play",
            problem = "متجر بلاي داخل ملف العمل فارغ أو يعرض رسالة 'التطبيقات مقيدة بواسطة المشرف'.",
            solution = "تأكد من ضبط وضع متجر بلاي على BLACKLIST في تبويب 'السياسة' قبل إنشاء الرمز. وضع BLACKLIST يسمح بجميع التطبيقات بحرية تامة."
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Emerald950)
                        .border(1.dp, Emerald500.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "حل المشكلات الشائعة (Troubleshooting)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "دليل شامل لأكثر الأخطاء شيوعاً أثناء إعداد المؤسسة واستخراج الرمز وحلولها الفورية خطوة بخطوة.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        // Accordion List
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEach { item ->
                TroubleshootAccordionItem(item)
            }
        }
    }
}

@Composable
private fun TroubleshootAccordionItem(item: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Slate900)
            .border(1.dp, Slate800, RoundedCornerShape(14.dp))
            .clickable { expanded = !expanded }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Amber400,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Slate200
                )
            }
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Slate400,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate950)
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "المشكلة:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Amber400)
                        Text(text = item.problem, fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Emerald950)
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "الحل الموصى به:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Emerald400)
                        Text(text = item.solution, fontSize = 11.sp, color = Slate200, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}
