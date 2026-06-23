package com.cb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.cb.ui.theme.CBTheme
import javax.inject.Inject
import com.cb.data.SessionManager
import com.cb.navigation.RootScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CBTheme {
                RootScreen(sessionManager = sessionManager)
            }
        }
    }
}