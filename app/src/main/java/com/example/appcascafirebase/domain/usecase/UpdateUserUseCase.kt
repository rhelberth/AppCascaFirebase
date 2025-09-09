package com.example.appcascafirebase.domain.usecase

import com.example.appcascafirebase.data.model.User
import com.example.appcascafirebase.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (user.id == null || user.id.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID não pode ser nulo ou vazio para atualização."))
        }
        if (user.name.isBlank() || user.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Nome e email não podem estar vazios."))
        }
        return userRepository.updateUser(user)
    }
}