package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.SearchUser
import com.basebox.fundro.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(query: String): Flow<ApiResult<List<SearchUser>>> {
        return userRepository.searchUsers(query)
    }
}