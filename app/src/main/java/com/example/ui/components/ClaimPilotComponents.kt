package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FactStatus
import com.example.data.StepCategory
import com.example.ui.theme.*

@Composable
fun ClaimPilotTopBar(
  canNavigateBack: Boolean,
  onNavigateBack: () -> Unit,
  onResetDemo: () -> Unit,
  currentCategory: StepCategory? = null,
  modifier: Modifier = Modifier
) {
  Surface(
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 1.dp,
    shadowElevation = 1.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (canNavigateBack) {
            IconButton(
              onClick = onNavigateBack,
              modifier = Modifier
                .testTag("btn_back")
                .size(40.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
          }

          // Brand Logo Badge
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Blue600)
          ) {
            Icon(
              imageVector = Icons.Default.NearMe,
              contentDescription = "ClaimPilot Logo",
              tint = PureWhite,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = "ClaimPilot",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = MaterialTheme.colorScheme.onSurface,
              letterSpacing = (-0.3).sp
            )
            Text(
              text = "Turn proof into a clear claim",
              fontSize = 11.sp,
              color = Neutral500,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        // Demo Reset Button
        TextButton(
          onClick = onResetDemo,
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.testTag("btn_reset_demo")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset Demo",
            tint = Neutral500,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("Demo", fontSize = 12.sp, color = Neutral500)
        }
      }

      if (currentCategory != null) {
        StepProgressBar(currentCategory = currentCategory)
      }
    }
  }
}

@Composable
fun StepProgressBar(
  currentCategory: StepCategory,
  modifier: Modifier = Modifier
) {
  val steps = StepCategory.values()

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(Neutral100)
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      steps.forEach { step ->
        val isCompleted = step.stepNumber < currentCategory.stepNumber
        val isCurrent = step.stepNumber == currentCategory.stepNumber

        val bgColor = when {
          isCompleted -> Green600
          isCurrent -> Blue600
          else -> Neutral300
        }
        val textColor = when {
          isCurrent -> Blue600
          isCompleted -> Green700
          else -> Neutral500
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 2.dp)
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(20.dp)
              .clip(CircleShape)
              .background(bgColor)
          ) {
            if (isCompleted) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = PureWhite,
                modifier = Modifier.size(12.dp)
              )
            } else {
              Text(
                text = "${step.stepNumber}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) PureWhite else Neutral700
              )
            }
          }

          Spacer(modifier = Modifier.width(4.dp))

          Text(
            text = step.title,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = textColor
          )
        }
      }
    }
  }
}

@Composable
fun StatusBadge(
  status: FactStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, icon, label) = when (status) {
    FactStatus.VERIFIED -> Quadruple(
      Green100,
      Green700,
      Icons.Default.CheckCircle,
      "Verified"
    )
    FactStatus.NEEDS_REVIEW -> Quadruple(
      Amber100,
      Amber800,
      Icons.Default.Warning,
      "Needs review"
    )
    FactStatus.UNCERTAIN -> Quadruple(
      Neutral200,
      Neutral700,
      Icons.Default.Help,
      "Uncertain"
    )
  }

  Surface(
    color = bgColor,
    shape = RoundedCornerShape(12.dp),
    modifier = modifier
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = textColor,
        modifier = Modifier.size(13.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun DisclaimerFooter(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      verticalAlignment = Alignment.Top,
      modifier = Modifier.fillMaxWidth(0.95f)
    ) {
      Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Notice",
        tint = Neutral400,
        modifier = Modifier
          .size(14.dp)
          .padding(top = 2.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "ClaimPilot provides AI-assisted document preparation and organization. It is not a law firm and does not provide legal advice or legal representation.",
        fontSize = 11.sp,
        color = Neutral500,
        lineHeight = 15.sp,
        textAlign = TextAlign.Start
      )
    }
  }
}

@Composable
fun ReassuranceBanner(
  text: String = "No permanent data storage.",
  modifier: Modifier = Modifier
) {
  Surface(
    color = Teal50,
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(1.dp, Teal100),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Security,
        contentDescription = "Security",
        tint = Teal700,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = text,
        fontSize = 13.sp,
        color = Teal800,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun ReviewReminderBanner(
  modifier: Modifier = Modifier
) {
  Surface(
    color = Amber50,
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(1.dp, Amber100),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Psychology,
        contentDescription = "Review",
        tint = Amber700,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = "AI suggestions should always be reviewed before creating your document.",
        fontSize = 12.sp,
        color = Amber800,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun StickyBottomBar(
  primaryText: String,
  onPrimaryClick: () -> Unit,
  isPrimaryEnabled: Boolean = true,
  testTag: String = "btn_primary_continue",
  secondaryText: String? = null,
  onSecondaryClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Surface(
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 8.dp,
    tonalElevation = 2.dp,
    modifier = modifier
      .fillMaxWidth()
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      Button(
        onClick = onPrimaryClick,
        enabled = isPrimaryEnabled,
        colors = ButtonDefaults.buttonColors(
          containerColor = Blue600,
          contentColor = PureWhite
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag(testTag)
      ) {
        Text(
          text = primaryText,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }

      if (secondaryText != null && onSecondaryClick != null) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
          onClick = onSecondaryClick,
          shape = RoundedCornerShape(14.dp),
          border = BorderStroke(1.dp, Neutral300),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Text(
            text = secondaryText,
            color = Neutral700,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}
