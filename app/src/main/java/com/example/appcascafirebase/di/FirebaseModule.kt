package com.example.appcascafirebase.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Define o escopo das dependências deste módulo
object FirebaseModule {

    @Provides
    @Singleton // Garante uma única instância do Firestore
    fun provideFirestoreInstance(): FirebaseFirestore {
        return Firebase.firestore
    }
}