package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.ui.components.EmptyState
import com.basebox.fundro.ui.components.MemberListItem


@Composable
fun MembersTab(
    members: List<GroupMember>,
    isOwner: Boolean,
    onAddMembersClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        if (members.isEmpty()) {
            item {
                EmptyState(
                    title = "No members yet",
                    message = if (isOwner) {
                        "Add members to start collecting contributions"
                    } else {
                        "This group has no members yet"
                    }
                )
            }
        } else {
            // Summary
            item {
                MembersSummaryCard(members = members)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Members list
            items(
                items = members,
                key = { it.id }
            ) { member ->
                MemberListItem(member = member)

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