package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.request.LoginRequest
import com.basebox.fundro.data.remote.dto.request.RegisterRequest
import com.basebox.fundro.data.remote.dto.response.AuthResponse
import com.basebox.fundro.data.remote.dto.response.RegisterResponse
import com.basebox.fundro.data.remote.dto.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("users/me")
    suspend fun getCurrentUser(): Response<UserResponse>
}