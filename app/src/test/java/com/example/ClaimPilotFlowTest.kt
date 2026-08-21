package com.example

import com.example.data.*
import com.example.viewmodel.ClaimViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ClaimPilotFlowTest {

  private lateinit var viewModel: ClaimViewModel

  @Before
  fun setup() {
    viewModel = ClaimViewModel()
  }

  @Test
  fun testInitialState() {
    val state = viewModel.state.value
    assertEquals(ClaimScreen.LANDING, state.currentScreen)
    assertEquals("US", state.selectedCountry.id)
    assertEquals("unpaid_invoice", state.selectedDisputeType)
    assertEquals(3, state.uploadedFiles.size)
    assertEquals(5, state.facts.size)
    assertEquals(5, state.timeline.size)
    assertEquals(DocType.DEMAND_LETTER, state.selectedDocType)
    assertEquals(RetentionChoice.DELETE_IMMEDIATELY, state.selectedRetention)
  }

  @Test
  fun testNavigationFlow() {
    viewModel.navigateTo(ClaimScreen.COUNTRY)
    assertEquals(ClaimScreen.COUNTRY, viewModel.state.value.currentScreen)

    viewModel.navigateTo(ClaimScreen.DISPUTE_TYPE)
    assertEquals(ClaimScreen.DISPUTE_TYPE, viewModel.state.value.currentScreen)

    val didPop = viewModel.navigateBack()
    assertTrue(didPop)
    assertEquals(ClaimScreen.COUNTRY, viewModel.state.value.currentScreen)
  }

  @Test
  fun testUpdateFactAndTimeline() {
    viewModel.updateFact("amount", "$750", FactStatus.VERIFIED)
    val updatedFact = viewModel.state.value.facts.find { it.id == "amount" }
    assertEquals("$750", updatedFact?.value)
    assertEquals(FactStatus.VERIFIED, updatedFact?.status)

    viewModel.addTimelineEvent("March 28, 2026", "Final demand letter delivered")
    assertEquals(6, viewModel.state.value.timeline.size)
  }

  @Test
  fun testDocumentGeneration() {
    val docText = viewModel.getGeneratedDocumentText()
    assertTrue(docText.contains("FORMAL DEMAND FOR PAYMENT"))
    assertTrue(docText.contains("Jack"))
    assertTrue(docText.contains("500"))
    assertTrue(docText.contains("ClaimPilot provides AI-assisted document preparation"))
  }
}
