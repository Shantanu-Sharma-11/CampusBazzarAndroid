package com.cb.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cb.MainScreen
import com.cb.auth.ForgotPasswordScreen
import com.cb.auth.LoginScreen
import com.cb.auth.SignUpScreen
import com.cb.data.SessionManager
import kotlinx.coroutines.delay

sealed class AuthRoute(val route: String) {
    object Login : AuthRoute("login")
    object SignUp : AuthRoute("signup")
    object ForgotPassword : AuthRoute("forgot_password")
}

@Composable
fun RootScreen(sessionManager: SessionManager) {
    val session by sessionManager.currentSession.collectAsState()
    var isChecking by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000) // Simulate session check delay like iOS
        isChecking = false
    }

    if (isChecking) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 20.dp))
            Text(
                text = "Waking up Campus Bazzar...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else if (session == null) {
        val authNavController = rememberNavController()
        NavHost(navController = authNavController, startDestination = AuthRoute.Login.route) {
            composable(AuthRoute.Login.route) {
                LoginScreen(
                    onNavigateToSignUp = { authNavController.navigate(AuthRoute.SignUp.route) },
                    onNavigateToForgot = { authNavController.navigate(AuthRoute.ForgotPassword.route) }
                )
            }
            composable(AuthRoute.SignUp.route) {
                SignUpScreen(
                    onNavigateToLogin = { authNavController.popBackStack(AuthRoute.Login.route, inclusive = false) }
                )
            }
            composable(AuthRoute.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateBack = { authNavController.popBackStack() }
                )
            }
        }
    } else {
        MainScreen(onLogout = { sessionManager.clearSession() })
    }
}
