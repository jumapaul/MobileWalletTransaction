package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TransactionHistoryResponse(
    val accountNo: String,
    val amount: Double,
    val balance: Double,
    val customerId: String,
    val debitOrCredit: String,
    val id: Int,
    val transactionId: String,
    val transactionType: String
)