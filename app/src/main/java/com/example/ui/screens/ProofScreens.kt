package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UploadedFile
import com.example.ui.components.StickyBottomBar
import com.example.ui.theme.*

// ==========================================
// SCREEN 5 — ADD YOUR PROOF
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProofUploadScreen(
  proofSources: List<String>,
  uploadedFiles: List<UploadedFile>,
  onAddFile: (String, String) -> Unit,
  onRemoveFile: (String) -> Unit,
  onContinueToAnalysis: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedSource by remember { mutableStateOf("WhatsApp") }
  var showAddFileDialog by remember { mutableStateOf(false) }
  var customFileName by remember { mutableStateOf("") }

  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "Where is your proof?",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "You don't need to rewrite everything. Upload what you already have.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Source Labels Section
      Text(
        text = "COMMON EVIDENCE SOURCES:",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral500,
        letterSpacing = 0.8.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        proofSources.forEach { source ->
          val isSelected = source == selectedSource
          FilterChip(
            selected = isSelected,
            onClick = { selectedSource = source },
            label = {
              Text(
                text = source,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Blue100,
              selectedLabelColor = Blue800,
              containerColor = PureWhite,
              labelColor = Neutral700
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = if (isSelected) Blue600 else Neutral300,
              borderWidth = if (isSelected) 1.5.dp else 1.dp
            ),
            shape = RoundedCornerShape(10.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Upload Drop Area
      Surface(
        onClick = { showAddFileDialog = true },
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        border = BorderStroke(1.5.dp, Blue500),
        shadowElevation = 1.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("upload_dropzone")
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Blue50)
          ) {
            Icon(
              imageVector = Icons.Default.CloudUpload,
              contentDescription = "Upload",
              tint = Blue600,
              modifier = Modifier.size(28.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Upload your proof",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral900
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "TXT, PDF, JPG, PNG",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue600
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Text files and exported conversations are usually easiest to process.",
            fontSize = 12.sp,
            color = Neutral500,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          FilledTonalButton(
            onClick = { showAddFileDialog = true },
            colors = ButtonDefaults.filledTonalButtonColors(
              containerColor = Blue50,
              contentColor = Blue700
            ),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Files (Simulate Upload)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Uploaded Files List
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ATTACHED EVIDENCE (${uploadedFiles.size})",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Neutral500,
          letterSpacing = 0.8.sp
        )

        if (uploadedFiles.isEmpty()) {
          TextButton(
            onClick = {
              onAddFile("whatsapp-export.txt", "WhatsApp")
              onAddFile("invoice.pdf", "PDF")
              onAddFile("screenshot-01.png", "Screenshots")
            }
          ) {
            Text("Load Demo Files", fontSize = 12.sp, color = Blue600)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (uploadedFiles.isEmpty()) {
        Surface(
          color = Neutral100,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "No files attached yet. Tap the upload area above or load demo files.",
            fontSize = 13.sp,
            color = Neutral500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
          )
        }
      } else {
        uploadedFiles.forEach { file ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = PureWhite,
            border = BorderStroke(1.dp, Neutral200),
            shadowElevation = 1.dp,
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(38.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (file.fileName.endsWith(".txt")) Teal50 else Blue50)
              ) {
                Icon(
                  imageVector = if (file.fileName.endsWith(".txt")) Icons.Default.Chat else Icons.Default.Description,
                  contentDescription = null,
                  tint = if (file.fileName.endsWith(".txt")) Teal700 else Blue700,
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = file.fileName,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Neutral900
                )
                Text(
                  text = "${file.sourceLabel} • ${file.fileSize}",
                  fontSize = 12.sp,
                  color = Neutral500
                )
              }

              Surface(
                color = Green50,
                shape = RoundedCornerShape(8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Green700,
                    modifier = Modifier.size(13.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Ready",
                    fontSize = 11.sp,
                    color = Green700,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              IconButton(
                onClick = { onRemoveFile(file.id) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.DeleteOutline,
                  contentDescription = "Remove file",
                  tint = Neutral400,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }

    StickyBottomBar(
      primaryText = "Analyze Proof with AI",
      onPrimaryClick = onContinueToAnalysis,
      isPrimaryEnabled = uploadedFiles.isNotEmpty(),
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }

  if (showAddFileDialog) {
    AlertDialog(
      onDismissRequest = { showAddFileDialog = false },
      title = { Text("Simulate File Upload", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text("Choose a sample file type or enter a file name:", fontSize = 13.sp, color = Neutral600)
          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = customFileName,
            onValueChange = { customFileName = it },
            label = { Text("File Name") },
            placeholder = { Text("e.g. client_agreement.pdf") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text("Quick Preset Files:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Neutral700)
          Spacer(modifier = Modifier.height(6.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
              onClick = { customFileName = "client-agreement-signed.pdf" },
              label = { Text("Agreement.pdf") }
            )
            AssistChip(
              onClick = { customFileName = "bank-transfer-attempt.png" },
              label = { Text("Receipt.png") }
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val name = if (customFileName.isNotBlank()) customFileName else "uploaded_evidence_${System.currentTimeMillis() % 1000}.pdf"
            onAddFile(name, selectedSource)
            customFileName = ""
            showAddFileDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
          Text("Upload File")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddFileDialog = false }) {
          Text("Cancel", color = Neutral600)
        }
      }
    )
  }
}

// ==========================================
// SCREEN 6 — AI ANALYSIS MOCK
// ==========================================
@Composable
fun AnalysisScreen(
  currentStep: Int,
  modifier: Modifier = Modifier
) {
  val steps = listOf(
    "Reading your proof...",
    "Finding important dates...",
    "Identifying payment information...",
    "Organizing what happened..."
  )

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Neutral50)
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Glowing Analysis Orb
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(100.dp)
        .clip(CircleShape)
        .background(Blue50)
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(76.dp)
          .clip(CircleShape)
          .background(Blue100.copy(alpha = pulseAlpha))
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = Blue600,
          modifier = Modifier.size(38.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    Text(
      text = "ClaimPilot is analyzing your proof",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = Neutral900,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Extracting key facts, dates, and amounts without permanent data retention.",
      fontSize = 13.sp,
      color = Neutral500,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(horizontal = 20.dp)
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Analysis Step Checklist Card
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = PureWhite,
      border = BorderStroke(1.dp, Neutral200),
      shadowElevation = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        steps.forEachIndexed { index, stepTitle ->
          val isDone = currentStep > index
          val isCurrent = currentStep == index

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier.size(24.dp)
            ) {
              if (isDone) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Green600)
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = PureWhite,
                    modifier = Modifier.size(14.dp)
                  )
                }
              } else if (isCurrent) {
                CircularProgressIndicator(
                  strokeWidth = 2.5.dp,
                  color = Blue600,
                  modifier = Modifier.size(20.dp)
                )
              } else {
                Box(
                  modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Neutral300)
                )
              }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
              text = stepTitle,
              fontSize = 14.sp,
              fontWeight = if (isCurrent || isDone) FontWeight.SemiBold else FontWeight.Normal,
              color = when {
                isDone -> Neutral900
                isCurrent -> Blue700
                else -> Neutral400
              }
            )
          }
        }
      }
    }
  }
}
