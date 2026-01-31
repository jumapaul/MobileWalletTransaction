package com.juma.mobilewalletandroidapplication.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val customerAccount: String? = null,
    val customerEmail: String? = null,
    val customerId: String? = null,
    val customerName: String? = null
)