package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DisclaimerFooter
import com.example.ui.components.ReassuranceBanner
import com.example.ui.theme.*

@Composable
fun LandingScreen(
  onStartClaim: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showHowItWorksDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Neutral50)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    // Reassurance Pill
    Surface(
      color = Teal50,
      shape = RoundedCornerShape(20.dp),
      border = BorderStroke(1.dp, Teal100),
      modifier = Modifier.padding(bottom = 16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = Teal700,
          modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "No permanent data storage",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = Teal800
        )
      }
    }

    // Hero Heading
    Text(
      text = "Didn't get paid?",
      fontSize = 32.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Neutral900,
      textAlign = TextAlign.Center,
      letterSpacing = (-0.5).sp
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Supporting Text
    Text(
      text = "Turn your messages, documents and proof into a clear timeline and a professional demand letter.",
      fontSize = 16.sp,
      color = Neutral600,
      textAlign = TextAlign.Center,
      lineHeight = 24.sp,
      modifier = Modifier.padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(28.dp))

    // Emotional Reassurance Callout
    Surface(
      color = PureWhite,
      shape = RoundedCornerShape(16.dp),
      shadowElevation = 2.dp,
      border = BorderStroke(1.dp, Neutral200),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Blue50)
        ) {
          Icon(
            imageVector = Icons.Default.Handshake,
            contentDescription = null,
            tint = Blue600,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = "You've already taken the first step.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral900
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Let's organize what happened calmly and clearly.",
            fontSize = 13.sp,
            color = Neutral600
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Primary CTA Button
    Button(
      onClick = onStartClaim,
      colors = ButtonDefaults.buttonColors(
        containerColor = Blue600,
        contentColor = PureWhite
      ),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("btn_start_my_claim")
    ) {
      Text(
        text = "Start My Claim",
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.width(8.dp))
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        modifier = Modifier.size(18.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Secondary CTA: How It Works
    OutlinedButton(
      onClick = { showHowItWorksDialog = true },
      shape = RoundedCornerShape(14.dp),
      border = BorderStroke(1.dp, Neutral300),
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("btn_how_it_works")
    ) {
      Icon(
        imageVector = Icons.Default.HelpOutline,
        contentDescription = null,
        tint = Neutral700,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "How It Works",
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = Neutral700
      )
    }

    Spacer(modifier = Modifier.height(32.dp))

    // 3-Step Simple Section
    Text(
      text = "HOW CLAIMPILOT WORKS",
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = Neutral500,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    StepCard(
      stepNumber = "1",
      title = "Add your proof",
      description = "Upload WhatsApp chats, invoices, emails or screenshots. No manual rewriting needed.",
      icon = Icons.Default.CloudUpload,
      iconTint = Blue600,
      bgTint = Blue50
    )

    Spacer(modifier = Modifier.height(12.dp))

    StepCard(
      stepNumber = "2",
      title = "Review your timeline",
      description = "We organize dates, agreements, and payment requests into an editable chronological story.",
      icon = Icons.Default.Timeline,
      iconTint = Teal600,
      bgTint = Teal50
    )

    Spacer(modifier = Modifier.height(12.dp))

    StepCard(
      stepNumber = "3",
      title = "Prepare your document",
      description = "Get a firm, polite, and professional demand letter ready to send, backed by clear facts.",
      icon = Icons.Default.Description,
      iconTint = Green600,
      bgTint = Green50
    )

    Spacer(modifier = Modifier.height(28.dp))

    ReassuranceBanner(text = "Privacy First: We do not permanently store your conversations or documents.")

    Spacer(modifier = Modifier.height(16.dp))

    DisclaimerFooter()

    Spacer(modifier = Modifier.height(16.dp))
  }

  if (showHowItWorksDialog) {
    AlertDialog(
      onDismissRequest = { showHowItWorksDialog = false },
      title = {
        Text("How ClaimPilot Works", fontWeight = FontWeight.Bold, color = Neutral900)
      },
      text = {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            "ClaimPilot takes the stress out of recovering unpaid invoices through 3 simple steps:",
            fontSize = 14.sp,
            color = Neutral700,
            lineHeight = 20.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            "1. Upload Your Proof: Drop in screenshots, exported text, or invoices.\n\n" +
            "2. Clarify & Confirm: Review the auto-organized timeline and key facts.\n\n" +
            "3. Export Document: Download a clean demand letter and choose data retention.",
            fontSize = 13.sp,
            color = Neutral600,
            lineHeight = 19.sp
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showHowItWorksDialog = false
            onStartClaim()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
          Text("Get Started")
        }
      },
      dismissButton = {
        TextButton(onClick = { showHowItWorksDialog = false }) {
          Text("Close", color = Neutral600)
        }
      }
    )
  }
}

@Composable
private fun StepCard(
  stepNumber: String,
  title: String,
  description: String,
  icon: ImageVector,
  iconTint: androidx.compose.ui.graphics.Color,
  bgTint: androidx.compose.ui.graphics.Color
) {
  Surface(
    color = PureWhite,
    shape = RoundedCornerShape(16.dp),
    shadowElevation = 1.dp,
    border = BorderStroke(1.dp, Neutral200),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(bgTint)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "$stepNumber. $title",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral900
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          fontSize = 13.sp,
          color = Neutral600,
          lineHeight = 18.sp
        )
      }
    }
  }
}
