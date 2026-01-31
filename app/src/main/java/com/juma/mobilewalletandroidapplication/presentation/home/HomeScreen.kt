package com.juma.mobilewalletandroidapplication.presentation.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.juma.mobilewalletandroidapplication.dtos.LoginRequest
import com.juma.mobilewalletandroidapplication.dtos.LoginResponse
import com.juma.mobilewalletandroidapplication.presentation.home.composables.BottomSheet
import com.juma.mobilewalletandroidapplication.presentation.navigation.HistoryRoute
import com.juma.mobilewalletandroidapplication.presentation.navigation.LocalTransactionRoute
import com.juma.mobilewalletandroidapplication.presentation.navigation.ProfileRoute
import com.juma.mobilewalletandroidapplication.presentation.navigation.SendMoneyRoute
import com.juma.mobilewalletandroidapplication.presentation.ui_states.UiState
import com.juma.mobilewalletandroidapplication.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch
import com.juma.mobilewalletandroidapplication.presentation.navigation.LoginRoute

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val openPinSendDialog = remember { mutableStateOf(false) }
    val openPinHistoryDialog = remember { mutableStateOf(false) }
    val openBalanceDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val customer = homeViewModel.customer
    val customerPin = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val context = LocalContext.current
    val verifyPinState = homeViewModel.pinState.value
    val getHistoryPinState = homeViewModel.pinState.value
    val getBalanceState = homeViewModel.accountBalanceState.value
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = modifier.height(20.dp))
            Text(text = "Welcome Back ${customer?.customerName}", fontWeight = FontWeight.Bold)

            Spacer(modifier = modifier.height(20.dp))
            ColumnItem(modifier = modifier, desc = "Send Money", onclick = {
                navController.navigate(SendMoneyRoute)
//                openPinSendDialog.value = true
            })

            Spacer(modifier = modifier.height(10.dp))
            ColumnItem(modifier = modifier, desc = "Check Balance", onclick = {
                coroutineScope.launch {
                    homeViewModel.getBalance(context)
                }
            })
            Spacer(modifier = modifier.height(10.dp))
            ColumnItem(modifier = modifier, desc = "Get History", onclick = {
                openPinHistoryDialog.value = true
            })

            Spacer(modifier = modifier.height(10.dp))
            ColumnItem(modifier = modifier, desc = "Local Transactions", onclick = {
                navController.navigate(LocalTransactionRoute)
            })
            Spacer(modifier = modifier.height(10.dp))
            ColumnItem(modifier = modifier, desc = "Profile", onclick = {
                navController.navigate(ProfileRoute)
            })

            Spacer(modifier = modifier.height(10.dp))
            ColumnItem(modifier = modifier, desc = "Log Out", onclick = {
                homeViewModel.logout(
                    onClick = {
                        navController.navigate(LoginRoute) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            })
        }
    }

    if (openBalanceDialog.value) {
        Dialog(
            onDismissRequest = {
                openBalanceDialog.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,   // Full width
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(text = "Account Number: ")
                        Text(text = "${getBalanceState.item?.accountNumber}")
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    Row {
                        Text(text = "Balance: ")
                        Text(text = "${getBalanceState.item?.balance}")
                    }
                    Spacer(modifier = modifier.height(10.dp))

                    Button(
                        onClick = {
                            openBalanceDialog.value = false
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue
                        )
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }

    if (openPinHistoryDialog.value) {
        BottomSheet(
            pinValue = customerPin,
            onDismissRequest = {
                openPinHistoryDialog.value = false
                customerPin.value = TextFieldValue("")
            },
            onConfirmRequest = {
                coroutineScope.launch {
                    val loginRequest = LoginRequest(
                        customer?.customerId!!,
                        customerPin.value.text
                    )
                    homeViewModel.verifyPin(loginRequest, context)
                }
            },
            modifier = modifier,
            isLoading = verifyPinState.isLoading
        )
    }

    if (openPinSendDialog.value) {
        BottomSheet(
            pinValue = customerPin,
            onDismissRequest = {
                openPinSendDialog.value = false
                customerPin.value = TextFieldValue("")
            },
            onConfirmRequest = {
                coroutineScope.launch {
                    val loginRequest = LoginRequest(
                        customer?.customerId!!,
                        customerPin.value.text
                    )
                    homeViewModel.verifyPin(loginRequest, context)
                }
            },
            modifier = modifier,
            isLoading = verifyPinState.isLoading
        )
    }

    //Verify pin when sending money
    LaunchedEffect(verifyPinState) {
        action(
            verifyBalancePinState = verifyPinState,
            context = context,
            navigate = {
                navController.navigate(SendMoneyRoute)
            },
            openPinDialog = openPinSendDialog
        )
    }

    //Verify pin when getting history
    LaunchedEffect(getHistoryPinState) {
        action(
            verifyBalancePinState = getHistoryPinState,
            context = context,
            navigate = {
                navController.navigate(HistoryRoute(pin = customerPin.value.text))
            },
            openPinDialog = openPinHistoryDialog
        )
    }

    //Get Balance states
    LaunchedEffect(getBalanceState) {
        if (getBalanceState.item?.accountNumber != null) {
            openBalanceDialog.value = true
        } else if (!getBalanceState.error.isNullOrEmpty()) {
            Toast.makeText(context, getBalanceState.error, Toast.LENGTH_SHORT).show()
        }
    }

    if (getBalanceState.isLoading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp, color = PrimaryBlue)
        }
    }
}

@Composable
fun ColumnItem(
    onclick: () -> Unit,
    modifier: Modifier,
    desc: String
) {
    Surface(
        onClick = {
            onclick()
        },
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = desc)
        }
    }
}

private fun action(
    verifyBalancePinState: UiState<LoginResponse>,
    context: Context,
    navigate: () -> Unit,
    openPinDialog: MutableState<Boolean>
) {
    when {
        verifyBalancePinState.error?.isNotEmpty() == true -> {
            Toast.makeText(context, verifyBalancePinState.error, Toast.LENGTH_SHORT).show()
        }

        verifyBalancePinState.item?.customerId != null -> {
            openPinDialog.value = false
            navigate()
        }
    }
}
