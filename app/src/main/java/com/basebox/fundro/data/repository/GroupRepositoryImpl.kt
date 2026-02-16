package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.GroupApi
import com.basebox.fundro.domain.model.Group
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
                val groupsPage = response.body()!!
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

    override suspend fun getParticipatingGroups(page: Int, size: Int): Flow<ApiResult<List<Group>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupApi.getParticipatingGroups(page, size)

            if (response.isSuccessful && response.body() != null) {
                val groupsPage = response.body()!!
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
                val groupResponse = response.body()!!
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
                emit(ApiResult.Error("Failed to fetch group details"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}