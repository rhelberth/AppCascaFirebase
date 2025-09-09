package com.example.appcascafirebase.data.repository

import com.example.appcascafirebase.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUser(user: User): Result<Unit>
    suspend fun getUser(userId: String): Result<User?>
    fun getAllUsers(): Flow<Result<List<User>>>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun deleteUser(userId: String): Result<Unit>
}