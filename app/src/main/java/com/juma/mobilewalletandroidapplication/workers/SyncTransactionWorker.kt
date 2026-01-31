package com.juma.mobilewalletandroidapplication.workers

import android.content.Context
import androidx.room.util.copy
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.juma.mobilewalletandroidapplication.domain.local.TransactionDao
import com.juma.mobilewalletandroidapplication.domain.local.TransactionStatus
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyRequest

class SyncTransactionWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val transactionDao: TransactionDao,
    private val walletRepo: MobileWalletRepo
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val clientId = inputData.getString(KEY_CLIENT_TRANSACTION_ID)
            ?: return Result.failure(
                workDataOf("error" to "Missing clientTransaction Id")
            )

        val entity = transactionDao.getTransactionByClientId(clientId)
            ?: return Result.success()

        if (entity.syncStatus == TransactionStatus.SYNCED) return Result.success()

        transactionDao.update(
            entity.copy(syncStatus = TransactionStatus.SYNCING)
        )

        return try {
            val response = walletRepo.sendMoney(
                SendMoneyRequest(
                    clientTransactionId = entity.clientTransactionId,
                    accountFrom = entity.accountFrom,
                    accountTo = entity.accountTo,
                    amount = entity.amount.toInt(),
                    customerId = entity.customerId
                )
            )

            Result.success()
        } catch (e: Exception) {
            transactionDao.update(
                entity.copy(
                    syncStatus = TransactionStatus.FAILED,
                    attemptCount = entity.attemptCount + 1
                )
            )

            Result.failure(workDataOf("error" to (e.message?: "Unknown Error")))
        }
    }

    companion object {
        const val KEY_CLIENT_TRANSACTION_ID = "clientTransactionId"
    }
}