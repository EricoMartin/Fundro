package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.ui.group.detail.GroupDetailUiState
import com.basebox.fundro.ui.group.enums.DetailTab
import com.basebox.fundro.ui.theme.FundroGreen

@Composable
    fun GroupDetailContent(
        uiState: GroupDetailUiState,
        onTabSelected: (DetailTab) -> Unit,
        onContributeClick: () -> Unit,
        onAddMembersClick: () -> Unit,
        onViewDetailsClick: () -> Unit,
        onReleaseFundsClick: () -> Unit
    ) {
        val group = uiState.group ?: return

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // GroupEntity Summary Card
            GroupSummaryCard(
                group = group,
                isOwner = uiState.isOwner
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            ActionButtonsRow(
                group = group,
                isOwner = uiState.isOwner,
                onContributeClick = onContributeClick,
                onAddMembersClick = onAddMembersClick,
                onReleaseFundsClick = onReleaseFundsClick,
                uiState = uiState
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            DetailTabs(
                selectedTab = uiState.selectedTab,
                onTabSelected = onTabSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tab Content
            when (uiState.selectedTab) {
                DetailTab.DETAILS -> {
                    DetailsTab(
                        group = group,
                        onViewDetailsClick = onViewDetailsClick
                    )
                }

                DetailTab.MEMBERS -> {
                    MembersTab(
                        members = uiState.members,
                        isOwner = uiState.isOwner,
                        onAddMembersClick = onAddMembersClick
                    )
                }

                DetailTab.ACTIVITY -> {
                    ActivityTab(
                        group = group,
                        members = uiState.members
                        )
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Show different buttons based on group status and ownership
//                    when {
//                        // COMPLETED: Show "View Receipt" (optional)
//                        group.status == "COMPLETED" -> {
//                            OutlinedButton(
//                                onClick = { /* TODO: Show receipt */ },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(56.dp),
//                                shape = RoundedCornerShape(12.dp)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Receipt,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = "View Receipt",
//                                    style = MaterialTheme.typography.labelLarge
//                                )
//                            }
//                        }
//
//                        // FUNDED & OWNER: Show "Release Funds"
//                        group.status == "FUNDED" && uiState.isOwner -> {
//                            Button(
//                                onClick = onReleaseFundsClick,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(56.dp),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = FundroGreen
//                                )
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.AccountBalance,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = "Release Funds (₦${group.totalCollected.toNaira()})",
//                                    style = MaterialTheme.typography.labelLarge.copy(
//                                        fontWeight = FontWeight.SemiBold
//                                    )
//                                )
//                            }
//                        }
//
//                        // OPEN & NOT CONTRIBUTED: Show "Contribute"
//                        group.status == "OPEN" && !group.hasCurrentUserContributed -> {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(12.dp)
//                            ) {
//                                if (uiState.isOwner) {
//                                    OutlinedButton(
//                                        onClick = onAddMembersClick,
//                                        modifier = Modifier
//                                            .weight(1f)
//                                            .height(56.dp),
//                                        shape = RoundedCornerShape(12.dp)
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.PersonAdd,
//                                            contentDescription = null
//                                        )
//                                        Spacer(modifier = Modifier.width(4.dp))
//                                        Text("Add Members")
//                                    }
//                                }
//
//                                Button(
//                                    onClick = onContributeClick,
//                                    modifier = Modifier
//                                        .weight(if (uiState.isOwner) 1f else 1f)
//                                        .height(56.dp),
//                                    shape = RoundedCornerShape(12.dp),
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = MaterialTheme.colorScheme.primary
//                                    )
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Payment,
//                                        contentDescription = null
//                                    )
//                                    Spacer(modifier = Modifier.width(8.dp))
//                                    Text("Contribute")
//                                }
//                            }
//                        }
//
//                        // Already contributed
//                        group.hasCurrentUserContributed -> {
//                            OutlinedButton(
//                                onClick = { /* Already contributed */ },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(56.dp),
//                                enabled = false,
//                                shape = RoundedCornerShape(12.dp)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.CheckCircle,
//                                    contentDescription = null,
//                                    tint = FundroGreen
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text("You've Contributed")
//                            }
//                        }
//                    }
                }
            }
        }
    }