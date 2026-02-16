package com.basebox.fundro.ui.group.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.ui.components.EmptyState

@Composable
fun ActivityTab(
    group: Group
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            EmptyState(
                title = "No activity yet",
                message = "Transaction history and updates will appear here"
            )
        }
    }
}