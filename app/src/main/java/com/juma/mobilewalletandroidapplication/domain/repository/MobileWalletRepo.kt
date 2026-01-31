package com.juma.mobilewalletandroidapplication.domain.repository

import com.juma.mobilewalletandroidapplication.dtos.AccountBalanceRequest
import com.juma.mobilewalletandroidapplication.dtos.AccountBalanceResponse
import com.juma.mobilewalletandroidapplication.dtos.LoginRequest
import com.juma.mobilewalletandroidapplication.dtos.LoginResponse
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyRequest
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyResponse
import com.juma.mobilewalletandroidapplication.dtos.TransactionHistoryRequest
import com.juma.mobilewalletandroidapplication.dtos.TransactionHistoryResponse
import com.juma.mobilewalletandroidapplication.utils.Resources
import kotlinx.coroutines.flow.Flow

interface MobileWalletRepo {

    //Login
    suspend fun login(loginRequest: LoginRequest): Flow<Resources<LoginResponse>>
    suspend fun getBalance(accountBalanceRequest: AccountBalanceRequest): Flow<Resources<AccountBalanceResponse>>
    suspend fun getTransactionHistory(transactionHistoryRequest: TransactionHistoryRequest): Flow<Resources<List<TransactionHistoryResponse>>>
    suspend fun sendMoney(sendMoneyRequest: SendMoneyRequest): SendMoneyResponse?
}