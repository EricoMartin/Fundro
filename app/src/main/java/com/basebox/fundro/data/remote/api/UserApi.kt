package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.response.UserSearchPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<UserSearchPageResponse>
}