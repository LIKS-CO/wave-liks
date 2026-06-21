package com.example.liks_sports

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.example.liks_sports.BuildConfig
import com.example.liks_sports.ui.navigation.AppNavHost
import org.woheller69.freeDroidWarn.FreeDroidWarn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FreeDroidWarn.showWarningOnUpgrade(this, BuildConfig.VERSION_CODE)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
        }
    }
}
