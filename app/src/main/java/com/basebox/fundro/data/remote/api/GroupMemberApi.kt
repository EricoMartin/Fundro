package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.response.ApiResponse
import com.basebox.fundro.data.remote.dto.response.GroupMemberResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupMemberApi {

    @GET("groups/{groupId}/members")
    suspend fun getGroupMembers(
        @Path("groupId") groupId: String
    ): Response<ApiResponse<List<GroupMemberResponse>>>
}