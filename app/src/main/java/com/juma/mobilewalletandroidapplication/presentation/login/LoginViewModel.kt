package com.juma.mobilewalletandroidapplication.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.dtos.LoginRequest
import com.juma.mobilewalletandroidapplication.dtos.LoginResponse
import com.juma.mobilewalletandroidapplication.presentation.ui_states.UiState
import com.juma.mobilewalletandroidapplication.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val datastoreUtil: DatastoreUtil,
    private val mobileWalletRepo: MobileWalletRepo
) : ViewModel() {

    private val _uiState = mutableStateOf(UiState<LoginResponse>())
    val uiState: State<UiState<LoginResponse>> = _uiState

    suspend fun login(loginRequest: LoginRequest, context: Context) {
        mobileWalletRepo.login(loginRequest).onEach { result ->

            when (result) {
                is Resources.Error -> {
                    _uiState.value = UiState(
                        error = result.message,
                        isLoading = false
                    )
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }

                is Resources.Loading -> {
                    _uiState.value = UiState(
                        isLoading = true
                    )
                }

                is Resources.Success -> {
                    _uiState.value = UiState(
                        item = result.data
                    )
                    datastoreUtil.saveCustomerData("data", result.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}