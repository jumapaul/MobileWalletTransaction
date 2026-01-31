package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountBalanceResponse(
    val accountNumber: String,
    val balance: Double
)