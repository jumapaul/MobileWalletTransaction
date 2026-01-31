package com.juma.mobilewalletandroidapplication.utils

object Constants {
    private const val BASE_URL = "https://e37651d89359.ngrok-free.app/springboot-rest-api/api/v1/"
    const val LOGIN_ENDPOINT = "${BASE_URL}customers/login"
    const val ACCOUNT_BALANCE = "${BASE_URL}accounts/balance"
    const val TRANSACTION_HISTORY = "${BASE_URL}transactions/last-100-transactions"
    const val SEND_MONEY = "${BASE_URL}transactions/send-money"
}