package com.juma.mobilewalletandroidapplication.domain.local

import androidx.room.TypeConverter

class TransactionTypeConverter {
    class TransactionTypeConverter {
        @TypeConverter
        fun fromStatus(status: TransactionStatus): String = status.name

        @TypeConverter
        fun toStatus(name: String): TransactionStatus = TransactionStatus.valueOf(name)
    }
}