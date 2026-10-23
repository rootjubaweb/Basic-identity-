package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald600
import com.example.theme.Emerald950
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate500
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import com.example.ui.components.CodeBlock
import com.example.util.QrBitmapGenerator
import com.example.util.TokenParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QrCodeCardScreen(
    enrollmentToken: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDpcBundle by remember { mutableStateOf(true) }
    var copiedPayload by remember { mutableStateOf(false) }

    val activeToken = enrollmentToken.trim().ifEmpty { "ENROLLMENT_TOKEN_PLACEHOLDER" }
    val payloadString = if (isDpcBundle) {
        TokenParser.generateAndroidDpcQrPayload(activeToken)
    } else {
        activeToken
    }

    val qrBitmap = remember(payloadString) {
        QrBitmapGenerator.generateQrBitmap(
            content = payloadString,
            size = 512,
            darkColor = android.graphics.Color.BLACK,
            lightColor = android.graphics.Color.WHITE
        )
    }

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
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "بطاقة رمز الاستجابة السريعة (Provisioning QR Code)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "امسح هذا الرمز مباشرة من شاشة إعداد هاتف أندرويد لتنزيل مشغّل ملف العمل (Google Cloud DPC) وبدء الإعداد التلقائي بضغطة زر واحدة.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        // Toggle Payload Type
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isDpcBundle) Emerald600 else Slate900)
                    .clickable { isDpcBundle = true }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "حزمة Cloud DPC (الموصى بها)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDpcBundle) Slate950 else Slate300
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (!isDpcBundle) Emerald600 else Slate900)
                    .clickable { isDpcBundle = false }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "الرمز الخام فقط (Raw Token)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isDpcBundle) Slate950 else Slate300
                )
            }
        }

        // QR Code Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(androidx.compose.ui.graphics.Color.White)
                    .padding(14.dp)
            ) {
                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(240.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier.size(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "جاري إنشاء كود QR...", color = Slate700)
                    }
                }
            }

            // Copy payload action
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("QR Payload", payloadString))
                    copiedPayload = true
                    scope.launch {
                        delay(2000)
                        copiedPayload = false
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate200)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(if (copiedPayload) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp), tint = Emerald400)
                    Text(if (copiedPayload) "تم نسخ محتوى QR!" else "نسخ محتوى رمز QR كـ JSON", fontSize = 11.sp)
                }
            }
        }

        // 6-Tap Phone Scanning Instruction Guide
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Smartphone, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                Text(text = "طريقة المسح من الهاتف (طريقة الـ 6 نقرات الرسمية):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate200)
            }
            Text(text = "1. بعد عمل فورمات للهاتف، في أول شاشة ترحيب (Welcome Screen / مرحباً)، انقر 6 مرات متتالية وسريعة في أي مساحة بيضاء فارغة.", fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
            Text(text = "2. ستظهر رسالة تطلب منك الاتصال بشبكة Wi-Fi لتنزيل قارئ QR.", fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
            Text(text = "3. بعد الاتصال بالواي فاي، ستفتح كاميرا قارئ الرموز تلقائياً.", fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
            Text(text = "4. وجّه الكاميرا نحو رمز QR الظاهر في الأعلى لتفعيل ملف العمل فوراً.", fontSize = 11.sp, color = Slate300, lineHeight = 16.sp)
        }

        // JSON payload viewer
        CodeBlock(
            title = "محتوى حزمة DPC QR",
            code = payloadString,
            language = "json"
        )
    }
}
