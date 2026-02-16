package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.model.GroupMember
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun getMyGroups(page: Int = 0, size: Int = 20): Flow<ApiResult<List<Group>>>
    suspend fun getParticipatingGroups(page: Int = 0, size: Int = 20): Flow<ApiResult<List<Group>>>
    suspend fun getGroupById(groupId: String): Flow<ApiResult<Group>>
    suspend fun getGroupMembers(groupId: String): Flow<ApiResult<List<GroupMember>>>
}