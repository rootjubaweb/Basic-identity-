package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AndroidPolicy
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald600
import com.example.theme.Emerald950
import com.example.theme.Rose400
import com.example.theme.Rose500
import com.example.theme.Slate100
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate500
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import com.example.ui.components.CodeBlock
import com.example.util.TokenParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StepWizardScreen(
    projectId: String,
    onProjectIdChange: (String) -> Unit,
    enterpriseToken: String,
    onEnterpriseTokenChange: (String) -> Unit,
    enrollmentToken: String,
    onEnrollmentTokenChange: (String) -> Unit,
    policy: AndroidPolicy,
    onNavigateToPolicy: () -> Unit,
    onNavigateToQr: () -> Unit,
    onSaveToken: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var inputUrl by remember { mutableStateOf("") }
    var extractError by remember { mutableStateOf<String?>(null) }
    var copiedToken by remember { mutableStateOf(false) }

    val projectValidation = TokenParser.validateProjectId(projectId)
    val step1Code = TokenParser.generateColabStep1Code(projectId)
    val step2Code = TokenParser.generateColabStep2Code(projectId, enterpriseToken, policy)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step progress header (4 pills)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StepTabItem(stepNumber = 1, title = "1. تهيئة Google Cloud", active = step == 1, completed = step > 1, onClick = { step = 1 }, modifier = Modifier.weight(1f))
            StepTabItem(stepNumber = 2, title = "2. رابط التسجيل", active = step == 2, completed = step > 2, onClick = { step = 2 }, modifier = Modifier.weight(1f))
            StepTabItem(stepNumber = 3, title = "3. استخراج الرمز", active = step == 3, completed = step > 3, onClick = { step = 3 }, modifier = Modifier.weight(1f))
            StepTabItem(stepNumber = 4, title = "4. رمز المشرف", active = step == 4, completed = step > 4, onClick = { step = 4 }, modifier = Modifier.weight(1f))
        }

        // STEP 1 CONTENT
        if (step == 1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate900)
                    .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "الخطوة الأولى: تحديد معرف مشروع Google Cloud",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
                Text(
                    text = "أنشئ مشروعاً مجانياً على Google Cloud Console وقم بتفعيل مكتبة Android Management API، ثم أدخل معرف المشروع (Project ID) هنا.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate400
                )

                OutlinedTextField(
                    value = projectId,
                    onValueChange = onProjectIdChange,
                    label = { Text("معرف المشروع (Project ID)") },
                    placeholder = { Text("مثال: android-work-profile-1234") },
                    isError = !projectValidation.valid,
                    supportingText = {
                        if (!projectValidation.valid && projectValidation.message != null) {
                            Text(text = projectValidation.message, color = Rose400, fontSize = 11.sp)
                        } else {
                            Text(text = "معرف المشروع متاح وصالح", color = Emerald400, fontSize = 11.sp)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Slate100,
                        unfocusedTextColor = Slate100,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Info Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate950)
                        .border(1.dp, Slate800, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "💡 تنبيه مهم:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Emerald400
                        )
                        Text(
                            text = "• تأكد من تفعيل Android Management API في مشروعك من صفحة APIs & Services.\n• لا تحتاج إلى إدخال أي بطاقة ائتمان أو دفع أي رسوم؛ الخدمة مجانية تماماً لحسابات العمل التجريبية والمشرفين.",
                            fontSize = 11.sp,
                            color = Slate400,
                            lineHeight = 17.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { if (projectValidation.valid) step = 2 },
                        enabled = projectValidation.valid,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("متابعة للخطوة 2", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // STEP 2 CONTENT
        if (step == 2) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate900)
                    .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "الخطوة الثانية: تشغيل الخلية الأولى في Google Colab",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
                Text(
                    text = "انسخ هذا الكود والصقه في دفتر Google Colab لتسجيل الدخول بحساب Google وإنشاء رابط تسجيل المؤسسة التلقائي (signupUrl).",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate400
                )

                CodeBlock(
                    title = "خلية كولاب 1: إنشاء رابط التسجيل",
                    code = step1Code
                )

                // Instruction cards
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate950)
                        .border(1.dp, Slate800, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "ماذا تفعل بعد تشغيل الكود في Colab؟", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Emerald400)
                    Text(text = "1. سيظهر رابط يبدأ بـ https://enterprise.google.com/android/enroll ... اضغط عليه.", fontSize = 11.sp, color = Slate300)
                    Text(text = "2. سجّل الدخول بحساب جوجل واضغط 'بدء الاستخدام' ثم وافق على الشروط.", fontSize = 11.sp, color = Slate300)
                    Text(text = "3. سيحولك المتصفح لصفحة localhost لا تفتح؛ انسخ الرابط كاملاً من شريط العنوان.", fontSize = 11.sp, color = Slate300)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { step = 1 },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("السابق", fontSize = 12.sp, color = Slate300)
                    }
                    Button(
                        onClick = { step = 3 },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("متابعة للخطوة 3", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // STEP 3 CONTENT
        if (step == 3) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate900)
                    .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "الخطوة الثالثة: استخراج الرمز وتوليد كود الخلية الثانية",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )

                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = { newVal ->
                        inputUrl = newVal
                        if (newVal.isBlank()) {
                            extractError = null
                        } else {
                            val res = TokenParser.extractEnterpriseToken(newVal)
                            if (res.success && res.token != null) {
                                onEnterpriseTokenChange(res.token)
                                extractError = null
                            } else {
                                extractError = res.error
                            }
                        }
                    },
                    label = { Text("الصق رابط localhost أو قيمة enterpriseToken هنا") },
                    placeholder = { Text("https://localhost/?enterpriseToken=ya29....") },
                    isError = extractError != null,
                    supportingText = {
                        if (extractError != null) {
                            Text(text = extractError ?: "", color = Rose400, fontSize = 11.sp)
                        } else if (enterpriseToken.isNotBlank()) {
                            Text(text = "✅ تم استخراج الرمز بنجاح!", color = Emerald400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Slate100,
                        unfocusedTextColor = Slate100,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Customize Policy Action
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate950)
                        .border(1.dp, Slate800, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "السياسة الحالية: ${policy.playStoreMode.name}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Emerald400)
                        Text(text = "يمكنك تخصيص صلاحيات الكاميرا والتطبيقات ولقطات الشاشة", fontSize = 10.sp, color = Slate400)
                    }
                    OutlinedButton(
                        onClick = onNavigateToPolicy,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate200)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp), tint = Emerald400)
                            Text("تخصيص", fontSize = 11.sp)
                        }
                    }
                }

                // Colab Step 2 Code Block
                CodeBlock(
                    title = "خلية كولاب 2: تطبيق السياسة واستخراج الرمز النهائي",
                    code = step2Code
                )

                // Input for Final Enrollment Token (copied from colab output)
                OutlinedTextField(
                    value = enrollmentToken,
                    onValueChange = onEnrollmentTokenChange,
                    label = { Text("رمز المشرف المستخرج من كولاب (Enrollment Token)") },
                    placeholder = { Text("الصق رمز المشرف النهائي هنا...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Slate100,
                        unfocusedTextColor = Slate100,
                        focusedContainerColor = Slate950,
                        unfocusedContainerColor = Slate950
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { step = 2 },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("السابق", fontSize = 12.sp, color = Slate300)
                    }
                    Button(
                        onClick = { step = 4 },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("متابعة للخطوة 4", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // STEP 4 CONTENT
        if (step == 4) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Slate900)
                    .border(1.dp, Slate800, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Emerald950)
                            .border(1.dp, Emerald500, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(text = "تهانينا! رمز المشرف جاهز للاستخدام", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate100)
                        Text(text = "يمكنك إدخال هذا الرمز مباشرة في شاشة إعداد الهاتف أو مسحه عبر كود QR", fontSize = 11.sp, color = Slate400)
                    }
                }

                // Token display card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Slate950)
                        .border(1.dp, Emerald600.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "رمز المشرف النهائي (Enrollment Token):", fontSize = 11.sp, color = Slate400)
                        Text(
                            text = enrollmentToken.ifBlank { "لم تقم بلصق الرمز بعد، يمكنك وضعه في الخطوة 3" },
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (enrollmentToken.isNotBlank()) Emerald400 else Slate500
                        )
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (enrollmentToken.isNotBlank()) {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Enrollment Token", enrollmentToken))
                                copiedToken = true
                                scope.launch {
                                    delay(2000)
                                    copiedToken = false
                                }
                            }
                        },
                        enabled = enrollmentToken.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(if (copiedToken) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text(if (copiedToken) "تم النسخ!" else "نسخ الرمز", fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = onSaveToken,
                        enabled = enrollmentToken.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate200),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(14.dp), tint = Emerald400)
                            Text("حفظ في السجل", fontSize = 11.sp)
                        }
                    }
                }

                // Open QR Action
                Button(
                    onClick = onNavigateToQr,
                    colors = ButtonDefaults.buttonColors(containerColor = Slate800, contentColor = Slate100),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.QrCode, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                        Text("عرض كود DPC QR للمسح بالكاميرا (6 نقرات)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    OutlinedButton(
                        onClick = { step = 3 },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("الرجوع للخطوة 3", fontSize = 12.sp, color = Slate300)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepTabItem(
    stepNumber: Int,
    title: String,
    active: Boolean,
    completed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (active) Emerald600 else if (completed) Emerald950 else Slate900)
            .border(1.dp, if (active) Emerald500 else if (completed) Emerald600.copy(alpha = 0.5f) else Slate800, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            color = if (active) Slate950 else if (completed) Emerald400 else Slate400,
            maxLines = 1
        )
    }
}
