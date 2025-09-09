package com.example.appcascafirebase.domain.usecase

import com.example.appcascafirebase.data.model.User
import com.example.appcascafirebase.data.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        // Validações de negócio podem ser adicionadas aqui antes de chamar o repositório
        // Por exemplo, verificar se o email já existe (embora o Firestore possa ter regras para isso)
        // ou se os campos obrigatórios estão preenchidos de forma adequada.
        if (user.name.isBlank() || user.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Nome e email não podem estar vazios."))
        }
        return userRepository.addUser(user)
    }
}