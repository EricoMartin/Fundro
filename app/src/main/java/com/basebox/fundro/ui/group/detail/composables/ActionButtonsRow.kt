package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.components.ActionButton
import com.basebox.fundro.ui.group.detail.GroupDetailUiState
import com.basebox.fundro.ui.theme.FundroGreen

@Composable
fun ActionButtonsRow(
    group: Group,
    isOwner: Boolean,
    onContributeClick: () -> Unit,
    onAddMembersClick: () -> Unit,
    onReleaseFundsClick: () -> Unit,
    uiState: GroupDetailUiState
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
        when {
            // COMPLETED: Show "View Receipt" (optional)
            group.status == "COMPLETED" -> {
                OutlinedButton(
                    onClick = { /* TODO: Show receipt */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Receipt",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // FUNDED & OWNER: Show "Release Funds"
            group.status == "FUNDED" && uiState.isOwner -> {
                Button(
                    onClick = onReleaseFundsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FundroGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Release Funds (${group.totalCollected.toNaira()})",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // OPEN & NOT CONTRIBUTED: Show "Contribute"
            group.status == "OPEN" && !group.hasCurrentUserContributed -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.isOwner) {
                        OutlinedButton(
                            onClick = onAddMembersClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Members")
                        }
                    }

                    Button(
                        onClick = onContributeClick,
                        modifier = Modifier
                            .weight(if (uiState.isOwner) 1f else 1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contribute")
                    }
                }
            }

            // Already contributed
//            group.hasCurrentUserContributed -> {
//                OutlinedButton(
//                    onClick = { /* Already contributed */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    enabled = false,
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CheckCircle,
//                        contentDescription = null,
//                        tint = FundroGreen
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("You've Contributed")
//                }
//            }
        }
    }
}