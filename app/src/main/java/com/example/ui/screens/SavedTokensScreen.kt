package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SavedEnrollment
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald950
import com.example.theme.Rose400
import com.example.theme.Slate100
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedTokensScreen(
    savedTokens: List<SavedEnrollment>,
    onSelectTokenForQr: (String) -> Unit,
    onDeleteToken: (String) -> Unit,
    onNavigateToWizard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

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
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "سجل رموز المشرف المحفوظة (${savedTokens.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "الرموز المحفوظة على هذا الجهاز للرجوع إليها لاحقاً أو عرض كود QR الخاص بها في أي وقت دون إعادة تشغيل كود كولاب.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        if (savedTokens.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate900)
                    .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "لا توجد رموز محفوظة حالياً",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate300
                    )
                    Text(
                        text = "قم بإتمام خطوات الدليل التفاعلي واضغط 'حفظ في السجل' في الخطوة الرابعة.",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                    Button(
                        onClick = onNavigateToWizard,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("الانتقال للدليل التفاعلي", color = Slate950, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                savedTokens.forEach { record ->
                    SavedTokenItem(
                        record = record,
                        formattedDate = dateFormat.format(Date(record.createdAt)),
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Saved Token", record.enrollmentToken))
                        },
                        onOpenQr = { onSelectTokenForQr(record.enrollmentToken) },
                        onDelete = { onDeleteToken(record.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedTokenItem(
    record: SavedEnrollment,
    formattedDate: String,
    onCopy: () -> Unit,
    onOpenQr: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Slate900)
            .border(1.dp, Slate800, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "مشروع: ${record.projectId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Slate100
                )
                Text(
                    text = formattedDate,
                    fontSize = 10.sp,
                    color = Slate400
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Emerald950)
                    .border(1.dp, Emerald500.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = record.policySummary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald400,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Slate950)
                .padding(10.dp)
        ) {
            Text(
                text = record.enrollmentToken,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Emerald400,
                maxLines = 2
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCopy,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate200)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                    Text("نسخ", fontSize = 11.sp)
                }
            }
            Button(
                onClick = onOpenQr,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(12.dp))
                    Text("عرض QR", fontSize = 11.sp)
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "حذف", tint = Rose400, modifier = Modifier.size(18.dp))
            }
        }
    }
}
private val Emerald600 = com.example.theme.Emerald600
