package com.juma.mobilewalletandroidapplication.domain.repository_impl

import android.util.Log
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.dtos.AccountBalanceRequest
import com.juma.mobilewalletandroidapplication.dtos.AccountBalanceResponse
import com.juma.mobilewalletandroidapplication.dtos.LoginRequest
import com.juma.mobilewalletandroidapplication.dtos.LoginResponse
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyRequest
import com.juma.mobilewalletandroidapplication.dtos.SendMoneyResponse
import com.juma.mobilewalletandroidapplication.dtos.TransactionHistoryRequest
import com.juma.mobilewalletandroidapplication.dtos.TransactionHistoryResponse
import com.juma.mobilewalletandroidapplication.utils.Constants.ACCOUNT_BALANCE
import com.juma.mobilewalletandroidapplication.utils.Constants.LOGIN_ENDPOINT
import com.juma.mobilewalletandroidapplication.utils.Constants.SEND_MONEY
import com.juma.mobilewalletandroidapplication.utils.Constants.TRANSACTION_HISTORY
import com.juma.mobilewalletandroidapplication.utils.NetworkRequestTemplates
import com.juma.mobilewalletandroidapplication.utils.Resources
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class MobileWalletRepoImpl(
    private val client: NetworkRequestTemplates
) : MobileWalletRepo {
    override suspend fun login(loginRequest: LoginRequest): Flow<Resources<LoginResponse>> =
        flow {
            emit(Resources.Loading())

            try {
                val (status, response) = client.postRequest<LoginRequest, LoginResponse>(
                    LOGIN_ENDPOINT,
                    loginRequest
                )

                if (status == HttpStatusCode.OK) {
                    emit(Resources.Success(response))
                } else {
                    emit(Resources.Error("Invalid credentials"))
                }
            } catch (io: IOException) {
                emit(Resources.Error("No internet"))
            }
        }

    override suspend fun getBalance(accountBalanceRequest: AccountBalanceRequest): Flow<Resources<AccountBalanceResponse>> =
        flow {
            emit(Resources.Loading())

            try {
                val (status, response) = client.postRequest<AccountBalanceRequest, AccountBalanceResponse>(
                    ACCOUNT_BALANCE,
                    accountBalanceRequest
                )

                if (status == HttpStatusCode.OK) {
                    emit(Resources.Success(response))
                } else {
                    emit(Resources.Error("User not found"))
                }
            } catch (io: IOException) {
                emit(Resources.Error("No internet"))
            }
        }

    override suspend fun getTransactionHistory(transactionHistoryRequest: TransactionHistoryRequest):
            Flow<Resources<List<TransactionHistoryResponse>>> = flow {
        emit(Resources.Loading())

        try {
            val (status, response) = client.postRequest<TransactionHistoryRequest, List<TransactionHistoryResponse>>(
                TRANSACTION_HISTORY,
                transactionHistoryRequest
            )
            if (status == HttpStatusCode.OK) {
                emit(Resources.Success(response))
            } else {
                emit(Resources.Error("An error occurred"))
            }
        } catch (io: IOException) {
            emit(Resources.Error("No internet"))
        }
    }

    override suspend fun sendMoney(sendMoneyRequest: SendMoneyRequest): SendMoneyResponse? {
        return try {
            val (status, response) = client.postRequest<SendMoneyRequest, SendMoneyResponse>(
                SEND_MONEY,
                sendMoneyRequest
            )

            response
        } catch (e: Exception) {
            Log.d("------->", "sendMoney: ${e.message}")
            null
        }
    }

    //    override suspend fun sendMoney(sendMoneyRequest: SendMoneyRequest): Flow<Resources<SendMoneyResponse>> =
//        flow {
//            emit(Resources.Loading())
//
//            try {
//                val (status, response) = client.postRequest<SendMoneyRequest, SendMoneyResponse>(
//                    SEND_MONEY,
//                    sendMoneyRequest
//                )
//
//                Log.d("-------->Transaction status", "getTransactionHistory: $status")
//                if (status == HttpStatusCode.OK) {
//                    emit(Resources.Success(response))
//                } else {
//                    emit(Resources.Error("An error occurred"))
//                }
//            } catch (io: IOException) {
//                emit(Resources.Error("No internet"))
//            }
//        }
}