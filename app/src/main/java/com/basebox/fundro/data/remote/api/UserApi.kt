package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.request.UpdateProfileRequest
import com.basebox.fundro.data.remote.dto.response.ApiResponse
import com.basebox.fundro.data.remote.dto.response.UserResponse
import com.basebox.fundro.data.remote.dto.response.UserSearchPageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApi {

    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<UserSearchPageResponse>

    @GET("users/me")
    suspend fun getCurrentUser(): Response<ApiResponse<UserResponse>>

    @PUT("users/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<UserResponse>>
}