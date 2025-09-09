package com.example.appcascafirebase.ui.presentation.user_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.appcascafirebase.Screen
import com.example.appcascafirebase.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.onErrorHandled()
        }
    }

    LaunchedEffect(uiState.userDeletedId) {
        uiState.userDeletedId?.let {
            snackbarHostState.showSnackbar(
                message = "Usuário deletado com sucesso",
                duration = SnackbarDuration.Short
            )
            viewModel.onUserDeleteHandled()
            // A lista deve se atualizar automaticamente devido ao Flow do Firestore
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Lista de Usuários") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditUser.withOptionalArg("userId", null))
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Usuário")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.users.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.users.isEmpty()) {
                Text(
                    text = "Nenhum usuário cadastrado.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.users, key = { user -> user.id ?: "" }) { user ->
                        UserItem(
                            user = user,
                            onEditClick = {
                                navController.navigate(
                                    Screen.AddEditUser.withOptionalArg("userId", user.id)
                                )
                            },
                            onDeleteClick = {
                                // Poderia mostrar um diálogo de confirmação aqui
                                viewModel.deleteUser(user.id ?: "")
                            }
                        )
                    }
                }
            }
             // Mostrar um indicador de carregamento no topo se estiver carregando em segundo plano
            if (uiState.isLoading && uiState.users.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter))
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* Pode navegar para detalhes do usuário se houver essa tela */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                user.age?.let {
                    Text(text = "Idade: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar Usuário")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = "Deletar Usuário")
                }
            }
        }
    }
}