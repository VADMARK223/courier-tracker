package com.example.couriertracker.ui.slot.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.model.slot.SlotWithService
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SlotListItem(
    item: SlotWithService,
    onDelete: (Slot) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val startTime = item.slot.startTime.format(timeFormatter)
    val endTime = item.slot.endTime.format(timeFormatter)

    val duration = Duration.between(
        item.slot.startTime,
        item.slot.endTime
    )

    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60

    val dateFormatter = DateTimeFormatter.ofPattern(
        "d MMM",
        Locale("ru")
    )

    val date = item.slot.startTime.format(dateFormatter)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = item.service.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "$startTime — $endTime",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "  |  ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = buildString {
                            if (hours > 0) {
                                append("$hours ч")
                            }

                            if (minutes > 0) {
                                if (hours > 0) {
                                    append(" ")
                                }

                                append("$minutes мин")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = {
                    onDelete(item.slot)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить слот",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}