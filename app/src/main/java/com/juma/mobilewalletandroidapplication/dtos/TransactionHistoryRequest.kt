package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TransactionHistoryRequest(
    val customerId: String,
    val pin: String
)