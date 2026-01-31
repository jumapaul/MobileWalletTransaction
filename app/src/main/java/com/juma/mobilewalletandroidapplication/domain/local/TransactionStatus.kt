package com.juma.mobilewalletandroidapplication.domain.local

enum class TransactionStatus(val label: String) {
    QUEUED("Queued"),
    SYNCING("Syncing"),
    SYNCED("Synced"),
    FAILED("Failed")
}
