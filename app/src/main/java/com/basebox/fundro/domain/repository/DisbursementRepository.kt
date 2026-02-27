package com.basebox.fundro.domain.repository

import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.Disbursement
import kotlinx.coroutines.flow.Flow

interface DisbursementRepository {
    suspend fun releaseFunds(groupId: String): Flow<ApiResult<Disbursement>>
}