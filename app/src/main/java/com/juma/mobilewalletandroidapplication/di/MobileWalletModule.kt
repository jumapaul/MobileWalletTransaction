package com.juma.mobilewalletandroidapplication.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.work.WorkManager
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import com.juma.mobilewalletandroidapplication.domain.local.TransactionDao
import com.juma.mobilewalletandroidapplication.domain.local.TransactionDatabase
import com.juma.mobilewalletandroidapplication.domain.local.TransactionEntity
import com.juma.mobilewalletandroidapplication.domain.repository.MobileWalletRepo
import com.juma.mobilewalletandroidapplication.domain.repository_impl.MobileWalletRepoImpl
import com.juma.mobilewalletandroidapplication.utils.NetworkRequestTemplates
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MobileWalletModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    @Singleton
    @Provides
    fun provideMobileWalletRepo(client: NetworkRequestTemplates): MobileWalletRepo {
        return MobileWalletRepoImpl(client)
    }

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDataStoreUtil(sharedPreferences: SharedPreferences): DatastoreUtil {
        return DatastoreUtil(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTransactionDatabase(@ApplicationContext context: Context): TransactionDatabase {
        return Room.databaseBuilder(
            context,
            TransactionDatabase::class.java,
            "transaction.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideMovieDetailDao(transactionDatabase: TransactionDatabase): TransactionDao =
        transactionDatabase.getTransactionDao()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}