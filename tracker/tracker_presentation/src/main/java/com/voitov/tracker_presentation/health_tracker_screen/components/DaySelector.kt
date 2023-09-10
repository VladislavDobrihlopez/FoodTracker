package com.voitov.tracker_presentation.health_tracker_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_presentation.utils.formatDate
import java.time.LocalDateTime

@Composable
fun DaySelector(
    date: State<LocalDateTime>,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current

    val time = remember(date) {
        formatDate(date.value, context)
    }

    Spacer(Modifier.height(spacing.spaceMedium))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.spaceMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            IconButton(onClick = onPreviousWeekClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.previous_week)
                )
            }
            IconButton(onClick = onPreviousDayClick) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.previous_day)
                )
            }
        }
        Text(text = time, style = MaterialTheme.typography.h2)
        Row {
            IconButton(onClick = onNextDayClick) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.next_day)
                )
            }
            IconButton(onClick = onNextWeekClick) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = stringResource(id = R.string.next_week)
                )
            }
        }
    }
    Spacer(Modifier.height(spacing.spaceMedium))

}
