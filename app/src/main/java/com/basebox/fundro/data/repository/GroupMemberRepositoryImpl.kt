package com.basebox.fundro.data.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.GroupMemberApi
import com.basebox.fundro.domain.model.GroupMember
import com.basebox.fundro.domain.repository.GroupMemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GroupMemberRepositoryImpl @Inject constructor(
    private val groupMemberApi: GroupMemberApi
) : GroupMemberRepository {

    override suspend fun getGroupMembers(groupId: String): Flow<ApiResult<List<GroupMember>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = groupMemberApi.getGroupMembers(groupId)

            if (response.isSuccessful && response.body() != null) {
                val members = response.body()!!.map { memberResponse ->
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
                val errorMessage = "Failed to fetch members: ${response.message()}"
                emit(ApiResult.Error(errorMessage, response.code()))
                Timber.e("Get members failed: $errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Network error: ${e.localizedMessage}"
            emit(ApiResult.Error(errorMessage))
            Timber.e(e, "Get members error")
        }
    }.flowOn(Dispatchers.IO)
}