package com.basebox.fundro.domain.usecase

import com.basebox.fundro.domain.repository.PaymentRepository
import javax.inject.Inject

class KycVerificationUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        bvn: String,
        accountNumber: String,
        bankCode: String,
        accountHolderName: String
    ) = paymentRepository.verifyKyc(bvn, accountNumber, bankCode, accountHolderName)
}
