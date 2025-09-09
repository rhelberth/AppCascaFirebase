package com.example.appcascafirebase.di

import com.example.appcascafirebase.data.repository.UserRepository
import com.example.appcascafirebase.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Define o escopo para as bindings neste módulo
abstract class RepositoryModule {

    @Binds
    @Singleton // Garante que a mesma instância de UserRepositoryImpl seja usada onde UserRepository for injetado
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}