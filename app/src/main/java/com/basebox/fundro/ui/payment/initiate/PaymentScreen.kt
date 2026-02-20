package com.basebox.fundro.ui.payment.initiate

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.basebox.fundro.core.payment.PaymentResult
import com.basebox.fundro.core.payment.PaystackHelper
import com.basebox.fundro.core.security.SecureStorage
import com.basebox.fundro.ui.components.*
import com.basebox.fundro.ui.theme.FundroTextSecondary
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    groupId: String,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val paymentInitiation by viewModel.paymentInitiation.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    // Get user email for Paystack
    val secureStorage = remember { SecureStorage(context) }
    val userEmail = remember { secureStorage.getUserEmail() ?: "user@fundro.app" }

    // Handle payment initiation
    LaunchedEffect(paymentInitiation) {
        paymentInitiation?.let { payment ->
            if (activity != null) {
                val amountInKobo = (payment.amount * 100).toInt()

                Timber.d("Charging card: ₦${payment.amount} (${amountInKobo} kobo)")

                PaystackHelper.chargeCard(
                    activity = activity,
                    email = userEmail,
                    amount = amountInKobo,
                    accessCode = payment.accessCode
                ) { result ->
                    when (result) {
                        is PaymentResult.Success -> {
                            Timber.d("Payment successful: ${result.reference}")
                            // Navigate to verification screen
                            navController.navigate("payment/${payment.contributionId}/verify") {
                                popUpTo("payment/$groupId") { inclusive = true }
                            }
                        }

                        is PaymentResult.Error -> {
                            Timber.e("Payment failed: ${result.message}")
                            // Show error
                        }

                        is PaymentResult.Cancelled -> {
                            Timber.d("Payment cancelled")
                        }
                    }
                }
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
                    Text(
                        text = "Make Payment",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // GroupEntity info
                uiState.group?.let { group ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Contributing to",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Amount input
                    AmountInputCard(
                        amount = uiState.amount,
                        onAmountChange = viewModel::onAmountChanged,
                        label = "How much would you like to contribute?",
                        error = uiState.amountError,
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick amount suggestions
                    Text(
                        text = "Suggested amounts",
                        style = MaterialTheme.typography.labelMedium,
                        color = FundroTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val suggestions = listOf(5000.0, 10000.0, 20000.0)
                        suggestions.forEach { amount ->
                            FilterChip(
                                selected = uiState.amount == amount.toString(),
                                onClick = { viewModel.onAmountChanged(amount.toString()) },
                                label = { Text("₦${amount.toInt()}") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Payment summary
                    if (uiState.amount.isNotBlank() && uiState.amount.toDoubleOrNull() != null) {
                        PaymentSummaryCard(
                            groupName = group.name,
                            amount = uiState.amount.toDouble()
                        )
                    }
                }
            }

            // Pay button
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
                        onClick = viewModel::initiatePayment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading && uiState.amount.isNotBlank(),
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
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.amount.isNotBlank() && uiState.amount.toDoubleOrNull() != null) {
                                    "Pay ₦${uiState.amount}"
                                } else {
                                    "Enter Amount to Continue"
                                },
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