package com.example.appcascafirebase.domain.usecase

import com.example.appcascafirebase.data.model.User
import com.example.appcascafirebase.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> {
        return userRepository.getAllUsers()
    }
}