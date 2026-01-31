package com.juma.mobilewalletandroidapplication.domain.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = false)
    val clientTransactionId: String,
    val customerId: String,
    val accountFrom: String,
    val accountTo: String,
    val amount: Double,
    val syncStatus: TransactionStatus,
    val createdAt: Long,
    val attemptCount: Int = 0,
)