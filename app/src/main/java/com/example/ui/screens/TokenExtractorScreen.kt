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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExtractionResult
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald600
import com.example.theme.Emerald950
import com.example.theme.Rose400
import com.example.theme.Slate100
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate500
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import com.example.util.TokenParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TokenExtractorScreen(
    onTokenExtracted: (String) -> Unit,
    onNavigateToWizard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<ExtractionResult?>(null) }
    var copied by remember { mutableStateOf(false) }

    fun processInput(text: String) {
        input = text
        if (text.isBlank()) {
            result = null
            return
        }
        val res = TokenParser.extractEnterpriseToken(text)
        result = res
        if (res.success && res.token != null) {
            onTokenExtracted(res.token)
        }
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "مستخرج رمز المشرف السريع (Enterprise Token)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "يقوم هذا المستخرج الذكي بقراءة روابط إعادة التوجيه التلقائية (مثل رابط localhost بعد موافقة جوجل) واستخراج enterpriseToken فوراً دون الحاجة للبحث اليدوي في شريط العناوين.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        // Input Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "الصق الرابط أو النص هنا:", fontSize = 12.sp, color = Slate300, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = clipboard.primaryClip
                            if (clip != null && clip.itemCount > 0) {
                                val pasted = clip.getItemAt(0).text?.toString() ?: ""
                                processInput(pasted)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(height = 32.dp, width = 85.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate200)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(12.dp), tint = Emerald400)
                            Text("لصق", fontSize = 11.sp)
                        }
                    }
                    if (input.isNotBlank()) {
                        OutlinedButton(
                            onClick = { processInput("") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(height = 32.dp, width = 80.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose400)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                                Text("مسح", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = input,
                onValueChange = { processInput(it) },
                placeholder = { Text("https://localhost/?enterpriseToken=ya29.a0AfH6SM...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Emerald500,
                    unfocusedBorderColor = Slate700,
                    focusedTextColor = Slate100,
                    unfocusedTextColor = Slate100,
                    focusedContainerColor = Slate950,
                    unfocusedContainerColor = Slate950
                ),
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Result State
            result?.let { res ->
                if (res.success && res.token != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Emerald950)
                            .border(1.dp, Emerald500.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✅ تم استخراج الرمز بنجاح!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Emerald400
                                )
                                OutlinedButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("enterpriseToken", res.token))
                                        copied = true
                                        scope.launch {
                                            delay(2000)
                                            copied = false
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate100)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(if (copied) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(if (copied) "تم النسخ" else "نسخ", fontSize = 11.sp)
                                    }
                                }
                            }
                            Text(
                                text = res.token,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Slate200,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToWizard,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("تطبيق الرمز والعودة للدليل التفاعلي", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else if (res.error != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Slate950)
                            .border(1.dp, Rose400.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "❌ ${res.error}",
                            fontSize = 12.sp,
                            color = Rose400,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
