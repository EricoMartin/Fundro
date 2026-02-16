package com.basebox.fundro.core.network

import com.basebox.fundro.core.security.SecureStorage
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

/**
 * Automatically adds JWT token to all API requests
 */
class AuthInterceptor @Inject constructor(
    private val secureStorage: SecureStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth for public endpoints
        val url = originalRequest.url.encodedPath
        if (url.contains("/auth/login") || url.contains("/auth/register") || url.contains("/kyc/banks")) {
            return chain.proceed(originalRequest)
        }

        // Add JWT token to header
        val token = secureStorage.getAccessToken()

        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        Timber.d("API Request: ${newRequest.method} ${newRequest.url}")

        return chain.proceed(newRequest)
    }
}