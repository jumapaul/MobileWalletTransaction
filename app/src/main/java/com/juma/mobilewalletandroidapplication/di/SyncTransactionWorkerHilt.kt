package com.juma.mobilewalletandroidapplication.di

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.juma.mobilewalletandroidapplication.domain.local.TransactionDao
import com.juma.mobilewalletandroidapplication.domain.local.TransactionStatus
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyRequest
import com.juma.mobilewalletandroidapplication.workers.SyncTransactionWorker.Companion.KEY_CLIENT_TRANSACTION_ID
import dagger.assisted.Assisted

@HiltWorker
class SyncTransactionWorkerHilt(
    context: Context,
    workerParameters: WorkerParameters,
    private val transactionDao: TransactionDao,
    private val mobileWalletRepo: MobileWalletRepo
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val clientId = inputData.getString(KEY_CLIENT_TRANSACTION_ID)
            ?: return Result.failure(workDataOf("error" to "Missing clientTransactionId"))

        val entity = transactionDao.getTransactionByClientId(clientId)
            ?: return Result.success()

        if (entity.syncStatus == TransactionStatus.SYNCED) return Result.success()

        transactionDao.update(entity.copy(syncStatus = TransactionStatus.SYNCING))

        return try {
            val response = mobileWalletRepo.sendMoney(
                SendMoneyRequest(
                    clientTransactionId = entity.clientTransactionId,
                    accountFrom = entity.accountFrom,
                    accountTo = entity.accountTo,
                    amount = entity.amount.toInt(),
                    customerId = entity.customerId
                )
            )

            if (response?.status == "OK") {
                transactionDao.update(
                    entity.copy(
                        syncStatus = TransactionStatus.SYNCED
                    )
                )
            }

            Result.success()

        } catch (e: Exception) {
            transactionDao.update(
                entity.copy(
                    syncStatus = TransactionStatus.FAILED,
                    attemptCount = entity.attemptCount + 1
                )
            )
            Result.failure(workDataOf("error" to (e.message ?: "Unknown error")))
        }
    }
}

// NOTE: If you use @HiltWorker, you do NOT need a custom WorkerFactory.
// Hilt handles injection automatically via WorkerFactory integration.
// Just make sure your Application class calls:
//     WorkManager.setConfiguration(
//         Configuration.Builder()
//             .setWorkerFactory(hiltWorkerFactory)   // injected via @Inject
//             .build()
//     )
