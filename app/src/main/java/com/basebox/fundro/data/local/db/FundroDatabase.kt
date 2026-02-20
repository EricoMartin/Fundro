package com.basebox.fundro.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.basebox.fundro.data.local.dao.*
import com.basebox.fundro.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        GroupEntity::class,
        GroupMemberEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FundroDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        const val DATABASE_NAME = "fundro_database"
    }
}