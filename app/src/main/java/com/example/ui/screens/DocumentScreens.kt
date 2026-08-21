package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocTone
import com.example.data.DocType
import com.example.data.RetentionChoice
import com.example.data.TimelineEvent
import com.example.ui.components.DisclaimerFooter
import com.example.ui.components.StickyBottomBar
import com.example.ui.theme.*

// ==========================================
// SCREEN 9 — DOCUMENT TYPE SELECTION
// ==========================================
@Composable
fun DocumentTypeScreen(
  selectedType: DocType,
  onSelectType: (DocType) -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  val docOptions = DocType.values()

  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "What would you like to prepare?",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Select the format that best fits the current status of your communication with the client.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(24.dp))

      docOptions.forEach { option ->
        val isSelected = option == selectedType

        Surface(
          onClick = { onSelectType(option) },
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) Blue50 else PureWhite,
          border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Blue600 else Neutral200
          ),
          shadowElevation = if (isSelected) 2.dp else 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("doc_type_${option.name}")
        ) {
          Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(18.dp)
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Blue600 else Blue100)
            ) {
              Icon(
                imageVector = when (option) {
                  DocType.DEMAND_LETTER -> Icons.Default.Gavel
                  DocType.PAYMENT_REMINDER -> Icons.Default.MailOutline
                  DocType.EVIDENCE_SUMMARY -> Icons.Default.FolderShared
                },
                contentDescription = null,
                tint = if (isSelected) PureWhite else Blue700,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = option.title,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Blue900 else Neutral900
                )

                Surface(
                  color = if (isSelected) Blue100 else Neutral100,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = option.badge,
                    fontSize = 11.sp,
                    color = if (isSelected) Blue800 else Neutral600,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = option.description,
                fontSize = 13.sp,
                color = Neutral600,
                lineHeight = 18.sp
              )
            }
          }
        }
      }
    }

    StickyBottomBar(
      primaryText = "Continue to Document Preview",
      onPrimaryClick = onContinue,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}

// ==========================================
// SCREEN 10 — DOCUMENT PREVIEW
// ==========================================
@Composable
fun DocumentPreviewScreen(
  docType: DocType,
  docTone: DocTone,
  claimantName: String,
  clientName: String,
  amount: String,
  service: String,
  dueDate: String,
  noticeDays: Int,
  timeline: List<TimelineEvent>,
  documentText: String,
  onSelectTone: (DocTone) -> Unit,
  onUpdateClaimantName: (String) -> Unit,
  onUpdateNoticeDays: (Int) -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showEditDetailsDialog by remember { mutableStateOf(false) }
  var editedClaimant by remember { mutableStateOf(claimantName) }
  var editedDays by remember { mutableStateOf(noticeDays.toString()) }
  var zoomLevel by remember { mutableFloatStateOf(1.0f) }

  val docHorizontalScroll = rememberScrollState()
  val docVerticalScroll = rememberScrollState()

  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "Review your document",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Generated from your verified facts and timeline. Use the zoom controls below to inspect every detail on mobile.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Tone Selection Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "DOCUMENT TONE:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Neutral500,
          letterSpacing = 0.8.sp
        )

        TextButton(
          onClick = { showEditDetailsDialog = true },
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Edit Claimant / Notice", fontSize = 12.sp, color = Blue600)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        DocTone.values().forEach { tone ->
          val isSelected = tone == docTone
          FilterChip(
            selected = isSelected,
            onClick = { onSelectTone(tone) },
            label = {
              Text(
                text = tone.name.lowercase().replaceFirstChar { it.uppercase() },
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Blue100,
              selectedLabelColor = Blue800,
              containerColor = PureWhite,
              labelColor = Neutral700
            ),
            modifier = Modifier.weight(1f)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Zoom & Scale Controls Bar
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ZoomIn,
                contentDescription = null,
                tint = Blue600,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "DOCUMENT ZOOM:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Neutral700,
                letterSpacing = 0.5.sp
              )
            }

            // Stepper controls (+ / - / Reset)
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              // Zoom Out Button
              IconButton(
                onClick = {
                  if (zoomLevel > 0.75f) zoomLevel = (zoomLevel - 0.2f).coerceAtLeast(0.75f)
                },
                enabled = zoomLevel > 0.75f,
                modifier = Modifier
                  .size(32.dp)
                  .testTag("btn_zoom_out")
              ) {
                Icon(
                  imageVector = Icons.Default.Remove,
                  contentDescription = "Zoom Out",
                  tint = if (zoomLevel > 0.75f) Neutral700 else Neutral300,
                  modifier = Modifier.size(16.dp)
                )
              }

              // Percentage Badge
              Surface(
                color = Blue50,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Blue200),
                modifier = Modifier.testTag("zoom_level_indicator")
              ) {
                Text(
                  text = "${(zoomLevel * 100).toInt()}%",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Blue700,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }

              // Zoom In Button
              IconButton(
                onClick = {
                  if (zoomLevel < 2.0f) zoomLevel = (zoomLevel + 0.2f).coerceAtMost(2.0f)
                },
                enabled = zoomLevel < 2.0f,
                modifier = Modifier
                  .size(32.dp)
                  .testTag("btn_zoom_in")
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Zoom In",
                  tint = if (zoomLevel < 2.0f) Neutral700 else Neutral300,
                  modifier = Modifier.size(16.dp)
                )
              }

              // Reset to 100%
              if (zoomLevel != 1.0f) {
                TextButton(
                  onClick = { zoomLevel = 1.0f },
                  contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                  modifier = Modifier.height(30.dp).testTag("btn_zoom_reset")
                ) {
                  Text("Reset", fontSize = 11.sp, color = Blue600, fontWeight = FontWeight.SemiBold)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Quick Zoom Presets Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(0.85f to "85%", 1.0f to "100%", 1.25f to "125%", 1.5f to "150%", 1.75f to "175%").forEach { (scale, label) ->
              val isCurrent = (zoomLevel * 100).toInt() == (scale * 100).toInt()
              Surface(
                onClick = { zoomLevel = scale },
                color = if (isCurrent) Blue600 else Neutral100,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                  .weight(1f)
                  .height(28.dp)
              ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                  Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) PureWhite else Neutral700
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Scrollable & Zoomable Document Viewport
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, Neutral300),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 380.dp, max = 560.dp)
            .background(Neutral100)
            .horizontalScroll(docHorizontalScroll)
            .verticalScroll(docVerticalScroll)
            .padding(12.dp)
        ) {
          // Document Paper with Zoom Transform applied
          Box(
            modifier = Modifier
              .width((320.dp * zoomLevel).coerceAtLeast(300.dp))
              .graphicsLayer {
                scaleX = zoomLevel
                scaleY = zoomLevel
                transformOrigin = TransformOrigin(0f, 0f)
              }
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = PureWhite,
              border = BorderStroke(1.dp, Neutral300),
              shadowElevation = 2.dp,
              modifier = Modifier.width(320.dp)
            ) {
              Column(modifier = Modifier.padding(18.dp)) {
                // Document Header
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.Top
                ) {
                  Column {
                    Text(
                      text = when (docType) {
                        DocType.DEMAND_LETTER -> "DEMAND FOR PAYMENT"
                        DocType.PAYMENT_REMINDER -> "URGENT PAYMENT REMINDER"
                        DocType.EVIDENCE_SUMMARY -> "EVIDENCE SUMMARY"
                      },
                      fontSize = 15.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = Blue900,
                      letterSpacing = 0.5.sp
                    )
                    Text(
                      text = "Issued: August 21, 2026",
                      fontSize = 11.sp,
                      color = Neutral500
                    )
                  }

                  Surface(
                    color = Teal50,
                    shape = RoundedCornerShape(4.dp)
                  ) {
                    Text(
                      text = "Formal Draft",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = Teal800,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp), color = Neutral200)

                // Key Parties Metadata Box
                Surface(
                  color = Neutral50,
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                      Column(modifier = Modifier.weight(1f)) {
                        Text("Claimant (You):", fontSize = 10.sp, color = Neutral500)
                        Text(claimantName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Neutral900)
                      }
                      Column(modifier = Modifier.weight(1f)) {
                        Text("Respondent (Client):", fontSize = 10.sp, color = Neutral500)
                        Text(clientName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Neutral900)
                      }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                      Column(modifier = Modifier.weight(1f)) {
                        Text("Service Rendered:", fontSize = 10.sp, color = Neutral500)
                        Text(service, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Neutral800)
                      }
                      Column(modifier = Modifier.weight(1f)) {
                        Text("Total Amount Due:", fontSize = 10.sp, color = Neutral500)
                        Text(amount, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Green700)
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                  text = "Statement of Demand & Facts:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Neutral800
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "This notice constitutes formal demand for overdue compensation. Deliverables were completed as specified. Payment of $amount due on $dueDate has not been remitted.",
                  fontSize = 11.sp,
                  color = Neutral700,
                  lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                  text = "Chronological Evidence Timeline:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Neutral800
                )

                Spacer(modifier = Modifier.height(4.dp))

                timeline.forEach { event ->
                  Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text("• ", color = Blue600, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text(
                      text = "${event.date} — ${event.title}",
                      fontSize = 11.sp,
                      color = Neutral700
                    )
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                  text = "Terms of Notice:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Neutral800
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "Full payment of $amount is requested within $noticeDays calendar days. If settlement is not established, formal recovery procedures will be initiated.",
                  fontSize = 11.sp,
                  color = Neutral700,
                  lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = Neutral200)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Respectfully submitted,", fontSize = 10.sp, color = Neutral600)
                Spacer(modifier = Modifier.height(2.dp))
                Text(claimantName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Neutral900)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Mandatory Guidance Notice
      Surface(
        color = Blue50,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Blue100),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Blue600,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Please review the information carefully before using this document.",
            fontSize = 12.sp,
            color = Blue800,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      DisclaimerFooter()
    }

    StickyBottomBar(
      primaryText = "Continue to Data Retention",
      onPrimaryClick = onContinue,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }

  if (showEditDetailsDialog) {
    AlertDialog(
      onDismissRequest = { showEditDetailsDialog = false },
      title = { Text("Edit Document Details", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          OutlinedTextField(
            value = editedClaimant,
            onValueChange = { editedClaimant = it },
            label = { Text("Claimant Name / Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = editedDays,
            onValueChange = { editedDays = it },
            label = { Text("Notice Duration (Days)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (editedClaimant.isNotBlank()) onUpdateClaimantName(editedClaimant)
            editedDays.toIntOrNull()?.let { onUpdateNoticeDays(it) }
            showEditDetailsDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditDetailsDialog = false }) {
          Text("Cancel", color = Neutral600)
        }
      }
    )
  }
}

// ==========================================
// SCREEN 11 — DATA RETENTION
// ==========================================
@Composable
fun RetentionScreen(
  selectedRetention: RetentionChoice,
  onSelectRetention: (RetentionChoice) -> Unit,
  onFinalize: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "How long should we keep your case?",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "ClaimPilot prioritizes your privacy. We never permanently store your sensitive communications.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(24.dp))

      RetentionChoice.values().forEach { option ->
        val isSelected = option == selectedRetention

        Surface(
          onClick = { onSelectRetention(option) },
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) Teal50 else PureWhite,
          border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Teal600 else Neutral200
          ),
          shadowElevation = if (isSelected) 2.dp else 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("retention_${option.name}")
        ) {
          Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(18.dp)
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Teal600 else Teal50)
            ) {
              Icon(
                imageVector = when (option) {
                  RetentionChoice.DELETE_IMMEDIATELY -> Icons.Default.DeleteSweep
                  RetentionChoice.KEEP_30_DAYS -> Icons.Default.Schedule
                },
                contentDescription = null,
                tint = if (isSelected) PureWhite else Teal700,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = option.title,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Teal800 else Neutral900
                )

                Surface(
                  color = if (isSelected) Teal100 else Neutral100,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = option.badge,
                    fontSize = 11.sp,
                    color = if (isSelected) Teal800 else Neutral600,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = option.description,
                fontSize = 13.sp,
                color = Neutral600,
                lineHeight = 18.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // No permanent storage reminder
      Surface(
        color = PureWhite,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Neutral200),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(14.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = Green600,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = "Strict Privacy Guarantee: No permanent database storage is ever maintained.",
            fontSize = 12.sp,
            color = Neutral700,
            lineHeight = 16.sp
          )
        }
      }
    }

    StickyBottomBar(
      primaryText = "Finalize & Ready Document",
      onPrimaryClick = onFinalize,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}

// ==========================================
// SCREEN 12 — DOCUMENT READY
// ==========================================
@Composable
fun DocumentReadyScreen(
  docType: DocType,
  clientName: String,
  amount: String,
  selectedRetention: RetentionChoice,
  documentText: String,
  onDownloadSimulated: () -> Unit,
  onBackToCase: () -> Unit,
  onStartNewClaim: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isDownloaded by remember { mutableStateOf(false) }
  var isCopied by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Neutral50)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(10.dp))

    // Celebration / Success Badge
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(Green100)
    ) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Success",
        tint = Green600,
        modifier = Modifier.size(48.dp)
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "Your document is ready.",
      fontSize = 26.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Neutral900,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Your formal demand package has been organized and formatted.",
      fontSize = 14.sp,
      color = Neutral600,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Document Card Ready State
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = PureWhite,
      border = BorderStroke(1.5.dp, Green500),
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Green50)
          ) {
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              tint = Green600,
              modifier = Modifier.size(26.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "${docType.title} — $clientName",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Neutral900
            )
            Text(
              text = "Total Claim: $amount • Format: PDF / Text",
              fontSize = 12.sp,
              color = Neutral500
            )
          }

          Surface(
            color = Green100,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "Ready",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Green700,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }

        Divider(modifier = Modifier.padding(vertical = 14.dp), color = Neutral200)

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = Teal600,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Selected retention: ${selectedRetention.title}",
            fontSize = 12.sp,
            color = Teal800,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Primary CTA: Download Document
    Button(
      onClick = {
        isDownloaded = true
        onDownloadSimulated()
      },
      colors = ButtonDefaults.buttonColors(
        containerColor = if (isDownloaded) Green600 else Blue600,
        contentColor = PureWhite
      ),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("btn_download_document")
    ) {
      Icon(
        imageVector = if (isDownloaded) Icons.Default.Check else Icons.Default.Download,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = if (isDownloaded) "Document Downloaded (PDF Ready)" else "Download Document",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Secondary CTA: Copy to Clipboard
    OutlinedButton(
      onClick = {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("ClaimPilot Demand Letter", documentText)
        clipboard.setPrimaryClip(clip)
        isCopied = true
      },
      shape = RoundedCornerShape(14.dp),
      border = BorderStroke(1.dp, Neutral300),
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("btn_copy_clipboard")
    ) {
      Icon(
        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
        contentDescription = null,
        tint = if (isCopied) Green600 else Neutral700,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = if (isCopied) "Copied Letter to Clipboard" else "Copy Letter Text",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = if (isCopied) Green700 else Neutral700
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Secondary CTA: Back to Case
    TextButton(
      onClick = onBackToCase,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "Back to Case Review",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Neutral600
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextButton(
      onClick = onStartNewClaim,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "Start Another Claim",
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = Blue600
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    DisclaimerFooter()
  }
}
