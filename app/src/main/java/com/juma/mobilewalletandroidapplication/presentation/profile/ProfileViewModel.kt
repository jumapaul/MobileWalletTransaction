package com.juma.mobilewalletandroidapplication.presentation.profile

import androidx.lifecycle.ViewModel
import com.juma.mobilewalletandroidapplication.domain.data_store.DatastoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    datastoreUtil: DatastoreUtil
): ViewModel() {

    val customer = datastoreUtil.getCustomerData("data")
}