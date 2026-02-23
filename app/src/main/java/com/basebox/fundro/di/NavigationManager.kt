package com.basebox.fundro.di

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

// Represents a one-time navigation event
sealed class NavigationEvent {
    data class NavigateTo(val route: String) : NavigationEvent()
    data class NavigateAndPopUpTo(val route: String, val popUpTo: String, val inclusive: Boolean = false) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}

@Singleton
class NavigationManager @Inject constructor() {
    private val _events = MutableSharedFlow<NavigationEvent>()
    val events = _events.asSharedFlow() // Expose as read-only

    suspend fun navigate(event: NavigationEvent) {
        _events.emit(event)
    }
}
