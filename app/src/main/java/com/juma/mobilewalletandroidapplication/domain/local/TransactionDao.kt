package com.juma.mobilewalletandroidapplication.domain.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(transaction: TransactionEntity)

    @Query("Select * From `transaction` Where clientTransactionId = :id")
    suspend fun getTransactionByClientId(id: String): TransactionEntity?

    @Query("SELECT * FROM `transaction` ORDER BY createdAt DESC")
    suspend fun getAllTransactions(): List<TransactionEntity?>

    // WorkManager needs this to pick up pending work
    @Query("SELECT * FROM `transaction` WHERE syncStatus = :status ORDER BY createdAt ASC")
    suspend fun getAllByStatus(status: TransactionStatus): List<TransactionEntity>

    @Update
    suspend fun update(transaction: TransactionEntity)
}