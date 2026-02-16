package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.components.ActionButton

@Composable
fun ActionButtonsRow(
    group: Group,
    isOwner: Boolean,
    onContributeClick: () -> Unit,
    onAddMembersClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!group.hasCurrentUserContributed && group.status == "OPEN") {
            ActionButton(
                text = "Contribute",
                onClick = onContributeClick,
                icon = Icons.Default.Payment,
                modifier = Modifier.weight(1f)
            )
        }

        if (isOwner && group.status == "OPEN") {
            ActionButton(
                text = "Add Members",
                onClick = onAddMembersClick,
                icon = Icons.Default.PersonAdd,
                outlined = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}