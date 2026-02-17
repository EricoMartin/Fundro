package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.SearchUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun searchUsers(
        query: String,
        page: Int = 0,
        size: Int = 20
    ): Flow<ApiResult<List<SearchUser>>>
}