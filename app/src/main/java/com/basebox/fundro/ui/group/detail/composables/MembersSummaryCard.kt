package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.ui.group.detail.SummaryItem
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroOrange

@Composable
fun MembersSummaryCard(
    members: List<GroupMember>
) {
    val paidCount = members.count { it.status == "PAID" }
    val joinedCount = members.count { it.status == "JOINED" }
    val invitedCount = members.count { it.status == "INVITED" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                count = paidCount,
                label = "Paid",
                color = FundroGreen
            )

            SummaryItem(
                count = joinedCount,
                label = "Joined",
                color = MaterialTheme.colorScheme.primary
            )

            SummaryItem(
                count = invitedCount,
                label = "Pending",
                color = FundroOrange
            )
        }
    }
}