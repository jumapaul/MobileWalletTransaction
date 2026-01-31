package com.juma.mobilewalletandroidapplication.presentation.send_money_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juma.mobilewalletandroidapplication.domain.local.TransactionEntity
import com.juma.mobilewalletandroidapplication.domain.local.TransactionStatus
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransactionsScreen(
    viewModel: SendMoneyScreenViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsState()

    // Refresh on entry
    LaunchedEffect(Unit) { viewModel.fetchTransactions() }

    Scaffold(topBar = { TopAppBar(title = { Text("Local Transactions") }) }) { padding ->
        if (transactions.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text("No transactions yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(transactions) { it ->
                    TransactionCard(tx = it!!,
                        onRetry = { viewModel.retryTransaction(it.clientTransactionId) }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionCard(tx: TransactionEntity, onRetry: () -> Unit) {
    val statusColor = when (tx.syncStatus) {
        TransactionStatus.QUEUED -> Color(0xFFFFC107)   // amber
        TransactionStatus.SYNCING -> Color(0xFF2196F3)   // blue
        TransactionStatus.SYNCED -> Color(0xFF4CAF50)   // green
        TransactionStatus.FAILED -> Color(0xFFF44336)   // red
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${tx.accountFrom} → ${tx.accountTo}", fontWeight = FontWeight.Bold)
                Text(
                    tx.syncStatus.name,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Text("Amount: ${tx.amount}")
            Text("Created: ${Instant.ofEpochMilli(tx.createdAt)}")   // format as you like

            if (tx.syncStatus == TransactionStatus.FAILED) {
                Text("Attempts: ${tx.attemptCount}")
                Button(
                    onClick = onRetry,
                ) {
                    Text("Retry")
                }
            }
        }
    }
}