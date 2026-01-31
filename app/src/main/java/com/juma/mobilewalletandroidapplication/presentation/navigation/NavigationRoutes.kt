package com.juma.mobilewalletandroidapplication.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object HomeRoute

@Serializable
object SendMoneyRoute

@Serializable
object ProfileRoute

@Serializable
object LocalTransactionRoute

@Serializable
data class HistoryRoute(
    val pin: String
)