package com.basebox.fundro.di

import com.basebox.fundro.data.repository.AuthRepositoryImpl
import com.basebox.fundro.data.repository.GroupMemberRepositoryImpl
import com.basebox.fundro.data.repository.GroupRepositoryImpl
import com.basebox.fundro.data.repository.NotificationRepositoryImpl
import com.basebox.fundro.data.repository.PaymentRepositoryImpl
import com.basebox.fundro.data.repository.UserRepositoryImpl
import com.basebox.fundro.domain.repository.AuthRepository
import com.basebox.fundro.domain.repository.GroupMemberRepository
import com.basebox.fundro.domain.repository.GroupRepository
import com.basebox.fundro.domain.repository.NotificationRepository
import com.basebox.fundro.domain.repository.PaymentRepository
import com.basebox.fundro.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

}