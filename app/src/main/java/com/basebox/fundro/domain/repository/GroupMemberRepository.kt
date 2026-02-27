package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.GroupMember
import kotlinx.coroutines.flow.Flow

interface GroupMemberRepository {
    suspend fun getGroupMembers(groupId: String): Flow<ApiResult<List<GroupMember>>>
    suspend fun acceptGroupMembership(groupId: String, userId: String): Flow<ApiResult<GroupMember>>
    suspend fun declineGroupMembership(groupId: String, userId: String): Flow<ApiResult<GroupMember>>

}