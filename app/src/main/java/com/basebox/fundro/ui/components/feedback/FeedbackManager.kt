package com.basebox.fundro.ui.components.feedback

import androidx.compose.runtime.*

class FeedbackManager {
    private val _dialogState = mutableStateOf<FeedbackConfig?>(null)
    val dialogState: State<FeedbackConfig?> = _dialogState

    fun showSuccess(
        title: String,
        message: String,
        buttonText: String = "OK",
        autoDismiss: Boolean = false,
        onDismiss: () -> Unit = {}
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.SUCCESS,
            title = title,
            message = message,
            primaryButtonText = buttonText,
            autoDismissMs = if (autoDismiss) 3000L else null,
            onDismiss = {
                onDismiss()
                _dialogState.value = null
            }
        )
    }

    fun showError(
        title: String = "Error",
        message: String,
        buttonText: String = "OK",
        onRetry: (() -> Unit)? = null,
        onDismiss: () -> Unit = {}
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.ERROR,
            title = title,
            message = message,
            primaryButtonText = buttonText,
            secondaryButtonText = if (onRetry != null) "Retry" else null,
            onPrimaryClick = {
                onDismiss()
                _dialogState.value = null
            },
            onSecondaryClick = onRetry,
            onDismiss = {
                onDismiss()
                _dialogState.value = null
            }
        )
    }

    fun showWarning(
        title: String,
        message: String,
        confirmText: String = "Continue",
        cancelText: String = "Cancel",
        onConfirm: () -> Unit,
        onCancel: () -> Unit = {}
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.WARNING,
            title = title,
            message = message,
            primaryButtonText = confirmText,
            secondaryButtonText = cancelText,
            onPrimaryClick = {
                onConfirm()
                _dialogState.value = null
            },
            onSecondaryClick = {
                onCancel()
                _dialogState.value = null
            },
            onDismiss = {
                onCancel()
                _dialogState.value = null
            }
        )
    }

    fun dismiss() {
        _dialogState.value = null
    }
}

// Composition local for providing feedback manager
val LocalFeedbackManager = staticCompositionLocalOf<FeedbackManager> {
    error("No FeedbackManager provided")
}

@Composable
fun ProvideFeedbackManager(content: @Composable () -> Unit) {
    val feedbackManager = remember { FeedbackManager() }

    CompositionLocalProvider(LocalFeedbackManager provides feedbackManager) {
        content()

        // Show dialog if there's a config
        feedbackManager.dialogState.value?.let { config ->
            FeedbackDialog(
                config = config,
                onDismiss = { feedbackManager.dismiss() }
            )
        }
    }
}