package com.basebox.fundro.data.remote.api

import com.basebox.fundro.data.remote.dto.request.AddMemberRequest
import com.basebox.fundro.data.remote.dto.request.AddMembersRequest
import com.basebox.fundro.data.remote.dto.request.CreateGroupRequest
import com.basebox.fundro.data.remote.dto.response.ApiResponse
import com.basebox.fundro.data.remote.dto.response.GroupDataResponse
import com.basebox.fundro.data.remote.dto.response.GroupMemberResponse
import com.basebox.fundro.data.remote.dto.response.GroupResponse
import com.basebox.fundro.data.remote.dto.response.GroupsPageDataResponse
import com.basebox.fundro.data.remote.dto.response.GroupsPageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {

    @GET("groups/my-groups")
    suspend fun getMyGroups(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<GroupsPageDataResponse>

    @GET("groups/participating")
    suspend fun getParticipatingGroups(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<GroupsPageDataResponse>

    @GET("groups/{groupId}")
    suspend fun getGroupById(
        @Path("groupId") groupId: String
    ): Response<GroupDataResponse>

    @GET("groups/{groupId}/members")
    suspend fun getGroupMembers(
        @Path("groupId") groupId: String
    ): Response<ApiResponse<List<GroupMemberResponse>>>

    @POST("groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequest
    ): Response<GroupDataResponse>

    @POST("groups/{groupId}/members/add")
    suspend fun addMember(
        @Path("groupId") groupId: String,
        @Body request: AddMemberRequest
    ): Response<ApiResponse<GroupMemberResponse>>

    @POST("groups/{groupId}/members")
    suspend fun addMembers(
        @Path("groupId") groupId: String,
        @Body request: AddMembersRequest
    ): Response<ApiResponse<List<GroupMemberResponse>>>

    @POST("groups/{groupId}/join")
    suspend fun joinGroup(
        @Path("groupId") groupId: String
    ): Response<ApiResponse<GroupMemberResponse>>

    @GET("groups/invited")
    suspend fun getInvitedGroups(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<GroupsPageResponse>>
}