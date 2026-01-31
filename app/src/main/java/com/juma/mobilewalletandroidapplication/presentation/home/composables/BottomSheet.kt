package com.juma.mobilewalletandroidapplication.presentation.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.juma.mobilewalletandroidapplication.presentation.common_composables.FilledOutlineButton
import com.juma.mobilewalletandroidapplication.presentation.common_composables.InputTextFieldComposable

@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    isLoading: Boolean,
    onConfirmRequest: () -> Unit,
    pinValue: MutableState<TextFieldValue>,
    modifier: Modifier
) {

    val passwordVisible = remember {
        mutableStateOf(false)
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,   // Full width
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 30.dp, topEnd = 30.dp
                ),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter pin to proceed",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )

                    Spacer(modifier = modifier.height(10.dp))
                    InputTextFieldComposable(
                        label = "1234",
                        inputText = pinValue,
                        passwordVisible = passwordVisible,
                        trailingIcon = {
                            val icon =
                                if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        FilledOutlineButton(
                            onClick = {
                                onDismissRequest()
                            },
                            desc = "Cancel",
                            modifier = modifier.weight(1f),
                        )
                        Spacer(modifier = modifier.width(10.dp))
                        FilledOutlineButton(
                            onClick = {
                                onConfirmRequest()
                            },
                            desc = "Continue",
                            isLoading = isLoading,
                            modifier = modifier.weight(1f),
                        )
                    }
                    Spacer(modifier = modifier.height(50.dp))
                }
            }
        }
    }
}