package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AndroidPolicy
import com.example.model.PasswordScope
import com.example.model.PlayStoreMode
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald600
import com.example.theme.Emerald950
import com.example.theme.Slate200
import com.example.theme.Slate400
import com.example.theme.Slate500
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import com.example.ui.components.CodeBlock
import com.example.util.TokenParser

@Composable
fun PolicyCustomizerScreen(
    policy: AndroidPolicy,
    onPolicyChange: (AndroidPolicy) -> Unit,
    modifier: Modifier = Modifier
) {
    val policyJson = TokenParser.buildPolicyJson(policy)

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
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "مخصص سياسات ملف العمل (Android Policy)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "تحكّم في إعدادات الأمان والتطبيقات وقفل الشاشة التي ستُطبّق تلقائياً على هاتف المستخدم عند إدخال رمز المشرف. التغييرات تنعكس فورياً في كود الخلية الثانية.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        // Play Store Mode Selector
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "وضع متجر Google Play في ملف العمل:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Slate200
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModeSelectorPill(
                    title = "سماح بكافة التطبيقات (BLACKLIST)",
                    subtitle = "الوضع الموصى به للمرونة الكاملة",
                    selected = policy.playStoreMode == PlayStoreMode.BLACKLIST,
                    onClick = { onPolicyChange(policy.copy(playStoreMode = PlayStoreMode.BLACKLIST)) },
                    modifier = Modifier.weight(1f)
                )
                ModeSelectorPill(
                    title = "تقييد صارم (WHITELIST)",
                    subtitle = "السماح فقط بتطبيقات محددة",
                    selected = policy.playStoreMode == PlayStoreMode.WHITELIST,
                    onClick = { onPolicyChange(policy.copy(playStoreMode = PlayStoreMode.WHITELIST)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Toggles List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "إعدادات الأمان والخصائص:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Slate200
            )

            PolicyToggleRow(
                title = "السماح بنسخ النصوص ومشاركتها مع الملف الشخصي",
                subtitle = "مشاركة الحافظة بين ملف العمل والملف الشخصي",
                icon = Icons.Default.ContentPaste,
                checked = policy.crossProfileCopyPasteAllowed,
                onCheckedChange = { onPolicyChange(policy.copy(crossProfileCopyPasteAllowed = it)) }
            )

            PolicyToggleRow(
                title = "السماح بتثبيت تطبيقات من مصادر خارجية (APK)",
                subtitle = "تمكين Unknown Sources لتثبيت الحزم اليدوية",
                icon = Icons.Default.Download,
                checked = policy.installUnknownSourcesAllowed,
                onCheckedChange = { onPolicyChange(policy.copy(installUnknownSourcesAllowed = it)) }
            )

            PolicyToggleRow(
                title = "تعطيل الكاميرا في ملف العمل",
                subtitle = "منع التطبيقات داخل ملف العمل من تشغيل الكاميرا",
                icon = Icons.Default.CameraAlt,
                checked = policy.cameraDisabled,
                onCheckedChange = { onPolicyChange(policy.copy(cameraDisabled = it)) }
            )

            PolicyToggleRow(
                title = "منع لقطات الشاشة وتسجيل الشاشة",
                subtitle = "حماية المحتوى السري من التسجيل أو التقاط الصور",
                icon = Icons.Default.Screenshot,
                checked = policy.screenCaptureDisabled,
                onCheckedChange = { onPolicyChange(policy.copy(screenCaptureDisabled = it)) }
            )

            PolicyToggleRow(
                title = "منع تعديل أو حذف حسابات جوجل",
                subtitle = "تأمين حساب العمل ضد الإزالة اليدوية",
                icon = Icons.Default.Person,
                checked = policy.modifyAccountsDisabled,
                onCheckedChange = { onPolicyChange(policy.copy(modifyAccountsDisabled = it)) }
            )

            PolicyToggleRow(
                title = "منع إعادة ضبط المصنع (Factory Reset)",
                subtitle = "حظر إعادة ضبط المصنع للجهاز",
                icon = Icons.Default.RestartAlt,
                checked = policy.factoryResetDisabled,
                onCheckedChange = { onPolicyChange(policy.copy(factoryResetDisabled = it)) }
            )

            PolicyToggleRow(
                title = "إلزام الوقت التلقائي من الشبكة",
                subtitle = "منع المستخدم من تغيير ساعة وتاريخ الهاتف يدوياً",
                icon = Icons.Default.Schedule,
                checked = policy.autoTimeRequired,
                onCheckedChange = { onPolicyChange(policy.copy(autoTimeRequired = it)) }
            )
        }

        // Live JSON Preview
        CodeBlock(
            title = "معاينة JSON لسياسة Android Management API",
            code = policyJson,
            language = "json"
        )
    }
}

@Composable
private fun ModeSelectorPill(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Emerald950 else Slate950)
            .border(1.dp, if (selected) Emerald500 else Slate800, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Emerald400 else Slate200
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Slate400,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun PolicyToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Slate950)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) Emerald400 else Slate500,
                modifier = Modifier.size(18.dp)
            )
            Column {
                Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate200)
                Text(text = subtitle, fontSize = 9.sp, color = Slate400)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Slate950,
                checkedTrackColor = Emerald500,
                uncheckedThumbColor = Slate400,
                uncheckedTrackColor = Slate800
            )
        )
    }
}
