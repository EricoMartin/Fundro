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

    suspend fun getCompletedGroups(page: Int = 0, size: Int = 20): Flow<ApiResult<List<Group>>>

    suspend fun getInvitedGroups(page: Int = 0, size: Int = 20): Flow<ApiResult<List<Group>>>
    suspend fun joinGroup(groupId: String): Flow<ApiResult<GroupMember>>
    suspend fun createGroup(
        name: String,
        description: String?,
        targetAmount: Double,
        deadline: String?,
        visibility: String = "PRIVATE",
        category: String?,
        tags: String?,
        maxMembers: Int?,
        generateJoinCode: Boolean = false
    ): Flow<ApiResult<Group>>

    suspend fun addMembersToGroup(
        groupId: String,
        userIds: List<String>,
        expectedAmount: Double?
    ): Flow<ApiResult<List<GroupMember>>>
}