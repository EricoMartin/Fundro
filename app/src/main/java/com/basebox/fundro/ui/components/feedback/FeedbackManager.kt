package com.basebox.fundro.ui.components.feedback

import androidx.compose.runtime.*
import androidx.navigation.NavController

class FeedbackManager {
    private val _dialogState = mutableStateOf<FeedbackConfig?>(null)
    val dialogState: State<FeedbackConfig?> = _dialogState


    fun setNavController(navController: NavController) {
        _dialogState.value?.navController = navController
    }
    fun showSuccess(
        title: String,
        message: String,
        buttonText: String = "OK",
        autoDismiss: Boolean = false,
        onDismiss: (() -> Unit)? = null,
        onNavigate: ((NavController) -> Unit)? = null
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.SUCCESS,
            title = title,
            message = message,
            primaryButtonText = buttonText,
            autoDismissMs = if (autoDismiss) 3000L else null,
            onPrimaryClick = {
                _dialogState.value = null
                onDismiss?.invoke()
            },
            onDismiss = {
                onNavigate
                onDismiss?.invoke()
                _dialogState.value = null
            },
            onNavigate = onNavigate
        )
    }

    fun showError(
        title: String = "Error",
        message: String,
        buttonText: String = "OK",
        onRetry: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
        onNavigate: ((NavController) -> Unit)? = null
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.ERROR,
            title = title,
            message = message,
            primaryButtonText = buttonText,
            secondaryButtonText = if (onRetry != null) "Retry" else null,
            onPrimaryClick = {
                _dialogState.value = null
                onDismiss?.invoke()
            },
            onSecondaryClick = {
                _dialogState.value = null
                onRetry?.invoke()
            },
            onDismiss = {
                _dialogState.value = null
                onDismiss?.invoke()
            },
            onNavigate = onNavigate
        )
    }

    fun showWarning(
        title: String,
        message: String,
        confirmText: String = "Continue",
        cancelText: String = "Cancel",
        onConfirm: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null,
        onNavigate: ((NavController) -> Unit)?,
        onDismiss: (() -> Unit)? = null
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.WARNING,
            title = title,
            message = message,
            primaryButtonText = confirmText,
            secondaryButtonText = cancelText,
            onPrimaryClick = {
                _dialogState.value = null
                onConfirm?.invoke()
            },
            onSecondaryClick = {
                _dialogState.value = null
                onCancel?.invoke()
            },
            onDismiss = {
                _dialogState.value = null
                onCancel?.invoke()
            },
            onConfirm = {
//                onNavigate
                onConfirm?.invoke()
                _dialogState.value = null
            },
            onCancel = onCancel,
            onNavigate = {
                _dialogState.value = null
                onNavigate
            }
        )
    }

    fun showInfo(
        title: String,
        message: String,
        buttonText: String = "Got it",
        onDismiss: (() -> Unit)? = null
    ) {
        _dialogState.value = FeedbackConfig(
            type = FeedbackType.INFO,
            title = title,
            message = message,
            primaryButtonText = buttonText,
            onPrimaryClick = {
                _dialogState.value = null
                onDismiss?.invoke()
            },
            onDismiss = {
                _dialogState.value = null
                onDismiss?.invoke()
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
                onDismiss = { feedbackManager.dismiss() },
                navController = feedbackManager.dialogState.value?.navController
            )
        }
    }
}