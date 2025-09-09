package com.example.appcascafirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcascafirebase.ui.presentation.add_edit_user.AddEditUserScreen
import com.example.appcascafirebase.ui.presentation.user_list.UserListScreen
import com.example.appcascafirebase.ui.theme.AppCascaFirebaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Hilt: Permite a injeção de dependências nesta Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppCascaFirebaseTheme { // Certifique-se de que seu tema M3 está configurado
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Define as rotas de navegação como objetos para melhor organização e segurança de tipo
sealed class Screen(val route: String) {
    object UserList : Screen("user_list_screen")
    object AddEditUser : Screen("add_edit_user_screen")

    // Função auxiliar para construir rotas com argumentos
    fun withOptionalArg(argName: String, argValue: String?): String {
        return if (argValue != null) {
            "$route?$argName=$argValue"
        } else {
            route // Para o caso de adicionar novo usuário, sem ID
        }
    }
     fun routeWithArgName(argName: String): String {
        return "$route?$argName={$argName}"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.UserList.route) {
        composable(route = Screen.UserList.route) {
            UserListScreen(navController = navController)
        }
        composable(
            route = Screen.AddEditUser.routeWithArgName("userId"),
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true // userId é opcional (para adicionar novo usuário)
                    defaultValue = null
                }
            )
        ) {
            // O AddEditUserViewModel pegará o userId via SavedStateHandle
            AddEditUserScreen(navController = navController)
        }
    }
}