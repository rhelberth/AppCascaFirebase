package com.example.appcascafirebase.data.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId // Anotação para mapear o ID do documento do Firestore automaticamente
    val id: String? = null, // ID será gerado pelo Firestore ou pode ser nulo antes de salvar
    val name: String = "",
    val email: String = "",
    val age: Int? = null // Exemplo de outro campo, pode ser nulo
) {
    // Construtor vazio necessário para o Firestore deserializar os dados
    constructor() : this(null, "", "", null)
}