package com.basebox.fundro.ui.components.notification

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.basebox.fundro.core.notification.model.FundroNotification

@Composable
public fun AnimatedNotificationDialog(
    notification: FundroNotification,
    config: NotificationDialogConfig,
    onDismiss: () -> Unit,
    onAction: (FundroNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    // Entry animation
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(200, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
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
                modifier = Modifier.fillMaxWidth()
            ) {
                // Gradient header
                NotificationDialogHeader(
                    icon = config.icon,
                    iconBackgroundColor = config.iconBackgroundColor,
                    gradientColors = config.gradientColors,
                    onDismiss = onDismiss
                )

                // Content
                NotificationDialogContent(
                    notification = notification,
                    config = config,
                    onDismiss = onDismiss,
                    onAction = onAction
                )
            }
        }
    }
}