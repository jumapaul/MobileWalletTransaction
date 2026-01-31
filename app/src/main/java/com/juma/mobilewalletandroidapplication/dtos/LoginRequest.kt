package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val customerId: String,
    val pin: String
)