package com.example.appcascafirebase.ui.presentation.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcascafirebase.domain.usecase.DeleteUserUseCase
import com.example.appcascafirebase.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserListState())
    val uiState: StateFlow<UserListState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        getUsersUseCase().onEach { result ->
            _uiState.update { it.copy(isLoading = true) }
            result.fold(
                onSuccess = { users ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            users = users,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Erro ao buscar usuários"
                        )
                    }
                }
            )
        }.launchIn(viewModelScope)
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteUserUseCase(userId).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userDeletedId = userId, // Informa à UI qual usuário foi deletado
                            error = null
                        )
                    }
                    // Opcional: recarregar a lista ou deixar que o Flow do Firestore atualize automaticamente
                    // loadUsers() // Descomente se a atualização automática via Flow não for suficiente
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Erro ao deletar usuário"
                        )
                    }
                }
            )
        }
    }

    fun onUserDeleteHandled() {
        _uiState.update { it.copy(userDeletedId = null) }
    }

    fun onErrorHandled() {
        _uiState.update { it.copy(error = null) }
    }
}