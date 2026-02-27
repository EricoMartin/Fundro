package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.components.EmptyState
import com.basebox.fundro.domain.model.GroupMember
import androidx.compose.foundation.lazy.items
import com.basebox.fundro.ui.components.MemberPaidListItem
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActivityTab(
    group: Group,
    members: List<GroupMember>
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
//        val groupPayments = group.payments.sortedByDescending { it.createdAt }
//        val groupContributions = group.contributions.payments.sortedByDescending { it.createdAt }

        if (members.isEmpty()) {
            item {
                EmptyState(
                    title = "No activity yet",
                    message = "Transaction history and updates will appear here"
                )
            }
        } else {

            // Members list
            items(
                items = members,
                key = { it.id }
            ) { member ->
                MemberPaidListItem(member = member)

                if (member != members.last()) {
                    Divider(
                        modifier = Modifier.padding(start = 60.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}