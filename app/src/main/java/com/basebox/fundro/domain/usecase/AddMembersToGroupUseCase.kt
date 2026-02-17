package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddMembersToGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(
        groupId: String,
        userIds: List<String>,
        expectedAmount: Double?
    ): Flow<ApiResult<List<GroupMember>>> {
        return groupRepository.addMembersToGroup(groupId, userIds, expectedAmount)
    }
}