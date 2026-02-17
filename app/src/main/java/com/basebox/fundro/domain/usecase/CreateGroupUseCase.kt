package com.basebox.fundro.domain.usecase

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.Group
import com.basebox.fundro.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String?,
        targetAmount: Double,
        deadline: String?,
        visibility: String = "PRIVATE",
        category: String?,
        tags: String? = null,
        maxMembers: Int? = null,
        generateJoinCode: Boolean = false
    ): Flow<ApiResult<Group>> {
        return groupRepository.createGroup(
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
    }
}