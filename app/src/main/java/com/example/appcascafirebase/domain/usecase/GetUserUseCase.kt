package com.example.appcascafirebase.domain.usecase

import com.example.appcascafirebase.data.model.User
import com.example.appcascafirebase.data.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User?> {
        if (userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID não pode estar vazio."))
        }
        return userRepository.getUser(userId)
    }
}