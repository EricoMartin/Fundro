package com.basebox.fundro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.basebox.fundro.domain.model.SearchUser

@Composable
fun UserSearchItem(
    user: SearchUser,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleSelection)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox/Selection indicator
        Icon(
            imageVector = if (isSelected) {
                Icons.Default.CheckCircle
            } else {
                Icons.Outlined.Circle
            },
            contentDescription = if (isSelected) "Selected ${user.fullName}" else "Select ${user.fullName}",
            tint = if (isSelected) {
                FundroGreen
            } else {
                MaterialTheme.colorScheme.outline
            },
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.fullName.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // User info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodySmall,
                color = FundroTextSecondary
            )
        }
    }
}