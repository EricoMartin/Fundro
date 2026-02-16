package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Combines both owned and participating groups
 */
class GetUserGroupsUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(): Flow<ApiResult<Pair<List<Group>, List<Group>>>> {
        return combine(
            groupRepository.getMyGroups(),
            groupRepository.getParticipatingGroups()
        ) { myGroupsResult, participatingResult ->
            when {
                myGroupsResult is ApiResult.Loading || participatingResult is ApiResult.Loading -> {
                    ApiResult.Loading
                }

                myGroupsResult is ApiResult.Error -> myGroupsResult
                participatingResult is ApiResult.Error -> participatingResult

                myGroupsResult is ApiResult.Success && participatingResult is ApiResult.Success -> {
                    ApiResult.Success(Pair(myGroupsResult.data, participatingResult.data))
                }

                else -> ApiResult.Error("Unknown error")
            }
        }
    }
}