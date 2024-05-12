package com.vinilos.misw4203.grupo6_202412.view.uiControls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto

@Composable
fun PerformerChip(performer: ArtistDto) {
    ElevatedAssistChip(
        onClick = { },
        label = { Text(text = performer.name ?: "") },
        leadingIcon = {
            ImageAsync(
                url = performer.image ?: "",
                contentDescription = performer.name ?: "",
                modifier = Modifier
                    .width(18.0.dp)
                    .size(AssistChipDefaults.IconSize)
                    .clip(CircleShape)
            )
        },

        shape = RoundedCornerShape(50.dp),
        border = AssistChipDefaults.assistChipBorder(
            borderColor = Color.Black,
            disabledBorderColor = Color.Gray
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerformersChips(performers: List<ArtistDto>) {
    Text(
        text = stringResource(id = R.string.performerTitle),
        style = MaterialTheme.typography.titleMedium
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        performers.forEach { performer ->
            PerformerChip(performer)
        }
    }
}
