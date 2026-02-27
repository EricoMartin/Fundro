package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.response.DisbursementResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface DisbursementApi {

    @POST("disbursements/groups/{groupId}/release")
    suspend fun releaseFunds(
        @Path("groupId") groupId: String
    ): Response<DisbursementResponse>
}