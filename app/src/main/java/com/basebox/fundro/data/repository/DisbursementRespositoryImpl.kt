package com.basebox.fundro.data.repository
import com.basebox.fundro.core.network.ApiResult
import com.basebox.fundro.data.remote.api.DisbursementApi
import com.basebox.fundro.domain.model.Disbursement
import com.basebox.fundro.domain.repository.DisbursementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class DisbursementRepositoryImpl @Inject constructor(
    private val disbursementApi: DisbursementApi
) : DisbursementRepository {

    override suspend fun releaseFunds(groupId: String): Flow<ApiResult<Disbursement>> = flow {
        emit(ApiResult.Loading)

        try {
            // Simulate processing delay (3 seconds)
            delay(3000)

            val response = disbursementApi.releaseFunds(groupId)

            if (response.isSuccessful && response.body() != null) {
                val disbursementResponse = response.body()!!

                val disbursement = Disbursement(
                    disbursementId = disbursementResponse.disbursementId,
                    groupId = disbursementResponse.groupId,
                    groupName = disbursementResponse.groupName,
                    amount = disbursementResponse.amount,
                    recipientName = disbursementResponse.recipientName,
                    recipientAccount = disbursementResponse.recipientAccount,
                    disbursedAt = disbursementResponse.disbursedAt,
                    status = disbursementResponse.status,
                    message = disbursementResponse.message
                )

                emit(ApiResult.Success(disbursement))
                Timber.d("✅ Funds released successfully")
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Group is not fully funded"
                    403 -> "Only group owner can release funds"
                    409 -> "Funds already released"
                    else -> "Failed to release funds"
                }
                emit(ApiResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
            Timber.e(e, "Release funds error")
        }
    }.flowOn(Dispatchers.IO)
}