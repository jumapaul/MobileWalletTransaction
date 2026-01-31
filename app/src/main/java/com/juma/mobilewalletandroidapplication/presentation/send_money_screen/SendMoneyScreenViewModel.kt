package com.juma.mobilewalletandroidapplication.presentation.send_money_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.juma.mobilewalletandroidapplication.di.SyncTransactionWorkerHilt
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import com.juma.mobilewalletandroidapplication.domain.local.TransactionDao
import com.juma.mobilewalletandroidapplication.domain.local.TransactionEntity
import com.juma.mobilewalletandroidapplication.domain.local.TransactionStatus
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyRequest
import com.juma.mobilewalletandroidapplication.workers.SyncTransactionWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SendMoneyScreenViewModel @Inject constructor(
    private val datastoreUtil: DatastoreUtil,
    private val transactionDao: TransactionDao,
    private val workManager: WorkManager
) : ViewModel() {

    var customer = datastoreUtil.getCustomerData("data")
    private val _transactions = MutableStateFlow<List<TransactionEntity?>>(emptyList())
    val transactions = _transactions.asStateFlow()

    init {
        fetchTransactions()
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            _transactions.value = transactionDao.getAllTransactions()
        }
    }

    /**
     * Main entry-point when the user taps "Send".
     *  1. Writes a QUEUED row to Room immediately.
     *  2. Enqueues a WorkManager task (network-constrained).
     *  Returns instantly so the UI can show "Queued for sync".
     */
    fun sendMoney(sendMoneyRequest: SendMoneyRequest, navigateHome: () -> Unit) {
        val entity = TransactionEntity(
            clientTransactionId = sendMoneyRequest.clientTransactionId,
            accountFrom = sendMoneyRequest.accountFrom,
            accountTo = sendMoneyRequest.accountTo,
            amount = sendMoneyRequest.amount.toDouble(),
            createdAt = System.currentTimeMillis(),
            customerId = sendMoneyRequest.customerId,
            syncStatus = TransactionStatus.QUEUED
        )

        viewModelScope.launch {
            // 1. Persist locally — this never fails (offline-safe)
            transactionDao.insert(entity)

            // 2. Kick off background sync
            enqueueSync(entity.clientTransactionId)

            // 3. Refresh the UI list
            fetchTransactions()
            navigateHome()
        }
    }

    /**
     * Manual retry for a FAILED transaction.
     * Resets status to QUEUED and re-enqueues.
     */
    fun retryTransaction(clientTransactionId: String) {
        viewModelScope.launch {
            val entity =
                transactionDao.getTransactionByClientId(clientTransactionId) ?: return@launch

            transactionDao.update(
                entity.copy(syncStatus = TransactionStatus.QUEUED)
            )
        }
    }

    private fun enqueueSync(clientTransactionId: String) {
        val workRequest = OneTimeWorkRequestBuilder<SyncTransactionWorkerHilt>()
            .setInputData(
                workDataOf(
                    SyncTransactionWorker.KEY_CLIENT_TRANSACTION_ID to clientTransactionId
                )
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()
    }
//    private val _transactions = MutableStateFlow<List<TransactionEntity?>>(arrayListOf())
//    val transactions get() = _transactions.asStateFlow()
//
//    fun addToTransaction() {
//        viewModelScope.launch {
//            _transactions.value = transactionDao.getAllTransactions()
//        }
//    }
//
//    fun addToDB() {
//        viewModelScope.launch {
//            val transactionEntity = TransactionEntity(
//                1,
//                "CD002",
//                "CD004",
//                "CD005",
//                3.0
//            )
//            transactionDao.insert(transactionEntity)
//        }
//    }

}