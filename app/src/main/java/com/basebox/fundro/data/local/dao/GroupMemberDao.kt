package com.basebox.fundro.data.local.dao

import androidx.room.*
import com.basebox.fundro.data.local.entity.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupMemberDao {

    @Query("SELECT * FROM group_members WHERE group_id = :groupId")
    fun getMembersFlow(groupId: String): Flow<List<GroupMemberEntity>>

    @Query("SELECT * FROM group_members WHERE group_id = :groupId")
    suspend fun getMembers(groupId: String): List<GroupMemberEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: GroupMemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<GroupMemberEntity>)

    @Query("DELETE FROM group_members WHERE group_id = :groupId")
    suspend fun deleteMembersByGroup(groupId: String)

    @Query("DELETE FROM group_members")
    suspend fun deleteAll()
}