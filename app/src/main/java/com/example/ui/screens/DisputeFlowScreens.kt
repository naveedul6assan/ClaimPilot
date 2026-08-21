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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CountryOption
import com.example.data.DisputeTypeOption
import com.example.ui.components.StickyBottomBar
import com.example.ui.theme.*

// ==========================================
// SCREEN 2 — COUNTRY SELECTION
// ==========================================
@Composable
fun CountryScreen(
  countries: List<CountryOption>,
  selectedCountry: CountryOption,
  onSelectCountry: (CountryOption) -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp) // Space for sticky bottom bar
    ) {
      Text(
        text = "Where is your dispute located?",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Select the country or jurisdiction where the agreement took place or the invoice was issued.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(24.dp))

      countries.forEach { country ->
        val isSelected = country.id == selectedCountry.id

        Surface(
          onClick = { onSelectCountry(country) },
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
            .testTag("country_card_${country.id}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
          ) {
            Text(
              text = country.flagEmoji,
              fontSize = 28.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = country.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Blue900 else Neutral900
              )
              Text(
                text = "${country.region} • Currency: ${country.currencySymbol}",
                fontSize = 12.sp,
                color = Neutral500
              )
            }

            if (isSelected) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(Blue600)
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = PureWhite,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }

    StickyBottomBar(
      primaryText = "Continue",
      onPrimaryClick = onContinue,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}

// ==========================================
// SCREEN 3 — DISPUTE TYPE
// ==========================================
@Composable
fun DisputeTypeScreen(
  disputeTypes: List<DisputeTypeOption>,
  selectedType: String,
  onSelectType: (String) -> Unit,
  onContinue: () -> Unit,
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
        text = "What happened?",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Choose the type of claim you want to organize. In Build 1, Unpaid Invoice claims are fully active.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(24.dp))

      disputeTypes.forEach { option ->
        val isSelected = option.isAvailable && option.id == selectedType
        val isEnabled = option.isAvailable

        Surface(
          onClick = { if (isEnabled) onSelectType(option.id) },
          enabled = isEnabled,
          shape = RoundedCornerShape(16.dp),
          color = when {
            isSelected -> Blue50
            isEnabled -> PureWhite
            else -> Neutral100
          },
          border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Blue600 else Neutral200
          ),
          shadowElevation = if (isSelected) 2.dp else if (isEnabled) 1.dp else 0.dp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("dispute_type_${option.id}")
        ) {
          Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(18.dp)
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isEnabled) Blue100 else Neutral200)
            ) {
              Icon(
                imageVector = if (isEnabled) Icons.Default.ReceiptLong else Icons.Default.Lock,
                contentDescription = null,
                tint = if (isEnabled) Blue700 else Neutral400,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = option.title,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isEnabled) Neutral900 else Neutral400
                )

                if (!isEnabled) {
                  Surface(
                    color = Neutral200,
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text(
                      text = "Coming soon",
                      fontSize = 11.sp,
                      color = Neutral500,
                      fontWeight = FontWeight.Medium,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                } else if (isSelected) {
                  Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                      .size(22.dp)
                      .clip(CircleShape)
                      .background(Blue600)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = PureWhite,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = option.description,
                fontSize = 13.sp,
                color = if (isEnabled) Neutral600 else Neutral400,
                lineHeight = 18.sp
              )
            }
          }
        }
      }
    }

    StickyBottomBar(
      primaryText = "Continue",
      onPrimaryClick = onContinue,
      isPrimaryEnabled = selectedType.isNotEmpty(),
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}

// ==========================================
// SCREEN 4 — TELL US WHAT HAPPENED
// ==========================================
@Composable
fun StoryScreen(
  storyText: String,
  onStoryChange: (String) -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  val placeholder = "My client agreed to pay me $500 for a website project, but I completed the work and haven't received payment."

  Box(modifier = modifier.fillMaxSize().background(Neutral50)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .padding(bottom = 90.dp)
    ) {
      Text(
        text = "Tell us what happened",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral900,
        letterSpacing = (-0.3).sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "You don't need legal language. Explain it in your own words.",
        fontSize = 14.sp,
        color = Neutral600,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Input Box Card
      Surface(
        color = PureWhite,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          OutlinedTextField(
            value = storyText,
            onValueChange = onStoryChange,
            placeholder = {
              Text(
                text = placeholder,
                color = Neutral400,
                fontSize = 14.sp,
                lineHeight = 20.sp
              )
            },
            minLines = 6,
            maxLines = 10,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Blue600,
              unfocusedBorderColor = Neutral200,
              focusedContainerColor = PureWhite,
              unfocusedContainerColor = PureWhite
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_story_text")
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "You can write as much or as little as you want.",
              fontSize = 12.sp,
              color = Neutral500
            )

            TextButton(
              onClick = {
                onStoryChange(
                  "My client Jack agreed to pay me $500 for a website design project, but I completed the work on March 15 and delivered all deliverables, yet haven't received payment despite reminders."
                )
              },
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text("Insert Demo Story", fontSize = 12.sp, color = Blue600)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Quick Inspiration Prompts
      Text(
        text = "HELPFUL DETAILS TO MENTION:",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Neutral500,
        letterSpacing = 0.8.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      val prompts = listOf(
        "• What service or product was provided?",
        "• The agreed total amount owed (e.g. $500)",
        "• When the project was delivered",
        "• When the payment was originally due"
      )

      prompts.forEach { prompt ->
        Text(
          text = prompt,
          fontSize = 13.sp,
          color = Neutral700,
          modifier = Modifier.padding(vertical = 2.dp)
        )
      }
    }

    StickyBottomBar(
      primaryText = "Continue",
      onPrimaryClick = onContinue,
      isPrimaryEnabled = storyText.trim().isNotEmpty(),
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}
