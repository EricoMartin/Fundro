package com.basebox.fundro.domain.usecase

import com.basebox.fundro.domain.repository.GroupMemberRepository
import javax.inject.Inject

class DeclineMembershipUseCase @Inject constructor(
    private val groupMemberRepository: GroupMemberRepository
)  {
    suspend operator fun invoke(groupId: String, userId: String)
            = groupMemberRepository.declineGroupMembership(groupId, userId)

}