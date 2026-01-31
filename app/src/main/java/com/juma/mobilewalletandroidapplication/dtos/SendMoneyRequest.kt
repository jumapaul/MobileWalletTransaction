package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SendMoneyRequest(
    val clientTransactionId: String,
    val accountFrom: String,
    val accountTo: String,
    val amount: Int,
    val customerId: String
)