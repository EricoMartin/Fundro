package com.basebox.fundro.ui.components.feedback

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.basebox.fundro.ui.theme.FundroGreen
import com.basebox.fundro.ui.theme.FundroOrange
import kotlinx.coroutines.delay
import timber.log.Timber

enum class FeedbackType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

data class FeedbackConfig(
    val type: FeedbackType,
    val title: String,
    val message: String,
    val primaryButtonText: String = "OK",
    val secondaryButtonText: String? = null,
    val autoDismissMs: Long? = null,
    val onPrimaryClick: (() -> Unit)? = null,
    val onSecondaryClick: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null,
    val onNavigate: ((NavController) -> Unit)? = null,
    var navController: NavController? = null
)

@Composable
fun FeedbackDialog(
    config: FeedbackConfig,
    onDismiss: () -> Unit,
    navController: NavController?
) {
    // Auto-dismiss logic
    LaunchedEffect(config.autoDismissMs) {
        config.autoDismissMs?.let { ms ->
            delay(ms)
            config.onDismiss?.invoke()
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = {
            config.onDismiss?.invoke()
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        FeedbackDialogContent(
            config = config,
            onDismiss = {
                config.onDismiss?.invoke()
                onDismiss()
            },
            navController = navController
        )
    }
}

@Composable
private fun FeedbackDialogContent(
    config: FeedbackConfig,
    onDismiss: () -> Unit,
    navController: NavController? = null
) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .scale(scale.value),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon with animation
                FeedbackIcon(type = config.type)

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = config.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Message
                Text(
                    text = config.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons
                if (config.secondaryButtonText != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Secondary button
                        OutlinedButton(
                            onClick = {
                                config.onSecondaryClick?.invoke()
                                onDismiss()
                                if (navController != null) {
                                    config.onNavigate?.invoke(navController)
                                } else {
                                    // Log an error if the controller is missing
                                    Timber.e("NavController not set in FeedbackManager, cannot navigate.")
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(config.secondaryButtonText)
                        }

                        // Primary button
                        Button(
                            onClick = {
                                config.onPrimaryClick?.invoke()
                                onDismiss()
                                if (navController != null) {
                                    config.onNavigate?.invoke(navController)
                                } else {
                                    // Log an error if the controller is missing
                                    Timber.e("NavController not set in FeedbackManager, cannot navigate.")
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = getColorForType(config.type)
                            )
                        ) {
                            Text(config.primaryButtonText)
                        }
                    }
                } else {
                    // Single button
                    Button(
                        onClick = {
                            config.onPrimaryClick?.invoke()
                            onDismiss()
                            if (navController != null) {
                                config.onNavigate?.invoke(navController)
                            } else {
                                // Log an error if the controller is missing
                                Timber.e("NavController not set in FeedbackManager, cannot navigate.")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getColorForType(config.type)
                        )
                    ) {
                        Text(
                            text = config.primaryButtonText,
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

@Composable
private fun FeedbackIcon(type: FeedbackType) {
    val icon = when (type) {
        FeedbackType.SUCCESS -> Icons.Default.CheckCircle
        FeedbackType.ERROR -> Icons.Default.Error
        FeedbackType.WARNING -> Icons.Default.Warning
        FeedbackType.INFO -> Icons.Default.Info
    }

    val color = getColorForType(type)
    val backgroundColor = color.copy(alpha = 0.15f)

    // Pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .scale(pulse)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.05f)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = color
        )
    }
}

@Composable
private fun getColorForType(type: FeedbackType): Color {
    return when (type) {
        FeedbackType.SUCCESS -> FundroGreen
        FeedbackType.ERROR -> MaterialTheme.colorScheme.error
        FeedbackType.WARNING -> FundroOrange
        FeedbackType.INFO -> MaterialTheme.colorScheme.primary
    }
}