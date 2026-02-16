package com.basebox.fundro.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.basebox.fundro.ui.components.ErrorState
import com.basebox.fundro.ui.home.composables.HomeContent
import com.basebox.fundro.ui.home.composables.HomeTopBar
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val logoutSuccess by viewModel.logoutSuccess.collectAsState()

    // Navigate to login on logout
    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                userName = uiState.user?.fullName ?: "",
                onProfileClick = { /* TODO: Navigate to profile */ },
                onNotificationClick = { /* TODO: Navigate to notifications */ }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO: Navigate to create group */ },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Pool"
                    )
                },
                text = { Text("Create Pool") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isRefreshing),
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading your groups...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FundroTextSecondary
                        )
                    }
                }

                uiState.error != null && !uiState.isRefreshing -> {
                    ErrorState(
                        message = uiState.error ?: "Unknown error",
                        onRetry = { viewModel.loadGroups() }
                    )
                }

                else -> {
                    HomeContent(
                        uiState = uiState,
                        onTabSelected = viewModel::selectTab,
                        onGroupClick = { groupId ->
                            // TODO: Navigate to group detail
                            // navController.navigate("group/$groupId")
                        }
                    )
                }
            }
        }
    }
}


