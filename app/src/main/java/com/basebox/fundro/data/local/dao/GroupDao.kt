package com.basebox.fundro.data.local.dao

import androidx.room.*
import com.basebox.fundro.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM `groups` ORDER BY created_at DESC")
    fun getAllGroupsFlow(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `groups` WHERE group_type = :type ORDER BY created_at DESC")
    fun getGroupsByTypeFlow(type: String): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `groups` WHERE id = :groupId LIMIT 1")
    fun getGroupFlow(groupId: String): Flow<GroupEntity?>

    @Query("SELECT * FROM `groups` WHERE id = :groupId LIMIT 1")
    suspend fun getGroup(groupId: String): GroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(groupEntity: GroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groupEntities: List<GroupEntity>)

    @Update
    suspend fun updateGroup(groupEntity: GroupEntity)

    @Query("DELETE FROM `groups` WHERE id = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Query("DELETE FROM `groups`")
    suspend fun deleteAll()

    @Query("DELETE FROM `groups` WHERE group_type = :type")
    suspend fun deleteByType(type: String)
}