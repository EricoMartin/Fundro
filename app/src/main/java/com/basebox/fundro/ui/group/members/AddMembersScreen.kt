package com.basebox.fundro.ui.group.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.basebox.fundro.ui.components.*
import com.basebox.fundro.ui.theme.FundroTextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddMembersScreen(
    navController: NavController,
    groupId: String,
    viewModel: AddMembersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val addSuccess by viewModel.addSuccess.collectAsState()

    // Navigate back on success
    LaunchedEffect(addSuccess) {
        if (addSuccess) {
            navController.popBackStack()
        }
    }

    // Show error snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.addError) {
        uiState.addError?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearAddError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Add Members",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        if (uiState.selectedUsers.isNotEmpty()) {
                            Text(
                                text = "${uiState.selectedUsers.size} selected",
                                style = MaterialTheme.typography.bodySmall,
                                color = FundroTextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    onSearch = { },
                    placeholder = "Search by name or username",
                    enabled = !uiState.isAdding
                )
                // After SearchBar, add:
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "or",
                        style = MaterialTheme.typography.bodySmall,
                        color = FundroTextSecondary
                    )

                    TextButton(
                        onClick = {
                            // TODO: Navigate to invite by email screen
                            // navController.navigate("group/$groupId/invite-email")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Invite by Email")
                    }
                }

                // Selected users chips
                if (uiState.selectedUsers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Selected Members",
                        style = MaterialTheme.typography.labelMedium,
                        color = FundroTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy( 8.dp)
                    ) {
                        uiState.selectedUsers.forEach { user ->
                            SelectedUserChip(
                                user = user,
                                onRemove = { viewModel.removeSelectedUser(user) }
                            )
                        }
                    }
                }
            }

            Divider()

            // Search results
            Box(
                modifier = Modifier.weight(1f)
            ) {
                when {
                    uiState.isSearching -> {
                        SearchResultsSkeletonList(count = 5)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.searchError != null -> {
                        EmptyState(
                            title = "Search failed",
                            message = uiState.searchError ?: "Unknown error"
                        )
                    }

                    uiState.searchQuery.length < 2 -> {
                        EmptyState(
                            title = "Search for users",
                            message = "Type at least 2 characters to search"
                        )
                    }

                    uiState.hasSearched && uiState.searchResults.isEmpty() -> {
                        EmptyState(
                            title = "No users found",
                            message = "Try a different search term"
                        )
                    }

                    uiState.searchResults.isNotEmpty() -> {
                        LazyColumn {
                            items(
                                items = uiState.searchResults,
                                key = { it.id }
                            ) { user ->
                                UserSearchItem(
                                    user = user,
                                    isSelected = uiState.selectedUsers.any { it.id == user.id },
                                    onToggleSelection = { viewModel.toggleUserSelection(user) }
                                )

                                if (user != uiState.searchResults.last()) {
                                    Divider(
                                        modifier = Modifier.padding(start = 52.dp),
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        EmptyState(
                            title = "Start searching",
                            message = "Find users to add to your pool"
                        )
                    }
                }
            }

            // Bottom section
            if (uiState.selectedUsers.isNotEmpty()) {
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
                        // Expected amount (optional)
                        FormTextField(
                            value = uiState.expectedAmount,
                            onValueChange = viewModel::onExpectedAmountChanged,
                            label = "Expected Amount (Optional)",
                            placeholder = "Same amount for all members",
                            errorMessage = uiState.expectedAmountError,
                            keyboardType = KeyboardType.Decimal,
                            leadingIcon = {
                                Text(
                                    text = "₦",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            enabled = !uiState.isAdding
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add button
                        Button(
                            onClick = viewModel::addMembers,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !uiState.isAdding && uiState.selectedUsers.isNotEmpty(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (uiState.isAdding) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Add ${uiState.selectedUsers.size} ${if (uiState.selectedUsers.size == 1) "Member" else "Members"}",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}