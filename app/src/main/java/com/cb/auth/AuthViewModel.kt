package com.cb.auth

import androidx.lifecycle.viewModelScope
import com.cb.core.BaseViewModel
import com.cb.data.SessionManager
import com.cb.data.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel<AuthState>(AuthState.Idle) {

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            updateState { AuthState.Error("Email and Password are required.") }
            return
        }

        updateState { AuthState.Loading }
        
        viewModelScope.launch {
            delay(1000) // Simulate network delay
            if (email.endsWith("@galgotiasuniversity.ac.in") || email == "test@test.com") {
                val session = UserSession(UUID.randomUUID().toString(), email, "Test User")
                sessionManager.createSession(session)
                updateState { AuthState.Success(session) }
            } else {
                updateState { AuthState.Error("Invalid credentials or non-university email.") }
            }
        }
    }

    fun signUp(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            updateState { AuthState.Error("All fields are required.") }
            return
        }

        updateState { AuthState.Loading }
        
        viewModelScope.launch {
            delay(1000) // Simulate network delay
            if (email.endsWith("@galgotiasuniversity.ac.in")) {
                val session = UserSession(UUID.randomUUID().toString(), email, name)
                sessionManager.createSession(session)
                updateState { AuthState.Success(session) }
            } else {
                updateState { AuthState.Error("Must use a university email to sign up.") }
            }
        }
    }
    
    fun forgotPassword(email: String) {
        if (email.isBlank()) {
            updateState { AuthState.Error("Email is required.") }
            return
        }
        
        updateState { AuthState.Loading }
        viewModelScope.launch {
            delay(1000)
            // Just simulate success
            updateState { AuthState.Error("Password reset link sent to $email (Simulated)") } 
            // Using Error state to display Snackbar message for now since we don't have a "Message" state
            // Let's revert to Idle after a bit so they can try again or go back
            delay(2000)
            updateState { AuthState.Idle }
        }
    }

    fun resetState() {
        updateState { AuthState.Idle }
    }
}
