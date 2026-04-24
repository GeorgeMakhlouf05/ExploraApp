package me.georgemakhlouf.exploraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import me.georgemakhlouf.exploraapp.ui.theme.ExploraAppTheme
import me.georgemakhlouf.exploraapp.ui.theme.HomeScreen
import me.georgemakhlouf.exploraapp.ui.theme.LoginScreen
import me.georgemakhlouf.exploraapp.ui.theme.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploraAppTheme {
                val auth = FirebaseAuth.getInstance()
                val myNavController = rememberNavController()

                // Session persistence check
                val startDestination = if (auth.currentUser != null) "home" else "login"

                NavHost(
                    navController = myNavController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(route = "login") {
                        LoginScreen(
                            onLoginSuccess = {
                                myNavController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                myNavController.navigate("Register")
                            }
                        )
                    }

                    composable(route = "Register") {
                        RegisterScreen(
                            onRegisterSuccess = {
                                myNavController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                myNavController.navigate("login")
                            }
                        )
                    }

                    composable(route = "home") {
                        HomeScreen(
                            onLogout = {
                                auth.signOut()
                                myNavController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }

                            }
                        )
                    }
                }
            }
        }
    }
}
