package com.basebox.fundro.ui.group.create

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.basebox.fundro.ui.components.*
import com.basebox.fundro.ui.theme.FundroTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    navController: NavController,
    viewModel: CreateGroupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()
    val focusManager = LocalFocusManager.current

    // Navigate to group detail on success
    LaunchedEffect(createSuccess) {
        createSuccess?.let { groupId ->
            navController.navigate("group/$groupId") {
                popUpTo("home") { inclusive = false }
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
                            text = "Create Pool",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Step ${uiState.currentStep} of 2",
                            style = MaterialTheme.typography.bodySmall,
                            color = FundroTextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (uiState.currentStep > 1) {
                                viewModel.previousStep()
                            } else {
                                navController.popBackStack()
                            }
                        }
                    ) {
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
            // Progress indicator
            LinearProgressIndicator(
                progress = uiState.currentStep / 2f,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                when (uiState.currentStep) {
                    1 -> Step1BasicInfo(
                        uiState = uiState,
                        onNameChanged = viewModel::onNameChanged,
                        onDescriptionChanged = viewModel::onDescriptionChanged,
                        onTargetAmountChanged = viewModel::onTargetAmountChanged,
                        onCategoryChanged = viewModel::onCategoryChanged,
                        focusManager = focusManager
                    )

                    2 -> Step2AdvancedSettings(
                        uiState = uiState,
                        onDeadlineChanged = viewModel::onDeadlineChanged,
                        onVisibilityChanged = viewModel::onVisibilityChanged,
                        onMaxMembersChanged = viewModel::onMaxMembersChanged,
                        onGenerateJoinCodeChanged = viewModel::onGenerateJoinCodeChanged,
                        focusManager = focusManager
                    )
                }
            }

            // Bottom action button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = viewModel::nextStep,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (uiState.currentStep == 1) "Continue" else "Create Pool",
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step1BasicInfo(
    uiState: CreateGroupUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onTargetAmountChanged: (String) -> Unit,
    onCategoryChanged: (String?) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column {
        // Header
        Text(
            text = "Basic Information",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Let's start with the basics about your pool",
            style = MaterialTheme.typography.bodyMedium,
            color = FundroTextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Pool Name
        FormTextField(
            value = uiState.name,
            onValueChange = onNameChanged,
            label = "Pool Name",
            placeholder = "e.g., Weekend Trip to Lagos",
            errorMessage = uiState.nameError,
            imeAction = ImeAction.Next,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Description
        FormTextField(
            value = uiState.description,
            onValueChange = onDescriptionChanged,
            label = "Description (Optional)",
            placeholder = "What is this pool for?",
            errorMessage = uiState.descriptionError,
            singleLine = false,
            maxLines = 4,
            imeAction = ImeAction.Next,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Target Amount
        FormTextField(
            value = uiState.targetAmount,
            onValueChange = onTargetAmountChanged,
            label = "Target Amount",
            placeholder = "0.00",
            errorMessage = uiState.targetAmountError,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done,
            leadingIcon = {
                Text(
                    text = "₦",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Category Selection
        Text(
            text = "Category (Optional)",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            CategoryChip(
                text = "Event",
                selected = uiState.category == "EVENT",
                onClick = {
                    onCategoryChanged(if (uiState.category == "EVENT") null else "EVENT")
                },
                icon = Icons.Default.Event
            )

            CategoryChip(
                text = "Gift",
                selected = uiState.category == "GIFT",
                onClick = {
                    onCategoryChanged(if (uiState.category == "GIFT") null else "GIFT")
                },
                icon = Icons.Default.CardGiftcard
            )

            CategoryChip(
                text = "Subscription",
                selected = uiState.category == "SUBSCRIPTION",
                onClick = {
                    onCategoryChanged(if (uiState.category == "SUBSCRIPTION") null else "SUBSCRIPTION")
                },
                icon = Icons.Default.Subscriptions
            )

            CategoryChip(
                text = "Campaign",
                selected = uiState.category == "CAMPAIGN",
                onClick = {
                    onCategoryChanged(if (uiState.category == "CAMPAIGN") null else "CAMPAIGN")
                },
                icon = Icons.Default.Campaign
            )

            CategoryChip(
                text = "General",
                selected = uiState.category == "GENERAL",
                onClick = {
                    onCategoryChanged(if (uiState.category == "GENERAL") null else "GENERAL")
                },
                icon = Icons.Default.Folder
            )
        }
    }
}

@Composable
fun Step2AdvancedSettings(
    uiState: CreateGroupUiState,
    onDeadlineChanged: (java.time.LocalDate?) -> Unit,
    onVisibilityChanged: (String) -> Unit,
    onMaxMembersChanged: (String) -> Unit,
    onGenerateJoinCodeChanged: (Boolean) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column {
        // Header
        Text(
            text = "Advanced Settings",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Configure additional options for your pool",
            style = MaterialTheme.typography.bodyMedium,
            color = FundroTextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Deadline
        DatePickerField(
            value = uiState.deadline,
            onValueChange = onDeadlineChanged,
            label = "Deadline (Optional)",
            errorMessage = uiState.deadlineError
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Max Members
        FormTextField(
            value = uiState.maxMembers,
            onValueChange = onMaxMembersChanged,
            label = "Maximum Members (Optional)",
            placeholder = "Unlimited",
            errorMessage = uiState.maxMembersError,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Visibility
        Text(
            text = "Who can see this pool?",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        GroupVisibility.values().forEach { visibility ->
            VisibilityOption(
                title = visibility.displayName,
                description = when (visibility) {
                    GroupVisibility.PRIVATE -> "Only invited members can see and join"
                    GroupVisibility.PUBLIC -> "Anyone can discover and join this pool"
                    GroupVisibility.UNLISTED -> "Anyone with the link can join"
                },
                selected = uiState.visibility == visibility.name,
                onClick = { onVisibilityChanged(visibility.name) }
            )

            if (visibility != GroupVisibility.values().last()) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Join Code Toggle
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Generate Join Code",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Create a unique code for easy sharing",
                        style = MaterialTheme.typography.bodySmall,
                        color = FundroTextSecondary
                    )
                }

                Switch(
                    checked = uiState.generateJoinCode,
                    onCheckedChange = onGenerateJoinCodeChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}

@Composable
fun VisibilityOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (selected) {
            null
        } else {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        },
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = FundroTextSecondary
                )
            }
        }
    }
}