package com.example.music

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home/{isLoginSuccess}") {

        composable("Home/{isLoginSuccess}") { backStackEntry ->
            val isLoginSuccess =
                (backStackEntry.arguments?.getString("isLoginSuccess") ?: "false").toBoolean()
            Log.d("Navigation", "Navigating to Home with isLoginSuccess = $isLoginSuccess")
            HomeScreen(
                isLoginSuccess = isLoginSuccess,
                openHome = {
                    navController.navigate("Home/$isLoginSuccess") {
                        popUpTo("Home") { inclusive = true }
                    }
                },
                openSearch = { navController.navigate("Search/$isLoginSuccess") },
                openProfile = { navController.navigate("Profile/$isLoginSuccess") },
                openMusicProfile = { id -> navController.navigate("MusicProfile/$id/$isLoginSuccess") }
            )
        }

        composable("MusicProfile/{id}/{isLoginSuccess}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val isLoginSuccess =
                (backStackEntry.arguments?.getString("isLoginSuccess") ?: "false").toBoolean()
            Log.d(
                "Navigation",
                "Navigating to MusicProfile with id = $id and isLoginSuccess = $isLoginSuccess"
            )
            MusicProfileScreen(
                isLoginSuccess = isLoginSuccess,
                id = id,
                openHome = { navController.navigate("Home/$isLoginSuccess") },
                openSearch = { navController.navigate("Search/$isLoginSuccess") },
                openProfile = { navController.navigate("Profile/$isLoginSuccess") }
            )
        }

        composable("Search/{isLoginSuccess}") { backStackEntry ->
            val isLoginSuccess =
                (backStackEntry.arguments?.getString("isLoginSuccess") ?: "false").toBoolean()
            Log.d("Navigation", "Navigating to Search with isLoginSuccess = $isLoginSuccess")
            Search(
                isLoginSuccess = isLoginSuccess,
                openHome = { navController.navigate("Home/$isLoginSuccess") },
                openSearch = {
                    navController.navigate("Search/$isLoginSuccess") {
                        popUpTo("Search") { inclusive = true }
                    }
                },
                openProfile = { navController.navigate("Profile/$isLoginSuccess") },
                openMusicProfile = { id -> navController.navigate("MusicProfile/$id/$isLoginSuccess") }
            )
        }

        composable("Profile/{isLoginSuccess}") { backStackEntry ->
            val isLoginSuccess =
                (backStackEntry.arguments?.getString("isLoginSuccess") ?: "false").toBoolean()
            Log.d("Navigation", "Navigating to Profile with isLoginSuccess = $isLoginSuccess")
            ProfileScreen(
                navController = navController,
                isLoginSuccess = isLoginSuccess,
                openHome = { navController.navigate("Home/$isLoginSuccess") },
                openSearch = { navController.navigate("Search/$isLoginSuccess") },
                openProfile = {
                    navController.navigate("Profile/$isLoginSuccess") {
                        popUpTo("Profile") { inclusive = true }
                    }
                },
                openLogin = { navController.navigate("Login") },
                openRegister = { navController.navigate("Register") }
            )
        }

        composable("Register") {backStackEntry ->
            val isLoginSuccess =
                (backStackEntry.arguments?.getString("isLoginSuccess") ?: "false").toBoolean()
            RegisterForm(
                navController = navController,
                openHome = {navController.navigate("Home/$isLoginSuccess")},
                openProfile = {navController.navigate("Profile/$isLoginSuccess")},
                openSearch = {navController.navigate("Search/$isLoginSuccess")}
            )
        }

        composable("Login") {backStackEntry ->
            val isLoginSuccess =
                (backStackEntry.arguments?.getString("isLoginSuccess") ?: "false").toBoolean()
            Login(
                isLoginSuccess =isLoginSuccess,
                navController = navController,
                openProfile = {navController.navigate("Profile/$isLoginSuccess")},
                openHome = {navController.navigate("Home/$isLoginSuccess")},
                openSearch = {navController.navigate("Search/$isLoginSuccess")}
            )
        }
    }
}



