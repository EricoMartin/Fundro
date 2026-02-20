package com.basebox.fundro.di

import android.content.Context
import androidx.room.Room
import com.basebox.fundro.data.local.db.FundroDatabase
import com.basebox.fundro.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFundroDatabase(
        @ApplicationContext context: Context
    ): FundroDatabase {
        return Room.databaseBuilder(
            context,
            FundroDatabase::class.java,
            FundroDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development only!
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: FundroDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideGroupDao(database: FundroDatabase): GroupDao {
        return database.groupDao()
    }

    @Provides
    @Singleton
    fun provideGroupMemberDao(database: FundroDatabase): GroupMemberDao {
        return database.groupMemberDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: FundroDatabase): NotificationDao {
        return database.notificationDao()
    }
}