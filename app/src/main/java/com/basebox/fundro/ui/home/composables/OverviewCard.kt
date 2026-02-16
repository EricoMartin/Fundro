package com.basebox.fundro.ui.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.home.OverviewItem
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroTextSecondary

@Composable
fun OverviewCard(
    myGroups: List<Group>,
    participatingGroups: List<Group>
) {
    val allGroups = myGroups + participatingGroups
    val totalLocked = allGroups.filter { it.status == "FUNDED" }.sumOf { it.totalCollected }
    val totalPending = allGroups.filter { it.status == "OPEN" }.sumOf { it.totalCollected }
    val totalPooled = allGroups.sumOf { it.totalCollected }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Your Escrow Overview",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OverviewItem(
                    amount = totalLocked,
                    label = "Locked",
                    modifier = Modifier.weight(1f)
                )

                OverviewItem(
                    amount = totalPending,
                    label = "Pending Release",
                    modifier = Modifier.weight(1f)
                )

                OverviewItem(
                    amount = totalPooled,
                    label = "Total Pooled (Lifetime)",
                    textColor = FundroGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Across ${allGroups.size} active pools • Last activity: 2 hours ago",
                style = MaterialTheme.typography.bodySmall,
                color = FundroTextSecondary
            )
        }
    }
}