package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCompletedGroupsUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(): Flow<ApiResult<List<Group>>>
    = groupRepository.getCompletedGroups()
}