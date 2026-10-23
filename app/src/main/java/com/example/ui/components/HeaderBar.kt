package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.example.theme.Slate400
import com.example.theme.Slate700
import com.example.theme.Slate800
import com.example.theme.Slate900
import com.example.theme.Slate950
import com.example.viewmodel.AppTab

@Composable
fun HeaderBar(
    activeTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    savedCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Slate950)
            .border(width = 1.dp, color = Slate800)
    ) {
        // Top branding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
                        .border(1.dp, Emerald500.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "App Logo",
                        tint = Emerald400,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Android Enterprise",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = Slate200
                    )
                    Text(
                        text = "مولد رمز المشرف و DPC QR",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Slate400
                    )
                }
            }

            // Saved token indicator button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeTab == AppTab.SAVED) Emerald600 else Slate900)
                    .border(1.dp, if (activeTab == AppTab.SAVED) Emerald500 else Slate800, RoundedCornerShape(10.dp))
                    .clickable { onTabSelected(AppTab.SAVED) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "السجلات المحفوظة",
                        tint = if (activeTab == AppTab.SAVED) Slate950 else Emerald400,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "المحفوظات ($savedCount)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == AppTab.SAVED) Slate950 else Slate200
                    )
                }
            }
        }

        // Horizontal navigation tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TabPill(
                title = "الدليل التفاعلي",
                icon = Icons.Default.PhoneAndroid,
                selected = activeTab == AppTab.WIZARD,
                onClick = { onTabSelected(AppTab.WIZARD) }
            )
            TabPill(
                title = "مستخرج الرمز",
                icon = Icons.Default.AutoAwesome,
                selected = activeTab == AppTab.EXTRACTOR,
                onClick = { onTabSelected(AppTab.EXTRACTOR) }
            )
            TabPill(
                title = "السياسة",
                icon = Icons.Default.Tune,
                selected = activeTab == AppTab.POLICY,
                onClick = { onTabSelected(AppTab.POLICY) }
            )
            TabPill(
                title = "رمز QR",
                icon = Icons.Default.QrCode,
                selected = activeTab == AppTab.QR,
                onClick = { onTabSelected(AppTab.QR) }
            )
            TabPill(
                title = "الأكواد الجاهزة",
                icon = Icons.Default.Code,
                selected = activeTab == AppTab.SCRIPTS,
                onClick = { onTabSelected(AppTab.SCRIPTS) }
            )
            TabPill(
                title = "دليل الهاتف",
                icon = Icons.Default.PhoneAndroid,
                selected = activeTab == AppTab.GUIDE,
                onClick = { onTabSelected(AppTab.GUIDE) }
            )
            TabPill(
                title = "حل المشكلات",
                icon = Icons.Default.HelpOutline,
                selected = activeTab == AppTab.TROUBLESHOOT,
                onClick = { onTabSelected(AppTab.TROUBLESHOOT) }
            )
        }
    }
}

@Composable
private fun TabPill(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Emerald500 else Slate900)
            .border(1.dp, if (selected) Emerald400 else Slate800, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Slate950 else Slate400,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Slate950 else Slate200
            )
        }
    }
}
private val Emerald600 = com.example.theme.Emerald600
