package com.basebox.fundro.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basebox.fundro.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class SplashDestination {
    object Onboarding : SplashDestination()
    object Login : SplashDestination()
    object Home : SplashDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            // Simulate splash delay
            delay(2000)

            val destination = when {
                // First time user - show onboarding
                !authRepository.isOnboardingCompleted() -> {
                    Timber.d("Navigating to Onboarding")
                    SplashDestination.Onboarding
                }

                // Returning user, not logged in - show login
                !authRepository.isLoggedIn() -> {
                    Timber.d("Navigating to Login")
                    SplashDestination.Login
                }

                // Logged in user - go to home
                else -> {
                    Timber.d("Navigating to Home")
                    SplashDestination.Home
                }
            }

            _destination.value = destination
        }
    }
}