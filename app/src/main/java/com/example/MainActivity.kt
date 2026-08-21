package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ClaimScreen
import com.example.ui.components.ClaimPilotTopBar
import com.example.ui.screens.*
import com.example.ui.theme.ClaimPilotTheme
import com.example.viewmodel.ClaimViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ClaimPilotTheme {
        ClaimPilotApp()
      }
    }
  }
}

@Composable
fun ClaimPilotApp(
  claimViewModel: ClaimViewModel = viewModel()
) {
  val state by claimViewModel.state.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }

  // Handle system back button
  BackHandler(enabled = state.currentScreen != ClaimScreen.LANDING) {
    claimViewModel.navigateBack()
  }

  // Observe toast messages
  LaunchedEffect(state.toastMessage) {
    state.toastMessage?.let { msg ->
      snackbarHostState.showSnackbar(
        message = msg,
        duration = SnackbarDuration.Short
      )
      claimViewModel.clearToast()
    }
  }

  val currentCategory = claimViewModel.getStepCategoryForScreen(state.currentScreen)
  val canGoBack = state.currentScreen != ClaimScreen.LANDING

  Scaffold(
    topBar = {
      ClaimPilotTopBar(
        canNavigateBack = canGoBack,
        onNavigateBack = { claimViewModel.navigateBack() },
        onResetDemo = { claimViewModel.resetToInitialDemo() },
        currentCategory = currentCategory
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    contentWindowInsets = WindowInsets.safeDrawing,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (state.currentScreen) {
        ClaimScreen.LANDING -> {
          LandingScreen(
            onStartClaim = { claimViewModel.navigateTo(ClaimScreen.COUNTRY) }
          )
        }

        ClaimScreen.COUNTRY -> {
          CountryScreen(
            countries = claimViewModel.availableCountries,
            selectedCountry = state.selectedCountry,
            onSelectCountry = { claimViewModel.selectCountry(it) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.DISPUTE_TYPE) }
          )
        }

        ClaimScreen.DISPUTE_TYPE -> {
          DisputeTypeScreen(
            disputeTypes = claimViewModel.disputeTypes,
            selectedType = state.selectedDisputeType,
            onSelectType = { claimViewModel.selectDisputeType(it) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.STORY) }
          )
        }

        ClaimScreen.STORY -> {
          StoryScreen(
            storyText = state.storyText,
            onStoryChange = { claimViewModel.updateStoryText(it) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.PROOF_UPLOAD) }
          )
        }

        ClaimScreen.PROOF_UPLOAD -> {
          ProofUploadScreen(
            proofSources = claimViewModel.proofSources,
            uploadedFiles = state.uploadedFiles,
            onAddFile = { name, source -> claimViewModel.addUploadedFile(name, source) },
            onRemoveFile = { claimViewModel.removeUploadedFile(it) },
            onContinueToAnalysis = { claimViewModel.startAiAnalysis() }
          )
        }

        ClaimScreen.ANALYSIS -> {
          AnalysisScreen(
            currentStep = state.analysisStep
          )
        }

        ClaimScreen.REVIEW_FACTS -> {
          ReviewFactsScreen(
            facts = state.facts,
            onApproveFact = { claimViewModel.approveFact(it) },
            onUpdateFact = { id, value, status -> claimViewModel.updateFact(id, value, status) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.TIMELINE) }
          )
        }

        ClaimScreen.TIMELINE -> {
          TimelineScreen(
            timeline = state.timeline,
            onAddEvent = { date, title, status -> claimViewModel.addTimelineEvent(date, title, status) },
            onUpdateEvent = { id, date, title, status -> claimViewModel.updateTimelineEvent(id, date, title, status) },
            onDeleteEvent = { claimViewModel.deleteTimelineEvent(it) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.DOCUMENT_TYPE) }
          )
        }

        ClaimScreen.DOCUMENT_TYPE -> {
          DocumentTypeScreen(
            selectedType = state.selectedDocType,
            onSelectType = { claimViewModel.selectDocumentType(it) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.DOCUMENT_PREVIEW) }
          )
        }

        ClaimScreen.DOCUMENT_PREVIEW -> {
          val clientName = state.facts.find { it.id == "client" }?.value ?: "Jack"
          val amount = state.facts.find { it.id == "amount" }?.value ?: "$500"
          val service = state.facts.find { it.id == "service" }?.value ?: "Website design"
          val dueDate = state.facts.find { it.id == "due_date" }?.value ?: "March 20, 2026"

          DocumentPreviewScreen(
            docType = state.selectedDocType,
            docTone = state.selectedDocTone,
            claimantName = state.claimantName,
            clientName = clientName,
            amount = amount,
            service = service,
            dueDate = dueDate,
            noticeDays = state.customNoticeDays,
            timeline = state.timeline,
            documentText = claimViewModel.getGeneratedDocumentText(),
            onSelectTone = { claimViewModel.selectDocumentTone(it) },
            onUpdateClaimantName = { claimViewModel.updateClaimantName(it) },
            onUpdateNoticeDays = { claimViewModel.updateNoticeDays(it) },
            onContinue = { claimViewModel.navigateTo(ClaimScreen.RETENTION) }
          )
        }

        ClaimScreen.RETENTION -> {
          RetentionScreen(
            selectedRetention = state.selectedRetention,
            onSelectRetention = { claimViewModel.selectRetention(it) },
            onFinalize = { claimViewModel.navigateTo(ClaimScreen.DOCUMENT_READY) }
          )
        }

        ClaimScreen.DOCUMENT_READY -> {
          val clientName = state.facts.find { it.id == "client" }?.value ?: "Jack"
          val amount = state.facts.find { it.id == "amount" }?.value ?: "$500"

          DocumentReadyScreen(
            docType = state.selectedDocType,
            clientName = clientName,
            amount = amount,
            selectedRetention = state.selectedRetention,
            documentText = claimViewModel.getGeneratedDocumentText(),
            onDownloadSimulated = {
              claimViewModel.showToast("Demand Letter downloaded as PDF")
            },
            onBackToCase = {
              claimViewModel.navigateTo(ClaimScreen.DOCUMENT_PREVIEW)
            },
            onStartNewClaim = {
              claimViewModel.resetToInitialDemo()
            }
          )
        }
      }
    }
  }
}
