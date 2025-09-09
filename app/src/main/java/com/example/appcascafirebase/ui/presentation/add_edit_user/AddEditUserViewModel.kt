package com.example.appcascafirebase.ui.presentation.add_edit_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcascafirebase.data.model.User
import com.example.appcascafirebase.domain.usecase.AddUserUseCase
import com.example.appcascafirebase.domain.usecase.GetUserUseCase
import com.example.appcascafirebase.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditUserViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    savedStateHandle: SavedStateHandle // Para receber argumentos de navegação (userId)
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUserState())
    val uiState: StateFlow<AddEditUserState> = _uiState.asStateFlow()

    private val userId: String? = savedStateHandle["userId"] // Argumento de navegação

    init {
        if (userId != null && userId != "null") { // "null" como string pode vir da navegação
            _uiState.update { it.copy(isEditMode = true) }
            loadUser(userId)
        } else {
            _uiState.update { it.copy(isEditMode = false) }
        }
    }

    fun loadUser(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUserUseCase(id).fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            name = user?.name ?: "",
                            email = user?.email ?: "",
                            age = user?.age?.toString() ?: "",
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Erro ao carregar usuário"
                        )
                    }
                }
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onAgeChange(age: String) {
        _uiState.update { it.copy(age = age) }
    }

    fun saveUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isUserSaved = false) }

            val name = _uiState.value.name
            val email = _uiState.value.email
            val age = _uiState.value.age.toIntOrNull()

            val userToSave = if (_uiState.value.isEditMode) {
                _uiState.value.user?.copy(name = name, email = email, age = age)
            } else {
                User(name = name, email = email, age = age)
            }

            if (userToSave == null && _uiState.value.isEditMode) {
                 _uiState.update { it.copy(isLoading = false, error = "Erro ao preparar usuário para atualização") }
                return@launch
            }
            
            if (userToSave == null ) {
                 _uiState.update { it.copy(isLoading = false, error = "Usuário inválido") }
                return@launch
            }


            val result = if (_uiState.value.isEditMode) {
                updateUserUseCase(userToSave)
            } else {
                addUserUseCase(userToSave)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isUserSaved = true) }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Erro ao salvar usuário"
                        )
                    }
                }
            )
        }
    }

    fun onUserSavedHandled() {
        _uiState.update { it.copy(isUserSaved = false) }
    }

    fun onErrorHandled() {
        _uiState.update { it.copy(error = null) }
    }
}