package com.yujin.record

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yujin.record.ui.theme.RecordTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            RecordTheme {
                RecordNavGraph()
            }
        }
    }
}