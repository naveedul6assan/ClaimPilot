package com.example.data

enum class ClaimScreen {
  LANDING,
  COUNTRY,
  DISPUTE_TYPE,
  STORY,
  PROOF_UPLOAD,
  ANALYSIS,
  REVIEW_FACTS,
  TIMELINE,
  DOCUMENT_TYPE,
  DOCUMENT_PREVIEW,
  RETENTION,
  DOCUMENT_READY
}

enum class StepCategory(val stepNumber: Int, val title: String) {
  DISPUTE(1, "Dispute"),
  PROOF(2, "Proof"),
  REVIEW(3, "Review"),
  TIMELINE(4, "Timeline"),
  DOCUMENT(5, "Document")
}

data class CountryOption(
  val id: String,
  val name: String,
  val flagEmoji: String,
  val currencySymbol: String,
  val region: String
)

data class DisputeTypeOption(
  val id: String,
  val title: String,
  val description: String,
  val isAvailable: Boolean = true
)

data class UploadedFile(
  val id: String,
  val fileName: String,
  val fileSize: String,
  val sourceLabel: String,
  val isReady: Boolean = true
)

enum class FactStatus {
  VERIFIED,
  NEEDS_REVIEW,
  UNCERTAIN
}

data class ExtractedFact(
  val id: String,
  val label: String,
  val value: String,
  val status: FactStatus,
  val category: String = "Claim Details"
)

data class TimelineEvent(
  val id: String,
  val date: String,
  val title: String,
  val status: FactStatus,
  val description: String = ""
)

enum class DocType(val title: String, val description: String, val badge: String) {
  DEMAND_LETTER(
    title = "Demand Letter",
    description = "Professional formal request for payment with itemized timeline and clear deadline.",
    badge = "Recommended"
  ),
  PAYMENT_REMINDER(
    title = "Payment Reminder",
    description = "A polite but firm follow-up requesting immediate settlement of the overdue invoice.",
    badge = "Early Stage"
  ),
  EVIDENCE_SUMMARY(
    title = "Evidence Summary",
    description = "A structured chronological summary of all proof, conversations, and milestones provided.",
    badge = "Record Keeping"
  )
}

enum class DocTone(val label: String, val description: String) {
  FORMAL("Formal & Professional", "Standard business tone suitable for formal notices"),
  DIRECT("Direct & Firm", "Concise and strictly focused on overdue payment terms"),
  COURTEOUS("Courteous & Respectful", "Diplomatic reminder for ongoing client relationships")
}

enum class RetentionChoice(val title: String, val description: String, val badge: String) {
  DELETE_IMMEDIATELY(
    title = "Delete immediately",
    description = "Your case data will be wiped from this session after your document is prepared/downloaded.",
    badge = "Maximum Privacy"
  ),
  KEEP_30_DAYS(
    title = "Keep for 30 days",
    description = "Your case will automatically be deleted after 30 days so you can return to edit or re-download.",
    badge = "Convenient Follow-Up"
  )
}

data class ClaimState(
  val currentScreen: ClaimScreen = ClaimScreen.LANDING,
  val navigationHistory: List<ClaimScreen> = listOf(ClaimScreen.LANDING),
  
  // Screen 2: Country
  val selectedCountry: CountryOption = CountryOption(
    id = "US",
    name = "United States",
    flagEmoji = "🇺🇸",
    currencySymbol = "$",
    region = "North America"
  ),
  
  // Screen 3: Dispute Type
  val selectedDisputeType: String = "unpaid_invoice",
  
  // Screen 4: Story
  val userRole: String = "Freelancer",
  val storyText: String = "My client Jack agreed to pay me $500 for a website design project, but I completed the work and delivered all deliverables and haven't received payment despite reminders.",
  
  // Screen 5: Proof Files
  val uploadedFiles: List<UploadedFile> = listOf(
    UploadedFile("1", "whatsapp-export.txt", "240 KB", "WhatsApp"),
    UploadedFile("2", "invoice.pdf", "1.2 MB", "PDF"),
    UploadedFile("3", "screenshot-01.png", "850 KB", "Screenshots")
  ),
  
  // Screen 6: Analysis
  val isAnalyzing: Boolean = false,
  val analysisStep: Int = 0,
  
  // Screen 7: Extracted Facts
  val facts: List<ExtractedFact> = listOf(
    ExtractedFact("client", "Client", "Jack", FactStatus.VERIFIED),
    ExtractedFact("amount", "Claim amount", "$500", FactStatus.VERIFIED),
    ExtractedFact("service", "Service", "Website design", FactStatus.VERIFIED),
    ExtractedFact("status", "Payment status", "Unpaid", FactStatus.VERIFIED),
    ExtractedFact("due_date", "Payment due", "March 20, 2026", FactStatus.NEEDS_REVIEW)
  ),
  
  // Screen 8: Timeline
  val timeline: List<TimelineEvent> = listOf(
    TimelineEvent("t1", "March 2, 2026", "Payment agreement reached", FactStatus.VERIFIED, "Agreed on \$500 for website project"),
    TimelineEvent("t2", "March 5, 2026", "Work started", FactStatus.VERIFIED, "Initial design draft and layout begun"),
    TimelineEvent("t3", "March 15, 2026", "Work completed", FactStatus.VERIFIED, "Final website assets delivered to client"),
    TimelineEvent("t4", "March 20, 2026", "Payment was due", FactStatus.NEEDS_REVIEW, "Invoice payment terms expired"),
    TimelineEvent("t5", "March 25, 2026", "Payment reminder sent", FactStatus.VERIFIED, "Friendly reminder sent via WhatsApp")
  ),
  
  // Screen 9: Document Type
  val selectedDocType: DocType = DocType.DEMAND_LETTER,
  val selectedDocTone: DocTone = DocTone.FORMAL,
  
  // Screen 10: Document Customization
  val claimantName: String = "Freelancer (You)",
  val customNoticeDays: Int = 10,
  val customLetterBody: String = "",
  
  // Screen 11: Data Retention
  val selectedRetention: RetentionChoice = RetentionChoice.DELETE_IMMEDIATELY,
  
  // Feedback
  val toastMessage: String? = null
)
