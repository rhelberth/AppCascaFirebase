package com.example.appcascafirebase.ui.presentation.add_edit_user

import com.example.appcascafirebase.data.model.User

data class AddEditUserState(
    val isLoading: Boolean = false,
    val user: User? = null, // Usuário carregado para edição
    val name: String = "",
    val email: String = "",
    val age: String = "", // Usar String para o campo de texto, converter para Int depois
    val error: String? = null,
    val isUserSaved: Boolean = false, // Flag para indicar que o usuário foi salvo/atualizado com sucesso
    val isEditMode: Boolean = false // Indica se está no modo de edição
)
