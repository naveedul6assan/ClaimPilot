package com.example.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExtractedFact
import com.example.data.FactStatus
import com.example.data.TimelineEvent
import com.example.ui.components.ReviewReminderBanner
import com.example.ui.components.StatusBadge
import com.example.ui.components.StickyBottomBar
import com.example.ui.theme.*

// ==========================================
// SCREEN 7 — REVIEW EXTRACTED INFORMATION
// ==========================================
@Composable
fun ReviewFactsScreen(
  facts: List<ExtractedFact>,
  onApproveFact: (String) -> Unit,
  onUpdateFact: (String, String, FactStatus) -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  var editingFact by remember { mutableStateOf<ExtractedFact?>(null) }
  var editedValue by remember { mutableStateOf("") }
  var editedStatus by remember { mutableStateOf(FactStatus.VERIFIED) }

  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "Here's what we found",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Our AI parsed your uploaded evidence into these key facts. Please review and confirm them.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      ReviewReminderBanner()

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "EXTRACTED CLAIM DETAILS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral500,
        letterSpacing = 0.8.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      facts.forEach { fact ->
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = PureWhite,
          border = BorderStroke(
            1.dp,
            if (fact.status == FactStatus.NEEDS_REVIEW) Amber300 else Neutral200
          ),
          shadowElevation = 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = fact.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Neutral500
              )

              StatusBadge(status = fact.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = fact.value,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Neutral900
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              if (fact.status != FactStatus.VERIFIED) {
                OutlinedButton(
                  onClick = { onApproveFact(fact.id) },
                  colors = ButtonDefaults.outlinedButtonColors(contentColor = Green700),
                  border = BorderStroke(1.dp, Green500),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                  modifier = Modifier.height(34.dp)
                ) {
                  Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.width(8.dp))
              }

              OutlinedButton(
                onClick = {
                  editingFact = fact
                  editedValue = fact.value
                  editedStatus = fact.status
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue700),
                border = BorderStroke(1.dp, Blue200),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }
      }
    }

    StickyBottomBar(
      primaryText = "Continue to Timeline",
      onPrimaryClick = onContinue,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }

  if (editingFact != null) {
    AlertDialog(
      onDismissRequest = { editingFact = null },
      title = { Text("Edit ${editingFact?.label}", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text("Update the extracted value:", fontSize = 13.sp, color = Neutral600)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = editedValue,
            onValueChange = { editedValue = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(14.dp))
          Text("Status:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Neutral700)
          Spacer(modifier = Modifier.height(6.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
              selected = editedStatus == FactStatus.VERIFIED,
              onClick = { editedStatus = FactStatus.VERIFIED },
              label = { Text("Verified") }
            )
            FilterChip(
              selected = editedStatus == FactStatus.NEEDS_REVIEW,
              onClick = { editedStatus = FactStatus.NEEDS_REVIEW },
              label = { Text("Needs Review") }
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            editingFact?.let { fact ->
              onUpdateFact(fact.id, editedValue, editedStatus)
            }
            editingFact = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
          Text("Save Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingFact = null }) {
          Text("Cancel", color = Neutral600)
        }
      }
    )
  }
}

// ==========================================
// SCREEN 8 — TIMELINE
// ==========================================
@Composable
fun TimelineScreen(
  timeline: List<TimelineEvent>,
  onAddEvent: (String, String, FactStatus) -> Unit,
  onUpdateEvent: (String, String, String, FactStatus) -> Unit,
  onDeleteEvent: (String) -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddDialog by remember { mutableStateOf(false) }
  var editingEvent by remember { mutableStateOf<TimelineEvent?>(null) }
  var inputDate by remember { mutableStateOf("") }
  var inputTitle by remember { mutableStateOf("") }
  var inputStatus by remember { mutableStateOf(FactStatus.VERIFIED) }

  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "Your timeline",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Review the timeline carefully. You are in control of what goes into your document.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      ReviewReminderBanner()

      Spacer(modifier = Modifier.height(20.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "CHRONOLOGICAL MILESTONES (${timeline.size})",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Neutral500,
          letterSpacing = 0.8.sp
        )

        OutlinedButton(
          onClick = {
            inputDate = "March 28, 2026"
            inputTitle = ""
            inputStatus = FactStatus.VERIFIED
            showAddDialog = true
          },
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Event", fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      timeline.forEachIndexed { index, event ->
        val isLast = index == timeline.lastIndex

        Row(modifier = Modifier.fillMaxWidth()) {
          // Left Node & Vertical Connector Line
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (event.status == FactStatus.VERIFIED) Green100 else Amber100)
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(if (event.status == FactStatus.VERIFIED) Green600 else Amber600)
              )
            }

            if (!isLast) {
              Box(
                modifier = Modifier
                  .width(2.dp)
                  .height(96.dp)
                  .background(Neutral300)
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          // Event Card
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = PureWhite,
            border = BorderStroke(
              1.dp,
              if (event.status == FactStatus.NEEDS_REVIEW) Amber300 else Neutral200
            ),
            shadowElevation = 1.dp,
            modifier = Modifier
              .weight(1f)
              .padding(bottom = if (isLast) 0.dp else 14.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  color = Neutral100,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = event.date,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }

                StatusBadge(status = event.status)
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = event.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Neutral900
              )

              if (event.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = event.description,
                  fontSize = 12.sp,
                  color = Neutral600
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                TextButton(
                  onClick = {
                    editingEvent = event
                    inputDate = event.date
                    inputTitle = event.title
                    inputStatus = event.status
                  },
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                  modifier = Modifier.height(30.dp)
                ) {
                  Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Edit", fontSize = 12.sp, color = Blue600)
                }

                Spacer(modifier = Modifier.width(4.dp))

                TextButton(
                  onClick = { onDeleteEvent(event.id) },
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                  modifier = Modifier.height(30.dp)
                ) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Delete", fontSize = 12.sp, color = Neutral500)
                }
              }
            }
          }
        }
      }
    }

    StickyBottomBar(
      primaryText = "Continue to Document",
      onPrimaryClick = onContinue,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }

  // Add Event Dialog
  if (showAddDialog) {
    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Add Timeline Event", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          OutlinedTextField(
            value = inputDate,
            onValueChange = { inputDate = it },
            label = { Text("Event Date") },
            placeholder = { Text("e.g. March 22, 2026") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = inputTitle,
            onValueChange = { inputTitle = it },
            label = { Text("Event Description") },
            placeholder = { Text("e.g. Second follow-up call with client") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (inputTitle.isNotBlank()) {
              onAddEvent(inputDate, inputTitle, inputStatus)
            }
            showAddDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
          Text("Add Event")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel", color = Neutral600)
        }
      }
    )
  }

  // Edit Event Dialog
  if (editingEvent != null) {
    AlertDialog(
      onDismissRequest = { editingEvent = null },
      title = { Text("Edit Timeline Event", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          OutlinedTextField(
            value = inputDate,
            onValueChange = { inputDate = it },
            label = { Text("Event Date") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = inputTitle,
            onValueChange = { inputTitle = it },
            label = { Text("Event Description") },
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text("Status:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Neutral700)
          Spacer(modifier = Modifier.height(6.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
              selected = inputStatus == FactStatus.VERIFIED,
              onClick = { inputStatus = FactStatus.VERIFIED },
              label = { Text("Verified") }
            )
            FilterChip(
              selected = inputStatus == FactStatus.NEEDS_REVIEW,
              onClick = { inputStatus = FactStatus.NEEDS_REVIEW },
              label = { Text("Needs Review") }
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            editingEvent?.let { e ->
              onUpdateEvent(e.id, inputDate, inputTitle, inputStatus)
            }
            editingEvent = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingEvent = null }) {
          Text("Cancel", color = Neutral600)
        }
      }
    )
  }
}
