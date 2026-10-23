package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald950
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate800
import com.example.theme.Slate900

@Composable
fun PhoneGuideScreen(
    modifier: Modifier = Modifier
) {
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
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "دليل تفعيل الهاتف وإعداد ملف العمل (Phone Guide)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "تعرّف على كافة الطرق الرسمية لإدخال رمز المشرف أو مسح كود QR على هواتف أندرويد لتجاوز شاشات الإعداد وتفعيل الوضع المؤسسي.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        // Method 1 Card
        MethodCard(
            number = "1",
            title = "طريقة الـ 6 نقرات (DPC QR Provisioning)",
            subtitle = "أسرع وأسهل طريقة لجميع أجهزة أندرويد الحديثة (Android 9 فما فوق)",
            icon = Icons.Default.QrCodeScanner,
            steps = listOf(
                "أعد ضبط المصنع للهاتف أو ابدأ من شاشة الترحيب الأولى (Welcome / مرحباً).",
                "انقر 6 مرات متتالية وسريعة على أي مساحة بيضاء خالية في الشاشة.",
                "ستظهر لك نافذة إعداد ماسح QR؛ اتصل بشبكة Wi-Fi للمتابعة.",
                "وجّه كاميرا الهاتف نحو كود QR المولد في تبويب 'رمز QR'.",
                "سيقوم الهاتف تلقائياً بتثبيت Google Cloud DPC وإعداد ملف العمل دون أي خطوات إضافية."
            )
        )

        // Method 2 Card
        MethodCard(
            number = "2",
            title = "طريقة كود afw#setup (طريقة المعرف اليدوي)",
            subtitle = "تصلح للأجهزة التي لا تدعم مسح كود QR في شاشة البداية",
            icon = Icons.Default.TouchApp,
            steps = listOf(
                "في شاشة تسجيل الدخول بحساب Google (شاشة إدخال البريد الإلكتروني).",
                "اكتب في خانة الإيميل الكود التالي بالضبط: afw#setup ثم اضغط التالي.",
                "سيتعرف نظام أندرويد على الكود وسيقوم بتحميل أداة إدارة الأجهزة Google Device Policy.",
                "بعد التثبيت، سيطلب منك إدخال رمز المشرف (Enrollment Token)؛ الصق الرمز الذي استخرجته.",
                "وافق على إنشاء ملف العمل واستمتع بالنظام المزدوج."
            )
        )

        // Tips Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                Text(text = "نصائح وإرشادات هامة:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Emerald400)
            }
            Text(text = "• رمز المشرف (Enrollment Token) صالح لفترة زمنية محددة ويُفضل استخدامه خلال ساعات من توليده.", fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
            Text(text = "• إذا أردت تثبيت تطبيقات المتجر بحرية، اختر وضع BLACKLIST في تبويب 'السياسة' قبل توليد الرمز.", fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun MethodCard(
    number: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    steps: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Slate900)
            .border(1.dp, Slate800, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Emerald950)
                    .border(1.dp, Emerald500.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = number, color = Emerald400, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate200)
                Text(text = subtitle, fontSize = 10.sp, color = Slate400)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(start = 6.dp, top = 4.dp)
        ) {
            steps.forEachIndexed { idx, step ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "${idx + 1}.", color = Emerald400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text(text = step, color = Slate300, fontSize = 11.sp, lineHeight = 17.sp)
                }
            }
        }
    }
}
