package com.juma.mobilewalletandroidapplication.presentation.send_money_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyRequest
import com.juma.mobilewalletandroidapplication.presentation.common_composables.FilledOutlineButton
import com.juma.mobilewalletandroidapplication.presentation.common_composables.InputTextFieldComposable
import com.juma.mobilewalletandroidapplication.presentation.navigation.HomeRoute
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SendMoneyScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    sendMoneyScreenViewModel: SendMoneyScreenViewModel = hiltViewModel()
) {

//    val states = sendMoneyScreenViewModel.transactions.value

    val accountTo = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val amount = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Send Money", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                },

                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = modifier
                .padding(paddingValue)
                .padding(10.dp)
        ) {
//            Spacer(modifier = modifier.height(20.dp))
            InputTextFieldComposable(
                title = "Enter to account number",
                label = "CB001", inputText = accountTo
            )
            InputTextFieldComposable(
                title = "Enter amount",
                label = "1.0", inputText = amount
            )

            Spacer(modifier = modifier.height(10.dp))
            FilledOutlineButton(onClick = {
                val customer = sendMoneyScreenViewModel.customer
                val sendMoneyRequest = SendMoneyRequest(
                    UUID.randomUUID().toString(),
                    customer?.customerAccount!!,
                    accountTo.value.text,
                    amount.value.text.toInt(),
                    customer.customerId!!
                )
                if (accountTo.value.text.isEmpty() || accountTo.value.text.isEmpty()) {
                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                }
                sendMoneyScreenViewModel.sendMoney(sendMoneyRequest, navigateHome = {
                    navController.navigate(HomeRoute)
                })
            }, desc = "Send")
        }
    }

    // "Queued" confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Transaction Queued") },
            text = { Text("Your transaction has been queued and will sync automatically when connected.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("localTransactions")
                }) {
                    Text("View Transactions")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                }) {
                    Text("Back to Home")
                }
            }
        )
    }
}