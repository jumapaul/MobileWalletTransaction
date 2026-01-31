package com.juma.mobilewalletandroidapplication.presentation.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.dtos.TransactionHistoryRequest
import com.juma.mobilewalletandroidapplication.dtos.TransactionHistoryResponse
import com.juma.mobilewalletandroidapplication.presentation.ui_states.ListUiState
import com.juma.mobilewalletandroidapplication.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    datastoreUtil: DatastoreUtil,
    savedStateHandle: SavedStateHandle,
    private val mobileWalletRepo: MobileWalletRepo
) : ViewModel() {

    private val _historyState = mutableStateOf(ListUiState<TransactionHistoryResponse>())
    val historyState: State<ListUiState<TransactionHistoryResponse>> = _historyState

    private val customer = datastoreUtil.getCustomerData("data")

    val pin: String = savedStateHandle["pin"]!!

    init {
        viewModelScope.launch {
            getHistory()
        }
    }

    private suspend fun getHistory() {
        val transactionHistoryRequest = TransactionHistoryRequest(
            customer?.customerId!!,
            pin
        )
        mobileWalletRepo.getTransactionHistory(transactionHistoryRequest).onEach { result ->
            when (result) {
                is Resources.Error -> {
                    _historyState.value = ListUiState(
                        error = result.message,
                        isLoading = false
                    )
                }

                is Resources.Loading -> {
                    _historyState.value = ListUiState(
                        isLoading = true
                    )
                }

                is Resources.Success -> {
                    _historyState.value = ListUiState(
                        item = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}