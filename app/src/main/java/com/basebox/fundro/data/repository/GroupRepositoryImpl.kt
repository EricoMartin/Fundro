package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.GroupApi
import com.basebox.fundro.data.remote.dto.request.AddMembersRequest
import com.basebox.fundro.data.remote.dto.request.CreateGroupRequest
import com.basebox.fundro.data.remote.dto.response.getOrThrow
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.domain.model.Owner
import com.basebox.fundro.domain.repository.GroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupApi: GroupApi
) : GroupRepository {

    override suspend fun getMyGroups(page: Int, size: Int): Flow<ApiResult<List<Group>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.getMyGroups(page, size)

            if (response.isSuccessful && response.body() != null) {
                val groupsPage = response.body()!!.groups
                val groups = groupsPage.groups.map { groupResponse ->
                    Group(
                        id = groupResponse.id,
                        name = groupResponse.name,
                        description = groupResponse.description,
                        targetAmount = groupResponse.targetAmount,
                        status = groupResponse.status,
                        visibility = groupResponse.visibility ?: "PRIVATE",
                        category = groupResponse.category,
                        deadline = groupResponse.deadline,
                        createdAt = groupResponse.createdAt,
                        owner = Owner(
                            id = groupResponse.owner.id,
                            username = groupResponse.owner.username,
                            fullName = groupResponse.owner.fullName,
                            email = groupResponse.owner.email
                        ),
                        totalCollected = groupResponse.totalCollected,
                        participantCount = groupResponse.participantCount,
                        progressPercentage = groupResponse.progressPercentage,
                        hasCurrentUserContributed = groupResponse.hasCurrentUserContributed
                    )
                }

                emit(ApiResult.Success(groups))
                Timber.d("Fetched ${groups.size} groups")
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Session expired. Please login again."
                    else -> "Failed to fetch groups: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Get groups failed: $errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Network error: ${e.localizedMessage}"
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Get groups error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun joinGroup(groupId: String): Flow<ApiResult<GroupMember>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.joinGroup(groupId)

            if (response.isSuccessful && response.body() != null) {
                val memberResponse = response.body()!!.getOrThrow()
                val member = GroupMember(
                    id = memberResponse.id,
                    userId = memberResponse.userId,
                    username = memberResponse.username,
                    fullName = memberResponse.fullName,
                    status = memberResponse.status,
                    expectedAmount = memberResponse.expectedAmount,
                    paidAmount = memberResponse.paidAmount,
                    invitedAt = memberResponse.invitedAt,
                    joinedAt = memberResponse.joinedAt,
                    paidAt = memberResponse.paidAt
                )

                emit(ApiResult.Success(member))
                Timber.d("Joined group successfully")
            } else {
                emit(ApiResult.Error("Failed to join group"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getParticipatingGroups(page: Int, size: Int): Flow<ApiResult<List<Group>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.getParticipatingGroups(page, size)

            if (response.isSuccessful && response.body() != null) {
                val groupsPage = response.body()!!.groups
                val groups = groupsPage.groups.map { groupResponse ->
                    Group(
                        id = groupResponse.id,
                        name = groupResponse.name,
                        description = groupResponse.description,
                        targetAmount = groupResponse.targetAmount,
                        status = groupResponse.status,
                        visibility = groupResponse.visibility ?: "PRIVATE",
                        category = groupResponse.category,
                        deadline = groupResponse.deadline,
                        createdAt = groupResponse.createdAt,
                        owner = Owner(
                            id = groupResponse.owner.id,
                            username = groupResponse.owner.username,
                            fullName = groupResponse.owner.fullName,
                            email = groupResponse.owner.email
                        ),
                        totalCollected = groupResponse.totalCollected,
                        participantCount = groupResponse.participantCount,
                        progressPercentage = groupResponse.progressPercentage,
                        hasCurrentUserContributed = groupResponse.hasCurrentUserContributed
                    )
                }

                emit(ApiResult.Success(groups))
            } else {
                emit(ApiResult.Error("Failed to fetch participating groups"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getGroupById(groupId: String): Flow<ApiResult<Group>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.getGroupById(groupId)

            if (response.isSuccessful && response.body() != null) {
                val groupResponse = response.body()!!.group
                val group = Group(
                    id = groupResponse.id,
                    name = groupResponse.name,
                    description = groupResponse.description,
                    targetAmount = groupResponse.targetAmount,
                    status = groupResponse.status,
                    visibility = groupResponse.visibility ?: "PRIVATE",
                    category = groupResponse.category,
                    deadline = groupResponse.deadline,
                    createdAt = groupResponse.createdAt,
                    owner = Owner(
                        id = groupResponse.owner.id,
                        username = groupResponse.owner.username,
                        fullName = groupResponse.owner.fullName,
                        email = groupResponse.owner.email
                    ),
                    totalCollected = groupResponse.totalCollected,
                    participantCount = groupResponse.participantCount,
                    progressPercentage = groupResponse.progressPercentage,
                    hasCurrentUserContributed = groupResponse.hasCurrentUserContributed
                )

                emit(ApiResult.Success(group))
            } else {
                emit(ApiResult.Error("Failed to fetch group details: ${response.body()!!.message}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getGroupMembers(groupId: String): Flow<ApiResult<List<GroupMember>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.getGroupMembers(groupId)

            if (response.isSuccessful && response.body() != null) {
                val membersResponse = response.body()!!.getOrThrow()
                val members = membersResponse.map { memberResponse ->
                    GroupMember(
                        id = memberResponse.id,
                        userId = memberResponse.userId,
                        username = memberResponse.username,
                        fullName = memberResponse.fullName,
                        status = memberResponse.status,
                        expectedAmount = memberResponse.expectedAmount,
                        paidAmount = memberResponse.paidAmount,
                        invitedAt = memberResponse.invitedAt,
                        joinedAt = memberResponse.joinedAt,
                        paidAt = memberResponse.paidAt
                    )
                }

                emit(ApiResult.Success(members))
                Timber.d("Fetched ${members.size} members for group $groupId")
            } else {
                emit(ApiResult.Error("Failed to fetch group members"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getInvitedGroups(page: Int, size: Int): Flow<ApiResult<List<Group>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.getInvitedGroups(page, size)

            if (response.isSuccessful && response.body() != null) {
                val groupsPage = response.body()!!
                val groups = groupsPage.groups.map {
                /* map to Group */
                    Group(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        targetAmount = it.targetAmount,
                        status = it.status,
                        visibility = it.visibility ?: "PRIVATE",
                        category = it.category,
                        deadline = it.deadline,
                        createdAt = it.createdAt,
                        owner = Owner(
                            id = it.owner.id,
                            username = it.owner.username,
                            fullName = it.owner.fullName,
                            email = it.owner.email
                        ),
                        totalCollected = it.totalCollected,
                        participantCount = it.participantCount,
                        progressPercentage = it.progressPercentage,
                        hasCurrentUserContributed = it.hasCurrentUserContributed
                    )
                }
                emit(ApiResult.Success(groups))
            } else {
                emit(ApiResult.Error("Failed to fetch invited groups"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createGroup(
        name: String,
        description: String?,
        targetAmount: Double,
        deadline: String?,
        visibility: String,
        category: String?,
        tags: String?,
        maxMembers: Int?,
        generateJoinCode: Boolean
    ): Flow<ApiResult<Group>> = flow {
        emit(ApiResult.Loading)

        try {
            val request = CreateGroupRequest(
                name = name,
                description = description,
                targetAmount = targetAmount,
                deadline = deadline,
                visibility = visibility,
                category = category,
                tags = tags,
                maxMembers = maxMembers,
                generateJoinCode = generateJoinCode
            )

            val response = groupApi.createGroup(request)

            if (response.isSuccessful && response.body() != null) {
                val groupResponse = response.body()!!.group
                val group = Group(
                    id = groupResponse.id,
                    name = groupResponse.name,
                    description = groupResponse.description,
                    targetAmount = groupResponse.targetAmount,
                    status = groupResponse.status,
                    visibility = groupResponse.visibility ?: "PRIVATE",
                    category = groupResponse.category,
                    deadline = groupResponse.deadline,
                    createdAt = groupResponse.createdAt,
                    owner = Owner(
                        id = groupResponse.owner.id,
                        username = groupResponse.owner.username,
                        fullName = groupResponse.owner.fullName,
                        email = groupResponse.owner.email
                    ),
                    totalCollected = groupResponse.totalCollected,
                    participantCount = groupResponse.participantCount,
                    progressPercentage = groupResponse.progressPercentage,
                    hasCurrentUserContributed = groupResponse.hasCurrentUserContributed
                )

                emit(ApiResult.Success(group))
                Timber.d("Group created successfully: ${group.name}")
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid request: Complete KYC Verification first."
                    401 -> "Session expired. Please login again."
                    else -> "Failed to create group: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Create group failed: $errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Network error: ${e.localizedMessage}"
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Create group error")
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addMembersToGroup(
        groupId: String,
        userIds: List<String>,
        expectedAmount: Double?
    ): Flow<ApiResult<List<GroupMember>>> = flow {
        emit(ApiResult.Loading)

        try {
            val request = AddMembersRequest(
                userIds = userIds,
                expectedAmount = expectedAmount
            )

            val response = groupApi.addMembers(groupId, request)

            if (response.isSuccessful && response.body() != null) {
                val membersResponse = response.body()!!.getOrThrow()
                val members = membersResponse.map { memberResponse ->
                    GroupMember(
                        id = memberResponse.id,
                        userId = memberResponse.userId,
                        username = memberResponse.username,
                        fullName = memberResponse.fullName,
                        status = memberResponse.status,
                        expectedAmount = memberResponse.expectedAmount,
                        paidAmount = memberResponse.paidAmount,
                        invitedAt = memberResponse.invitedAt,
                        joinedAt = memberResponse.joinedAt,
                        paidAt = memberResponse.paidAt
                    )
                }

                emit(ApiResult.Success(members))
                Timber.d("Added ${members.size} members to group $groupId")
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid member data"
                    403 -> "You don't have permission to add members"
                    404 -> "Group not found"
                    else -> "Failed to add members: ${response.message()}"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "Add members error")
        }
    }.flowOn(Dispatchers.IO)
}