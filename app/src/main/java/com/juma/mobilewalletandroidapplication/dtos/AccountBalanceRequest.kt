package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountBalanceRequest(
    val customerId: String
)