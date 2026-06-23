package com.cb.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cb.core.components.LabeledAuthField
import com.cb.core.components.PrimaryAuthButton
import com.cb.core.components.SecureAuthField
import com.cb.core.theme.AuthBackground
import com.cb.core.theme.AuthDarkNavy
import com.cb.core.theme.Dimensions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val coroutineScope = rememberCoroutineScope()

    val subtitleText = when (step) {
        1 -> "Enter your email to receive a reset OTP."
        2 -> "We've sent a 6-digit OTP to your email."
        else -> "Create a new secure password."
    }

    val buttonText = when (step) {
        1 -> "Send OTP"
        2 -> "Verify OTP"
        else -> "Update Password"
    }

    val isStepValid = when (step) {
        1 -> email.isNotEmpty()
        2 -> otp.isNotEmpty()
        3 -> newPassword.isNotEmpty() && newPassword == confirmPassword
        else -> false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        if (step > 1) {
                            step -= 1
                        } else {
                            onNavigateBack()
                        }
                    }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimensions.PaddingLarge)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Reset Password",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AuthDarkNavy
                )
                Text(
                    text = subtitleText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Form Fields
            if (step == 1) {
                LabeledAuthField(
                    title = "Email Address",
                    placeholder = "Enter your university email",
                    text = email,
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email,
                    enabled = !isLoading
                )
            } else if (step == 2) {
                LabeledAuthField(
                    title = "Enter OTP",
                    placeholder = "Enter 6-digit OTP",
                    text = otp,
                    onValueChange = { otp = it },
                    keyboardType = KeyboardType.Number,
                    enabled = !isLoading
                )
            } else if (step == 3) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SecureAuthField(
                        title = "New Password",
                        placeholder = "Enter new password",
                        text = newPassword,
                        onValueChange = { newPassword = it },
                        enabled = !isLoading
                    )
                    SecureAuthField(
                        title = "Confirm Password",
                        placeholder = "Repeat new password",
                        text = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        enabled = !isLoading
                    )
                    if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                        Text(
                            text = "Passwords do not match",
                            color = Color.Red,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            PrimaryAuthButton(
                text = buttonText,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        errorMessage = ""
                        delay(1000) // Mocking network
                        isLoading = false
                        
                        if (step == 1) step = 2
                        else if (step == 2) {
                            if (otp == "123456") step = 3
                            else errorMessage = "Invalid OTP. For now, use 123456."
                        } else if (step == 3) {
                            onNavigateBack() // Success
                        }
                    }
                },
                isEnabled = isStepValid,
                isLoading = isLoading,
                modifier = Modifier.padding(top = 10.dp)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
