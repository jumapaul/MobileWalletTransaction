package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SendMoneyResponse(
    val responseMessage: String,
    val status: String
)