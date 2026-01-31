package com.juma.mobilewalletandroidapplication.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.juma.mobilewalletandroidapplication.presentation.history.HistoryScreen
import com.juma.mobilewalletandroidapplication.presentation.history.HistoryViewModel
import com.juma.mobilewalletandroidapplication.presentation.home.HomeScreen
import com.juma.mobilewalletandroidapplication.presentation.login.LoginScreen
import com.juma.mobilewalletandroidapplication.presentation.profile.ProfileScreen
import com.juma.mobilewalletandroidapplication.presentation.send_money_screen.LocalTransactionsScreen
import com.juma.mobilewalletandroidapplication.presentation.send_money_screen.SendMoneyScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    startDestination: Any
) {

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable<LoginRoute> {
            LoginScreen(navHostController)
        }

        composable<HomeRoute> {
            HomeScreen(
                navHostController
            )
        }

        composable<SendMoneyRoute> {
            SendMoneyScreen(navHostController)
        }

        composable<HistoryRoute> {
            HistoryScreen(navHostController)
        }

        composable<LocalTransactionRoute> {
            LocalTransactionsScreen()
        }

        composable<ProfileRoute> {
            ProfileScreen(navHostController)
        }
    }
}