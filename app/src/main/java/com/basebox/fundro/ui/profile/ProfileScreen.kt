package com.basebox.fundro.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.basebox.fundro.ui.components.*
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroTextSecondary
import com.basebox.fundro.core.util.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
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

    // Logout confirmation dialog
    if (uiState.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = viewModel::logout,
            onDismiss = viewModel::hideLogoutDialog
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                actions = {
                    // Edit profile button
                    IconButton(
                        onClick = { navController.navigate("profile/edit") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.user != null -> {
                ProfileContent(
                    uiState = uiState,
                    modifier = Modifier.padding(padding),
                    onEditProfile = { navController.navigate("profile/edit") },
                    onKycClick = { /* TODO: Navigate to KYC screen */ },
                    onSecurityClick = { /* TODO: Navigate to security settings */ },
                    onHelpClick = { /* TODO: Navigate to help */ },
                    onLogoutClick = viewModel::showLogoutDialog
                )
            }

            else -> {
                ErrorState(
                    message = uiState.error ?: "Failed to load profile",
                    onRetry = viewModel::loadUserProfile,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        // Logout loading overlay
        if (uiState.isLoggingOut) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text(
                            text = "Logging out...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit,
    onKycClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val user = uiState.user ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header
        ProfileHeader(
            user = user,
            onEditProfile = onEditProfile
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Account details section
        ProfileSection(title = "Account Details") {
            ProfileInfoItem(
                label = "Full Name",
                value = user.fullName,
                icon = Icons.Default.Person
            )
            Divider(
                modifier = Modifier.padding(start = 54.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            ProfileInfoItem(
                label = "Username",
                value = "@${user.username}",
                icon = Icons.Default.AlternateEmail
            )
            Divider(
                modifier = Modifier.padding(start = 54.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            ProfileInfoItem(
                label = "Email",
                value = user.email,
                icon = Icons.Default.Email
            )
            Divider(
                modifier = Modifier.padding(start = 54.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            ProfileInfoItem(
                label = "Phone",
                value = user.phoneNumber,
                icon = Icons.Default.Phone
            )
            if (user.createdAt != null) {
                Divider(
                    modifier = Modifier.padding(start = 54.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
                ProfileInfoItem(
                    label = "Member since",
                    value = user.createdAt.toFormattedDate(),
                    icon = Icons.Default.CalendarToday
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bank account section
        ProfileSection(title = "Bank Account") {
            if (user.bankName != null && user.accountHolderName != null) {
                ProfileInfoItem(
                    label = "Bank",
                    value = user.bankName,
                    icon = Icons.Default.AccountBalance
                )
                Divider(
                    modifier = Modifier.padding(start = 54.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
                ProfileInfoItem(
                    label = "Account Name",
                    value = user.accountHolderName,
                    icon = Icons.Default.Person
                )
            } else {
                ProfileMenuItem(
                    title = "Link Bank Account",
                    subtitle = "Required for receiving payouts",
                    icon = Icons.Default.AddCard,
                    onClick = onKycClick,
                    iconTint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Settings section
        ProfileSection(title = "Settings") {
            ProfileMenuItem(
                title = "KYC Verification",
                subtitle = when (user.kycStatus.uppercase()) {
                    "VERIFIED" -> "Verified on ${user.kycVerifiedAt?.toFormattedDate() ?: "N/A"}"
                    "PENDING" -> "Verification in progress"
                    "REJECTED" -> "Verification rejected"
                    else -> "Complete verification to unlock all features"
                },
                icon = Icons.Default.VerifiedUser,
                onClick = onKycClick,
                iconTint = when (user.kycStatus.uppercase()) {
                    "VERIFIED" -> FundroGreen
                    "REJECTED" -> MaterialTheme.colorScheme.error
                    else -> FundroTextSecondary
                },
                trailingContent = {
                    KycStatusBadge(status = user.kycStatus)
                },
                showChevron = false
            )

            Divider(
                modifier = Modifier.padding(start = 54.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            ProfileMenuItem(
                title = "Security",
                subtitle = "Password and authentication",
                icon = Icons.Default.Security,
                onClick = onSecurityClick
            )

            Divider(
                modifier = Modifier.padding(start = 54.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            ProfileMenuItem(
                title = "Notifications",
                subtitle = "Manage notification preferences",
                icon = Icons.Default.Notifications,
                onClick = { /* TODO: Navigate to notification settings */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Support section
        ProfileSection(title = "Support") {
            ProfileMenuItem(
                title = "Help & FAQ",
                subtitle = "Get help with Fundro",
                icon = Icons.Default.HelpOutline,
                onClick = onHelpClick,
                iconTint = MaterialTheme.colorScheme.tertiary
            )

            Divider(
                modifier = Modifier.padding(start = 54.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            ProfileMenuItem(
                title = "About Fundro",
                subtitle = "Version 1.0.0",
                icon = Icons.Default.Info,
                onClick = { /* TODO: Show about screen */ },
                iconTint = FundroTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Logout section
        ProfileSection(title = "") {
            ProfileMenuItem(
                title = "Log Out",
                icon = Icons.Default.Logout,
                onClick = onLogoutClick,
                iconTint = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.error,
                showChevron = false
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileHeader(
    user: com.basebox.fundro.domain.model.User,
    onEditProfile: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                ProfileAvatar(
                    name = user.fullName,
                    size = 96.dp,
                    fontSize = 36.sp
                )

                // Edit button
                Surface(
                    modifier = Modifier.size(28.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = onEditProfile
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Full name
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Username
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = FundroTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // KYC Status badge
            KycStatusBadge(status = user.kycStatus)
        }
    }
}

@Composable
fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = FundroTextSecondary,
                modifier = Modifier.padding(
                    horizontal = 4.dp,
                    vertical = 8.dp
                )
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Label and value
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = FundroTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Log Out",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(
                text = "Are you sure you want to log out of your Fundro account?",
                style = MaterialTheme.typography.bodyLarge,
                color = FundroTextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Log Out")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}