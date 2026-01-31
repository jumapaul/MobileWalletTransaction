package com.juma.mobilewalletandroidapplication.presentation.ui_states

data class UiState<T>(
    val isLoading: Boolean = false,
    val item: T? = null,
    val error: String? = null
)

data class ListUiState<T>(
    val isLoading: Boolean = false,
    val item: List<T>? = null,
    val error: String? = null
)