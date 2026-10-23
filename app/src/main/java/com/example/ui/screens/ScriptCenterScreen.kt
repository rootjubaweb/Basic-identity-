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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AndroidPolicy
import com.example.theme.Emerald400
import com.example.theme.Emerald500
import com.example.theme.Emerald600
import com.example.theme.Emerald950
import com.example.theme.Slate200
import com.example.theme.Slate300
import com.example.theme.Slate400
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import com.example.ui.components.CodeBlock
import com.example.util.TokenParser

@Composable
fun ScriptCenterScreen(
    projectId: String,
    enterpriseToken: String,
    policy: AndroidPolicy,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Colab, 1: Python, 2: cURL

    val step1Code = TokenParser.generateColabStep1Code(projectId)
    val step2Code = TokenParser.generateColabStep2Code(projectId, enterpriseToken, policy)
    val pythonScript = TokenParser.generateStandalonePythonScript(projectId, policy)

    val curlCommand = """
# 1. الحصول على Access Token من Google Cloud SDK
gcloud auth print-access-token

# 2. إنشاء رابط التسجيل
curl -X POST "https://androidmanagement.googleapis.com/v1/signupUrls?projectId=${projectId.ifEmpty { "YOUR_PROJECT_ID" }}&callbackUrl=https://localhost" \
  -H "Authorization: Bearer $(gcloud auth print-access-token)" \
  -H "Content-Type: application/json"
""".trimIndent()

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
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "مركز الأكواد والبرمجيات النصية (Script Center)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Slate200
                )
            }
            Text(
                text = "جميع الأكواد البرمجية الجاهزة لتوليد رمز المشرف عبر بايثون أو جوجل كولاب أو أسطر أوامر cURL مباشرة دون الحاجة لأي خوادم خارجية.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                lineHeight = 18.sp
            )
        }

        // Subtabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ScriptTabItem(title = "Google Colab", selected = selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.weight(1f))
            ScriptTabItem(title = "سكربت Python مستقل", selected = selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.weight(1f))
            ScriptTabItem(title = "أوامر cURL / Terminal", selected = selectedTab == 2, onClick = { selectedTab = 2 }, modifier = Modifier.weight(1f))
        }

        when (selectedTab) {
            0 -> {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    CodeBlock(title = "كولاب - الخلية 1 (إنشاء الرابط)", code = step1Code)
                    CodeBlock(title = "كولاب - الخلية 2 (تفعيل المؤسسة والسياسة)", code = step2Code)
                }
            }
            1 -> {
                CodeBlock(title = "سكربت بايثون مستقل (enrollment_generator.py)", code = pythonScript)
            }
            2 -> {
                CodeBlock(title = "أوامر cURL مع gcloud CLI", code = curlCommand, language = "bash")
            }
        }
    }
}

@Composable
private fun ScriptTabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Emerald600 else Slate900)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Slate950 else Slate300
        )
    }
}
