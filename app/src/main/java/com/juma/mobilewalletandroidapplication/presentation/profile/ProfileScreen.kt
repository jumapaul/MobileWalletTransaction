package com.juma.mobilewalletandroidapplication.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val customer = profileViewModel.customer
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Customer Profile", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                },

                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            ProfileItem(
                title = "Customer Name:",
                desc = customer?.customerName!!,
                modifier = modifier
            )
            ProfileItem(title = "Customer Id:", desc = customer.customerId!!, modifier = modifier)
            ProfileItem(
                title = "Customer Account:",
                desc = customer.customerAccount!!,
                modifier = modifier
            )
            ProfileItem(
                title = "Customer Email:",
                desc = customer.customerEmail!!,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ProfileItem(
    title: String,
    desc: String,
    modifier: Modifier
) {
    Row(
        modifier = modifier.padding(top = 10.dp)
    ) {
        Text(
            text = title, style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = modifier.width(10.dp))
        Text(text = desc)
    }
}