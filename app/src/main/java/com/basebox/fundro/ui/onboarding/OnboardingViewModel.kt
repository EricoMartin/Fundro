package com.basebox.fundro.ui.onboarding

import androidx.lifecycle.ViewModel
import com.basebox.fundro.core.security.SecureStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val secureStorage: SecureStorage
) : ViewModel() {

    fun completeOnboarding() {
        secureStorage.setOnboardingCompleted(true)
        Timber.d("Onboarding completed")
    }
}