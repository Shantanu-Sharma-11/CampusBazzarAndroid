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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cb.core.components.LabeledAuthField
import com.cb.core.components.PrimaryAuthButton
import com.cb.core.components.SecureAuthField
import com.cb.core.theme.AuthBackground
import com.cb.core.theme.AuthBrandColor
import com.cb.core.theme.AuthDarkNavy
import com.cb.core.theme.Dimensions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    
    // Email Verification State
    var fullEmail by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    
    // Details Entry State
    var fullName by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val isEmailValid = fullEmail.contains("@") && fullEmail.contains(".")
    val isOTPValid = otpCode.length == 6
    val isDetailsFormValid = fullName.trim().isNotEmpty() &&
        course.trim().isNotEmpty() &&
        branch.trim().isNotEmpty() &&
        password.length >= 6 &&
        password == confirmPassword

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
                        if (step == 2) {
                            step = 1
                            otpSent = false
                            otpCode = ""
                        } else {
                            onNavigateToLogin()
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
                    text = if (step == 1) "Verify Email" else "Complete Profile",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AuthDarkNavy
                )
                Text(
                    text = if (step == 1) "Enter your official college email to continue." else "Set up your student account details.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (step == 1) {
                // Email Verification
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    LabeledAuthField(
                        title = "Official College Email",
                        placeholder = "e.g. name.admissionnumber@galgotiasuniversity.ac.in",
                        text = fullEmail,
                        onValueChange = { fullEmail = it },
                        keyboardType = KeyboardType.Email,
                        enabled = !otpSent && !isLoading
                    )

                    if (!otpSent) {
                        PrimaryAuthButton(
                            text = "Send Verification OTP",
                            onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    delay(1000)
                                    isLoading = false
                                    otpSent = true
                                    // Mock profile prepopulation
                                    fullName = "John Doe"
                                    course = "Computer Science"
                                    branch = "B.Tech"
                                }
                            },
                            isEnabled = isEmailValid,
                            isLoading = isLoading
                        )
                    } else {
                        LabeledAuthField(
                            title = "Enter OTP",
                            placeholder = "123456",
                            text = otpCode,
                            onValueChange = { otpCode = it },
                            keyboardType = KeyboardType.Number,
                            enabled = !isLoading
                        )
                        
                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        PrimaryAuthButton(
                            text = "Verify & Continue",
                            onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    errorMessage = ""
                                    delay(1000)
                                    isLoading = false
                                    if (otpCode == "123456") {
                                        step = 2
                                    } else {
                                        errorMessage = "Invalid or expired OTP."
                                    }
                                }
                            },
                            isEnabled = isOTPValid,
                            isLoading = isLoading
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Log in",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AuthBrandColor,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            } else {
                // Details Entry
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    LabeledAuthField(
                        title = "First Name",
                        placeholder = "Auto-filled from email",
                        text = fullName,
                        onValueChange = { fullName = it },
                        enabled = false
                    )
                    
                    LabeledAuthField(
                        title = "University",
                        placeholder = "Auto-filled from email",
                        text = "Galgotias University",
                        onValueChange = {},
                        enabled = false
                    )
                    
                    LabeledAuthField(
                        title = "Department",
                        placeholder = "Auto-filled from email",
                        text = course,
                        onValueChange = {},
                        enabled = false
                    )
                    
                    LabeledAuthField(
                        title = "Course & Branch",
                        placeholder = "Auto-filled from email",
                        text = branch,
                        onValueChange = {},
                        enabled = false
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SecureAuthField(
                            title = "Password",
                            placeholder = "Create a password",
                            text = password,
                            onValueChange = { password = it },
                            enabled = !isLoading
                        )
                        SecureAuthField(
                            title = "Confirm Password",
                            placeholder = "Repeat your password",
                            text = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            enabled = !isLoading
                        )
                        if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                            Text(
                                text = "Passwords do not match",
                                color = Color.Red,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    PrimaryAuthButton(
                        text = "Create Account",
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                errorMessage = ""
                                // Connect to Supabase AuthViewModel later
                                viewModel.signUp(fullName, fullEmail, password)
                                delay(1000)
                                isLoading = false
                                onNavigateToLogin() // Success
                            }
                        },
                        isEnabled = isDetailsFormValid,
                        isLoading = isLoading,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
