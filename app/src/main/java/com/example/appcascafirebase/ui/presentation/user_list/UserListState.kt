package com.example.appcascafirebase.ui.presentation.user_list

import com.example.appcascafirebase.data.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
    val userDeletedId: String? = null // Para feedback de exclusão
)