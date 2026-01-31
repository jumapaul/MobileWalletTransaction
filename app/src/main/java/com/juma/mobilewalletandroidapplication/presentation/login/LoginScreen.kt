package com.juma.mobilewalletandroidapplication.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.juma.mobilewalletandroidapplication.dtos.LoginRequest
import com.juma.mobilewalletandroidapplication.presentation.common_composables.FilledOutlineButton
import com.juma.mobilewalletandroidapplication.presentation.common_composables.InputTextFieldComposable
import com.juma.mobilewalletandroidapplication.presentation.navigation.HomeRoute
import com.juma.mobilewalletandroidapplication.presentation.navigation.LoginRoute
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val customerId = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val customerPin = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }
    val loginState by loginViewModel.uiState
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(loginState.item) {
        if (loginState.item != null) {
            navController.navigate(HomeRoute) {
                popUpTo(LoginRoute) { inclusive = true }
            }
        }
    }
    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Login in to your account",
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            )

            Spacer(modifier = modifier.height(30.dp))
            InputTextFieldComposable(
                title = "Enter customer ID",
                label = "CB001", inputText = customerId
            )

            InputTextFieldComposable(
                title = "Enter customer pin",
                label = "1234",
                inputText = customerPin,
                passwordVisible = passwordVisible,
                trailingIcon = {
                    val icon =
                        if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
            Spacer(modifier = modifier.height(10.dp))

            FilledOutlineButton(
                onClick = {
                    val request = LoginRequest(
                        customerId.value.text,
                        customerPin.value.text
                    )

                    coroutineScope.launch {
                        loginViewModel.login(request, context)
                    }
                },
                isLoading = loginState.isLoading,
                isEnabled = !loginState.isLoading,
                desc = "Login"
            )
        }
    }
}