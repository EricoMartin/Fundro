package com.basebox.fundro.domain.usecase
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.domain.model.Disbursement
import com.basebox.fundro.domain.repository.DisbursementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReleaseFundsUseCase @Inject constructor(
    private val disbursementRepository: DisbursementRepository
) {
    suspend operator fun invoke(groupId: String): Flow<ApiResult<Disbursement>> {
        return disbursementRepository.releaseFunds(groupId)
    }
}