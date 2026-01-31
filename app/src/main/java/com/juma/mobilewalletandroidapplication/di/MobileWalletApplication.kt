package com.juma.mobilewalletandroidapplication.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MobileWalletApplication : Application() {
//    @Inject
//    lateinit var hiltWorkerFactory: HiltWorkerFactory
//
//    override fun onCreate() {
//        super.onCreate()
//        WorkManager.getInstance(this, Configuration.Builder()
//            .setWorkerFactory(hiltWorkerFactory)
//            .build()
//        )
//    }
}