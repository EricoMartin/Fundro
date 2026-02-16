package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.response.GroupResponse
import com.basebox.fundro.data.remote.dto.response.GroupsPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {

    @GET("groups/my-groups")
    suspend fun getMyGroups(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<GroupsPageResponse>

    @GET("groups/participating")
    suspend fun getParticipatingGroups(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<GroupsPageResponse>

    @GET("groups/{groupId}")
    suspend fun getGroupById(
        @Path("groupId") groupId: String
    ): Response<GroupResponse>
}