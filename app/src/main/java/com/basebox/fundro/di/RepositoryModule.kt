package com.basebox.fundro.di

import com.basebox.fundro.data.repository.AuthRepositoryImpl
import com.basebox.fundro.data.repository.GroupMemberRepositoryImpl
import com.basebox.fundro.data.repository.GroupRepositoryImpl
import com.basebox.fundro.domain.repository.AuthRepository
import com.basebox.fundro.domain.repository.GroupMemberRepository
import com.basebox.fundro.domain.repository.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    @Singleton
    abstract fun bindGroupMemberRepository(
        groupMemberRepositoryImpl: GroupMemberRepositoryImpl
    ): GroupMemberRepository
}