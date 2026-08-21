package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClaimViewModel : ViewModel() {

  private val _state = MutableStateFlow(ClaimState())
  val state: StateFlow<ClaimState> = _state.asStateFlow()

  val availableCountries = listOf(
    CountryOption("US", "United States", "🇺🇸", "$", "North America"),
    CountryOption("CA", "Canada", "🇨🇦", "CA$", "North America"),
    CountryOption("GB", "United Kingdom", "🇬🇧", "£", "Europe"),
    CountryOption("AU", "Australia", "🇦🇺", "AU$", "Oceania"),
    CountryOption("NZ", "New Zealand", "🇳🇿", "NZ$", "Oceania")
  )

  val disputeTypes = listOf(
    DisputeTypeOption(
      id = "unpaid_invoice",
      title = "Unpaid Invoice",
      description = "A client or customer owes you money for work, products or services.",
      isAvailable = true
    ),
    DisputeTypeOption(
      id = "refund_dispute",
      title = "Refund dispute",
      description = "Dispute regarding an unfulfilled purchase or promised refund.",
      isAvailable = false
    ),
    DisputeTypeOption(
      id = "security_deposit",
      title = "Security deposit",
      description = "Disputed residential or commercial rental deposit withholdings.",
      isAvailable = false
    ),
    DisputeTypeOption(
      id = "contract_dispute",
      title = "Contract dispute",
      description = "Breach of general service or delivery terms in signed agreement.",
      isAvailable = false
    ),
    DisputeTypeOption(
      id = "service_dispute",
      title = "Service dispute",
      description = "Unresolved performance or satisfaction dispute with a vendor.",
      isAvailable = false
    )
  )

  val proofSources = listOf(
    "WhatsApp", "Email", "Upwork", "Fiverr",
    "Screenshots", "PDF", "Payment records", "Other"
  )

  fun navigateTo(screen: ClaimScreen) {
    _state.update { current ->
      val updatedHistory = if (current.currentScreen == screen) {
        current.navigationHistory
      } else {
        current.navigationHistory + screen
      }
      current.copy(
        currentScreen = screen,
        navigationHistory = updatedHistory
      )
    }
  }

  fun navigateBack(): Boolean {
    val current = _state.value
    if (current.navigationHistory.size <= 1) {
      if (current.currentScreen != ClaimScreen.LANDING) {
        _state.update { it.copy(currentScreen = ClaimScreen.LANDING) }
        return true
      }
      return false
    }

    val newHistory = current.navigationHistory.dropLast(1)
    val prevScreen = newHistory.last()
    _state.update {
      it.copy(
        currentScreen = prevScreen,
        navigationHistory = newHistory
      )
    }
    return true
  }

  fun selectCountry(country: CountryOption) {
    _state.update { it.copy(selectedCountry = country) }
  }

  fun selectDisputeType(typeId: String) {
    _state.update { it.copy(selectedDisputeType = typeId) }
  }

  fun updateStoryText(text: String) {
    _state.update { it.copy(storyText = text) }
  }

  fun addUploadedFile(fileName: String, source: String, size: String = "420 KB") {
    val newFile = UploadedFile(
      id = System.currentTimeMillis().toString(),
      fileName = fileName,
      fileSize = size,
      sourceLabel = source,
      isReady = true
    )
    _state.update { it.copy(uploadedFiles = it.uploadedFiles + newFile) }
    showToast("Added $fileName")
  }

  fun removeUploadedFile(fileId: String) {
    _state.update { it.copy(uploadedFiles = it.uploadedFiles.filterNot { f -> f.id == fileId }) }
    showToast("File removed")
  }

  fun startAiAnalysis() {
    navigateTo(ClaimScreen.ANALYSIS)
    _state.update { it.copy(isAnalyzing = true, analysisStep = 0) }

    viewModelScope.launch {
      delay(750)
      _state.update { it.copy(analysisStep = 1) } // Reading your proof...
      delay(850)
      _state.update { it.copy(analysisStep = 2) } // Finding important dates...
      delay(850)
      _state.update { it.copy(analysisStep = 3) } // Identifying payment information...
      delay(850)
      _state.update { it.copy(analysisStep = 4) } // Organizing what happened...
      delay(700)
      _state.update { it.copy(isAnalyzing = false) }
      navigateTo(ClaimScreen.REVIEW_FACTS)
    }
  }

  fun updateFact(id: String, newValue: String, newStatus: FactStatus) {
    _state.update { state ->
      val updated = state.facts.map { fact ->
        if (fact.id == id) fact.copy(value = newValue, status = newStatus) else fact
      }
      state.copy(facts = updated)
    }
    showToast("Fact updated")
  }

  fun approveFact(id: String) {
    _state.update { state ->
      val updated = state.facts.map { fact ->
        if (fact.id == id) fact.copy(status = FactStatus.VERIFIED) else fact
      }
      state.copy(facts = updated)
    }
    showToast("Fact verified")
  }

  fun addTimelineEvent(date: String, title: String, status: FactStatus = FactStatus.VERIFIED) {
    val newEvent = TimelineEvent(
      id = "t_${System.currentTimeMillis()}",
      date = date,
      title = title,
      status = status
    )
    _state.update { it.copy(timeline = it.timeline + newEvent) }
    showToast("Event added to timeline")
  }

  fun updateTimelineEvent(id: String, newDate: String, newTitle: String, newStatus: FactStatus) {
    _state.update { state ->
      val updated = state.timeline.map { event ->
        if (event.id == id) event.copy(date = newDate, title = newTitle, status = newStatus) else event
      }
      state.copy(timeline = updated)
    }
    showToast("Timeline event updated")
  }

  fun deleteTimelineEvent(id: String) {
    _state.update { state ->
      state.copy(timeline = state.timeline.filterNot { it.id == id })
    }
    showToast("Event removed from timeline")
  }

  fun selectDocumentType(type: DocType) {
    _state.update { it.copy(selectedDocType = type) }
  }

  fun selectDocumentTone(tone: DocTone) {
    _state.update { it.copy(selectedDocTone = tone) }
    showToast("Tone updated to ${tone.label}")
  }

  fun updateClaimantName(name: String) {
    _state.update { it.copy(claimantName = name) }
  }

  fun updateNoticeDays(days: Int) {
    _state.update { it.copy(customNoticeDays = days) }
  }

  fun updateCustomLetterBody(body: String) {
    _state.update { it.copy(customLetterBody = body) }
  }

  fun selectRetention(choice: RetentionChoice) {
    _state.update { it.copy(selectedRetention = choice) }
  }

  fun resetToInitialDemo() {
    _state.value = ClaimState()
    showToast("Reset to demo state")
  }

  fun showToast(message: String) {
    _state.update { it.copy(toastMessage = message) }
  }

  fun clearToast() {
    _state.update { it.copy(toastMessage = null) }
  }

  fun getStepCategoryForScreen(screen: ClaimScreen): StepCategory? {
    return when (screen) {
      ClaimScreen.LANDING -> null
      ClaimScreen.COUNTRY,
      ClaimScreen.DISPUTE_TYPE,
      ClaimScreen.STORY -> StepCategory.DISPUTE
      ClaimScreen.PROOF_UPLOAD,
      ClaimScreen.ANALYSIS -> StepCategory.PROOF
      ClaimScreen.REVIEW_FACTS -> StepCategory.REVIEW
      ClaimScreen.TIMELINE -> StepCategory.TIMELINE
      ClaimScreen.DOCUMENT_TYPE,
      ClaimScreen.DOCUMENT_PREVIEW,
      ClaimScreen.RETENTION,
      ClaimScreen.DOCUMENT_READY -> StepCategory.DOCUMENT
    }
  }

  fun getGeneratedDocumentText(): String {
    val s = _state.value
    val clientName = s.facts.find { it.id == "client" }?.value ?: "Client"
    val amount = s.facts.find { it.id == "amount" }?.value ?: "$500"
    val service = s.facts.find { it.id == "service" }?.value ?: "Website design"
    val dueDate = s.facts.find { it.id == "due_date" }?.value ?: "March 20, 2026"
    val claimant = s.claimantName

    val title = when (s.selectedDocType) {
      DocType.DEMAND_LETTER -> "FORMAL DEMAND FOR PAYMENT"
      DocType.PAYMENT_REMINDER -> "URGENT PAYMENT REMINDER"
      DocType.EVIDENCE_SUMMARY -> "SUMMARY OF EVIDENCE & CLAIM DETAILS"
    }

    val opening = when (s.selectedDocTone) {
      DocTone.FORMAL -> "This letter serves as a formal demand for payment regarding professional services rendered under our agreement."
      DocTone.DIRECT -> "This is an urgent notice regarding overdue balance for delivered work. Immediate payment is required."
      DocTone.COURTEOUS -> "I hope this notice finds you well. I am writing to respectfully follow up and request settlement for completed work."
    }

    val timelineItems = s.timeline.joinToString("\n") { "  • ${it.date}: ${it.title}" }

    return """
==================================================
$title
==================================================

Date: August 21, 2026
Jurisdiction: ${s.selectedCountry.name}

TO (RESPONDENT):
$clientName

FROM (CLAIMANT):
$claimant

RE: Outstanding Payment for $service
CLAIM AMOUNT DUE: $amount

--------------------------------------------------
STATEMENT OF FACTS:
--------------------------------------------------
$opening

As agreed, all deliverables for $service were duly completed and furnished. According to our agreement, payment of $amount was scheduled and due on $dueDate. To date, this amount remains unpaid.

CHRONOLOGICAL TIMELINE OF EVENTS:
$timelineItems

--------------------------------------------------
DEMAND FOR SETTLEMENT:
--------------------------------------------------
Please remit full payment of $amount within ${s.customNoticeDays} calendar days of receipt of this notice.

Payment may be made via our standard payment arrangement or bank transfer.

If you believe any information outlined above is incorrect or wish to discuss an immediate payment arrangement, please respond immediately.

Sincerely,

$claimant
Contact: Via Claim Record
==================================================
DISCLAIMER: ClaimPilot provides AI-assisted document preparation and organization. It is not a law firm and does not provide legal advice or legal representation.
""".trimIndent()
  }
}
