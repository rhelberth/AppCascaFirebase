package com.example.appcascafirebase.domain.usecase

import com.example.appcascafirebase.data.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        if (userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID não pode estar vazio para exclusão."))
        }
        return userRepository.deleteUser(userId)
    }
}