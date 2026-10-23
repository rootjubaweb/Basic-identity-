package com.example.ui.components

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald600
import com.example.theme.Emerald950
import com.example.theme.Slate100
import com.example.theme.Slate200
import com.example.theme.Slate400
import com.example.theme.Slate500
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950

@Composable
fun HeroSection(
    onStartWizard: () -> Unit,
    onOpenExtractor: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Slate900, Slate900.copy(alpha = 0.7f))
                )
            )
            .border(1.dp, Slate800, RoundedCornerShape(24.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Metadata kicker badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Emerald950)
                    .border(1.dp, Emerald600.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Google Android Management API",
                    color = Emerald400,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
            Text(text = "·", color = Slate500)
            Text(text = "تجاوز إعداد ملف العمل", color = Slate400, fontSize = 11.sp)
            Text(text = "·", color = Slate500)
            Text(text = "مجاني 100%", color = Emerald400, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        // Title
        Text(
            text = "مولّد رمز المشرف لملف العمل في أندرويد",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            ),
            color = Slate100
        )

        // Subtitle
        Text(
            text = "أنشئ مؤسستك السحابية الافتراضية، وخصّص سياسات التطبيقات وقفل الشاشة، واستخرج رمز المشرف (Enrollment Token) الرسمي مجاناً عبر منصة Google Cloud لتجاوز شاشة إعداد ملف العمل على أي هاتف في دقائق.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 20.sp
            ),
            color = Slate400
        )

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onStartWizard,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald600,
                    contentColor = Slate100
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Smartphone,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = "بدء الدليل", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            OutlinedButton(
                onClick = onOpenExtractor,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Slate200
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(Slate700, Slate700))
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = "المستخرج السريع", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // 4 KPI strip
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCard(
                    label = "المنصة الرسمية",
                    value = "Google Cloud",
                    sub = "Management API v1",
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    label = "بيئة التشغيل",
                    value = "Google Colab",
                    sub = "بدون تثبيت برامج",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCard(
                    label = "سياسة المتجر",
                    value = "BLACKLIST",
                    sub = "سماح بالتطبيقات بحرية",
                    isEmerald = true,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    label = "طريقة التفعيل",
                    value = "DPC QR (6 نقرات)",
                    sub = "تنزيل المشرف تلقائياً",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun KpiCard(
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier,
    isEmerald: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Slate950.copy(alpha = 0.7f))
            .border(1.dp, Slate800, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = label, fontSize = 10.sp, color = Slate400)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isEmerald) Emerald400 else Slate100,
            fontFamily = if (isEmerald) FontFamily.Monospace else FontFamily.SansSerif
        )
        Text(text = sub, fontSize = 9.sp, color = Slate500)
    }
}
