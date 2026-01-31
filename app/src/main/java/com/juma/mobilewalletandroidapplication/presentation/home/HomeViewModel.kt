package com.juma.mobilewalletandroidapplication.presentation.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.dtos.AccountBalanceRequest
import com.juma.mobilewalletandroidapplication.dtos.AccountBalanceResponse
import com.juma.mobilewalletandroidapplication.dtos.LoginRequest
import com.juma.mobilewalletandroidapplication.dtos.LoginResponse
import com.juma.mobilewalletandroidapplication.presentation.ui_states.UiState
import com.juma.mobilewalletandroidapplication.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val datastoreUtil: DatastoreUtil,
    private val mobileWalletRepo: MobileWalletRepo
) : ViewModel() {

    var customer = datastoreUtil.getCustomerData("data")

    private val _pinState = mutableStateOf(UiState<LoginResponse>())
    val pinState: State<UiState<LoginResponse>> = _pinState

    private val _accountBalanceState = mutableStateOf(UiState<AccountBalanceResponse>())
    val accountBalanceState: State<UiState<AccountBalanceResponse>> = _accountBalanceState

    suspend fun verifyPin(loginRequest: LoginRequest, context: Context) {
        mobileWalletRepo.login(loginRequest).onEach { result ->
            when (result) {
                is Resources.Error -> {
                    _pinState.value = UiState(
                        error = result.message,
                        isLoading = false
                    )
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }

                is Resources.Loading -> {
                    _pinState.value = UiState(
                        isLoading = true
                    )
                }

                is Resources.Success -> {
                    Log.d("------>Login request", "verifyPin: $loginRequest")
                    _pinState.value = UiState(
                        item = result.data
                    )
                    datastoreUtil.saveCustomerData("data", result.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getBalance(context: Context) {
        val accountBalanceRequest = AccountBalanceRequest(
            customer?.customerId!!
        )
        mobileWalletRepo.getBalance(accountBalanceRequest).onEach { result ->
            when (result) {
                is Resources.Error -> {
                    _accountBalanceState.value = UiState(
                        error = result.message,
                        isLoading = false
                    )
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }

                is Resources.Loading -> {
                    _accountBalanceState.value = UiState(
                        isLoading = true
                    )
                }

                is Resources.Success -> {
                    _accountBalanceState.value = UiState(
                        item = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout(onClick: () -> Unit) {
        datastoreUtil.logout()
        onClick()
    }
}