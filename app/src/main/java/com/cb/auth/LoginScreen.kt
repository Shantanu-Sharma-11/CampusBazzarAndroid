package com.cb.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cb.core.components.LabeledAuthField
import com.cb.core.components.PrimaryAuthButton
import com.cb.core.components.SecureAuthField
import com.cb.core.theme.AuthBackground
import com.cb.core.theme.AuthBrandColor
import com.cb.core.theme.Dimensions

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgot: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val authState by viewModel.uiState.collectAsState()
    val isLoading = authState is AuthState.Loading
    
    val isFormValid = email.trim().isNotEmpty() && password.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimensions.PaddingLarge),
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // Header
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineLarge,
                color = AuthBrandColor
            )
            Text(
                text = "Secure access to your account",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Form Fields
        Column(verticalArrangement = Arrangement.spacedBy(Dimensions.LoginFieldSpacing)) {
            LabeledAuthField(
                title = "Email ID",
                placeholder = "Enter your college email id",
                text = email,
                onValueChange = { email = it },
                enabled = !isLoading
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SecureAuthField(
                    title = "Password",
                    placeholder = "Enter your password",
                    text = password,
                    onValueChange = { password = it },
                    enabled = !isLoading
                )
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AuthBrandColor,
                        modifier = Modifier.clickable { onNavigateToForgot() }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Error Message
        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Login Button
        PrimaryAuthButton(
            text = "Login",
            onClick = { viewModel.login(email, password) },
            isEnabled = isFormValid,
            isLoading = isLoading,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        // Help Footer
        Box(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), contentAlignment = Alignment.Center) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Need help signing in?",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(40.dp))
        
        // Sign Up Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = AuthBrandColor,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}
