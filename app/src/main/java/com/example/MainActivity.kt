package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.theme.AndroidEnterpriseTheme
import com.example.theme.Slate950
import com.example.ui.components.HeaderBar
import com.example.ui.components.HeroSection
import com.example.ui.screens.PhoneGuideScreen
import com.example.ui.screens.PolicyCustomizerScreen
import com.example.ui.screens.QrCodeCardScreen
import com.example.ui.screens.SavedTokensScreen
import com.example.ui.screens.ScriptCenterScreen
import com.example.ui.screens.StepWizardScreen
import com.example.ui.screens.TokenExtractorScreen
import com.example.ui.screens.TroubleshootingScreen
import com.example.viewmodel.AppTab
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidEnterpriseTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    EnterpriseTokenMasterApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun EnterpriseTokenMasterApp(viewModel: MainViewModel) {
    val activeTab by viewModel.activeTab.collectAsState()
    val projectId by viewModel.projectId.collectAsState()
    val enterpriseToken by viewModel.enterpriseToken.collectAsState()
    val enrollmentToken by viewModel.enrollmentToken.collectAsState()
    val policy by viewModel.policy.collectAsState()
    val savedList by viewModel.savedEnrollments.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    // Back button returns to Wizard if in sub-screen
    BackHandler(enabled = activeTab != AppTab.WIZARD) {
        viewModel.selectTab(AppTab.WIZARD)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HeaderBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.selectTab(it) },
                savedCount = savedList.size
            )
        },
        containerColor = Slate950
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate950),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Hero Section displayed on wizard view
                if (activeTab == AppTab.WIZARD) {
                    HeroSection(
                        onStartWizard = { viewModel.selectTab(AppTab.WIZARD) },
                        onOpenExtractor = { viewModel.selectTab(AppTab.EXTRACTOR) }
                    )
                }

                // Main Tab View
                when (activeTab) {
                    AppTab.WIZARD -> {
                        StepWizardScreen(
                            projectId = projectId,
                            onProjectIdChange = { viewModel.setProjectId(it) },
                            enterpriseToken = enterpriseToken,
                            onEnterpriseTokenChange = { viewModel.setEnterpriseToken(it) },
                            enrollmentToken = enrollmentToken,
                            onEnrollmentTokenChange = { viewModel.setEnrollmentToken(it) },
                            policy = policy,
                            onNavigateToPolicy = { viewModel.selectTab(AppTab.POLICY) },
                            onNavigateToQr = { viewModel.selectTab(AppTab.QR) },
                            onSaveToken = { viewModel.saveCurrentEnrollmentToken() }
                        )
                    }
                    AppTab.EXTRACTOR -> {
                        TokenExtractorScreen(
                            onTokenExtracted = { tok ->
                                viewModel.setEnterpriseToken(tok)
                            },
                            onNavigateToWizard = { viewModel.selectTab(AppTab.WIZARD) }
                        )
                    }
                    AppTab.POLICY -> {
                        PolicyCustomizerScreen(
                            policy = policy,
                            onPolicyChange = { viewModel.updatePolicy(it) }
                        )
                    }
                    AppTab.QR -> {
                        QrCodeCardScreen(
                            enrollmentToken = enrollmentToken
                        )
                    }
                    AppTab.SCRIPTS -> {
                        ScriptCenterScreen(
                            projectId = projectId,
                            enterpriseToken = enterpriseToken,
                            policy = policy
                        )
                    }
                    AppTab.GUIDE -> {
                        PhoneGuideScreen()
                    }
                    AppTab.TROUBLESHOOT -> {
                        TroubleshootingScreen()
                    }
                    AppTab.SAVED -> {
                        SavedTokensScreen(
                            savedTokens = savedList,
                            onSelectTokenForQr = { tok ->
                                viewModel.setEnrollmentToken(tok)
                                viewModel.selectTab(AppTab.QR)
                            },
                            onDeleteToken = { id -> viewModel.deleteSavedEnrollment(id) },
                            onNavigateToWizard = { viewModel.selectTab(AppTab.WIZARD) }
                        )
                    }
                }
            }
        }
    }
}
