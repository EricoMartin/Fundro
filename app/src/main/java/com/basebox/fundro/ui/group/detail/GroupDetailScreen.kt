package com.basebox.fundro.ui.group.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.basebox.fundro.core.security.SecureStorage
import com.basebox.fundro.core.util.toNaira
import com.basebox.fundro.ui.components.ErrorState
import com.basebox.fundro.ui.components.feedback.LocalFeedbackManager
import com.basebox.fundro.ui.group.detail.composables.GroupDetailContent
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    navController: NavController,
    groupId: String,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val feedbackManager = LocalFeedbackManager.current

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    //Navigate to disbursement screen when disbursement is completed

    LaunchedEffect(uiState.isCompleted) {
        feedbackManager.setNavController(navController)
        savedStateHandle?.getStateFlow("disbursement_completed", false)?.collect { completed ->
            if (uiState.isCompleted && completed) {
                viewModel.refresh()
                navController.navigate("disbursement/$groupId")
                savedStateHandle["disbursement_completed"] = false
            }
        }
    }
    LaunchedEffect(savedStateHandle) {
        feedbackManager.setNavController(navController)
        savedStateHandle?.getStateFlow("payment_completed", false)?.collect { completed ->
            if (completed) {
                viewModel.refreshAfterPayment()
                savedStateHandle["payment_completed"] = false
            }
        }
    }

    // Show error snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.group?.name ?: "Loading...",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (uiState.group != null) {
                            Text(
                                text = "${uiState.members.size} members",
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
                actions = {
                    IconButton(onClick = { /* TODO: Share group */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }

                    if (uiState.isOwner) {
                        IconButton(onClick = { /* TODO: GroupEntity settings */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
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
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null && uiState.group == null -> {
                    ErrorState(
                        message = uiState.error ?: "Unknown error",
                        onRetry = { viewModel.loadGroupDetails() }
                    )
                }

                uiState.group != null -> {
                    GroupDetailContent(
                        uiState = uiState,
                        onTabSelected = viewModel::selectTab,
                        onContributeClick = {
                            navController.navigate("payment/${uiState.group?.id}")
                        },
                        onAddMembersClick = {
                            navController.navigate("group/${uiState.group?.id}/add-members")
                        },
                        onViewDetailsClick = {
                            // TODO: Show full details
                        },
                        onReleaseFundsClick = {

                            //navController.navigate("disbursement/${uiState.group?.id}")
                            feedbackManager.setNavController(navController)
                            feedbackManager.showWarning(
                                title = "Release Funds?",
                                message = "This will transfer ₦${uiState.group?.totalCollected?.toNaira()} to the recipient's account. This action cannot be undone.",
                                confirmText = "Release Funds",
                                cancelText = "Cancel",
                                onConfirm = {
                                    viewModel.navigateToDisbursement()
                                    // Navigate to disbursement screen
                                },
                                onCancel = navController::popBackStack,
                                onNavigate = {
                                },
                            )
                        })
                }
            }
        }
    }
}