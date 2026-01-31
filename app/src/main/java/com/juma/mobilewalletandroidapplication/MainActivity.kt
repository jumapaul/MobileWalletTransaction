package com.juma.mobilewalletandroidapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import com.juma.mobilewalletandroidapplication.presentation.navigation.HomeRoute
import com.juma.mobilewalletandroidapplication.presentation.navigation.LoginRoute
import com.juma.mobilewalletandroidapplication.presentation.navigation.NavigationGraph
import com.juma.mobilewalletandroidapplication.ui.theme.MobileWalletAndroidApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileWalletAndroidApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()

                    val context = LocalContext.current
                    val sharedPreferences: SharedPreferences =
                        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val customer = DatastoreUtil(sharedPreferences).getCustomerData("data")

                    val startDestination: Any = if (customer == null) LoginRoute else HomeRoute
                    NavigationGraph(
                        navHostController = navHostController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}