package com.juma.mobilewalletandroidapplication.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 2, entities = [TransactionEntity::class], exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao
}