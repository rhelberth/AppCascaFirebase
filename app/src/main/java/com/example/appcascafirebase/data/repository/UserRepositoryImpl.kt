package com.example.appcascafirebase.data.repository

import com.example.appcascafirebase.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Define que o Hilt deve fornecer uma única instância deste repositório
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun addUser(user: User): Result<Unit> {
        return try {
            // Se o ID for nulo, o Firestore gerará um automaticamente
            val documentReference = if (user.id == null) {
                usersCollection.document()
            } else {
                usersCollection.document(user.id)
            }
            // Usar o ID gerado (ou fornecido) para criar o usuário final a ser salvo
            val userToSave = user.copy(id = documentReference.id)
            documentReference.set(userToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(userId: String): Result<User?> {
        return try {
            val documentSnapshot = usersCollection.document(userId).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllUsers(): Flow<Result<List<User>>> = callbackFlow {
        val listenerRegistration = usersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                close(error) // Fecha o flow em caso de erro
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val users = snapshot.toObjects(User::class.java)
                trySend(Result.success(users))
            }
        }
        // Quando o Flow for cancelado (o coletor parar de ouvir), remove o listener
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            requireNotNull(user.id) { "User ID must not be null for update" }
            usersCollection.document(user.id).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            usersCollection.document(userId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}