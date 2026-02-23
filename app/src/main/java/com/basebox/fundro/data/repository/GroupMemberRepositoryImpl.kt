package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.local.dao.GroupDao
import com.basebox.fundro.data.local.dao.GroupMemberDao
import com.basebox.fundro.data.local.dto.toDomain
import com.basebox.fundro.data.local.dto.toEntity
import com.basebox.fundro.data.remote.api.GroupMemberApi
import com.basebox.fundro.data.remote.dto.response.getOrThrow
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.domain.repository.GroupMemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GroupMemberRepositoryImpl @Inject constructor(
    private val groupMemberApi: GroupMemberApi,
    private val groupMemberDao: GroupMemberDao,
    private val groupDao: GroupDao
) : GroupMemberRepository {

    override suspend fun getGroupMembers(groupId: String): Flow<ApiResult<List<GroupMember>>> = flow {
        emit(ApiResult.Loading)

        try {
            val cachedGroupMembers = groupMemberDao.getMembers(groupId)
            if (cachedGroupMembers.isNotEmpty()) {
                emit(ApiResult.Success(cachedGroupMembers.map { it.toDomain() }))
                Timber.d("📦 Emitted ${cachedGroupMembers.size} cached members")
            }

            val response = groupMemberApi.getGroupMembers(groupId)

            if (response.isSuccessful && response.body() != null) {
                val members = response.body()!!.getOrThrow().map { memberResponse ->
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

                val entities = members.map { it.toEntity(groupId) }
                groupMemberDao.deleteMembersByGroup(groupId)
                groupMemberDao.insertMembers(entities)
                Timber.d("💾 Saving ${members.size} members to cache...")

                emit(ApiResult.Success(members))
                Timber.d("Fetched ${members.size} members for group $groupId")
            } else {
                val errorMessage = "Failed to fetch members: ${response.message()}"
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Get members failed: $errorMessage")
            }
        } catch (e: Exception) {
            val cachedGroupMembers = groupMemberDao.getMembers(groupId)
            if (cachedGroupMembers.isNotEmpty()) {
                emit(ApiResult.Success(cachedGroupMembers.map { it.toDomain()}))
                Timber.d("📦 Using cached data due to network error")
            } else {
                emit(ApiResult.Error("Network error: ${e.localizedMessage}"))

                val errorMessage = "Network error: ${e.localizedMessage}"
                emit(ApiResult.Error(errorMessage))
                Timber.e(e, "Get members error")
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun acceptGroupMembership(groupId: String, userId: String): Flow<ApiResult<GroupMember>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupMemberApi.acceptGroupMembership(groupId, userId)

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

                val acceptedCachedMember = groupMemberDao.getMemberById(member.id)
                if (acceptedCachedMember != null) {
                    Timber.d("📦 User already accepted invitation")
                } else {
                    groupMemberDao.insertMember(member.toEntity(groupId))
                    Timber.d("💾 Saving accepted member to cache: ${member.fullName}")
                    emit(ApiResult.Success(member))
                }

                emit(ApiResult.Success(member))
                Timber.d("Joined group successfully")
            } else {
                emit(ApiResult.Error("Failed to join group"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}