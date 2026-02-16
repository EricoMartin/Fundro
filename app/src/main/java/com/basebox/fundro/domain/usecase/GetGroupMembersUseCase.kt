package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.domain.repository.GroupMemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupMembersUseCase @Inject constructor(
    private val groupMemberRepository: GroupMemberRepository
) {
    suspend operator fun invoke(groupId: String): Flow<ApiResult<List<GroupMember>>> {
        return groupMemberRepository.getGroupMembers(groupId)
    }
}