package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.UserApi
import com.basebox.fundro.domain.model.SearchUser
import com.basebox.fundro.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun searchUsers(query: String, page: Int, size: Int): Flow<ApiResult<List<SearchUser>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = userApi.searchUsers(query, page, size)

            if (response.isSuccessful && response.body() != null) {
                val searchResponse = response.body()!!
                val users = searchResponse.content.map { userResponse ->
                    SearchUser(
                        id = userResponse.id,
                        username = userResponse.username,
                        fullName = userResponse.fullName
                    )
                }

                emit(ApiResult.Success(users))
                Timber.d("Found ${users.size} users for query: $query")
            } else {
                emit(ApiResult.Error("Failed to search users"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "Search users error")
        }
    }.flowOn(Dispatchers.IO)
}