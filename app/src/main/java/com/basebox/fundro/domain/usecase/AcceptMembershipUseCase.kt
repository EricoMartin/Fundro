package com.basebox.fundro.domain.usecase

import com.basebox.fundro.domain.repository.GroupMemberRepository
import javax.inject.Inject

class AcceptMembershipUseCase @Inject constructor(
    private val groupMemberRepository: GroupMemberRepository
)  {
    suspend operator fun invoke(groupId: String, userId: String)
    = groupMemberRepository.acceptGroupMembership(groupId, userId)

}