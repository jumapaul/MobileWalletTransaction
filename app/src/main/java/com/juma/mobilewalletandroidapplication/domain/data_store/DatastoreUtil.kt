package com.juma.mobilewalletandroidapplication.domain.data_store

import android.content.SharedPreferences
import com.google.gson.Gson
import com.juma.mobilewalletandroidapplication.dtos.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveCustomerData(key: String, data: LoginResponse) {
        val editor = sharedPreferences.edit()
        val customer = Gson().toJson(data)
        editor.putString(key, customer)
        editor.apply()
    }

    fun getCustomerData(key: String): LoginResponse? {
        val data = sharedPreferences.getString(key, null) ?: return null

        return try {
            Gson().fromJson(data, LoginResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("data")
        editor.apply()
    }
}