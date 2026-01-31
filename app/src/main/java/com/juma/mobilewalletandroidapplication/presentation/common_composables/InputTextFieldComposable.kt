package com.juma.mobilewalletandroidapplication.presentation.common_composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun InputTextFieldComposable(
    label: String,
    inputText: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    title: String? = null,
    passwordVisible: MutableState<Boolean>? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textError: String? = null,
    onClick: (() -> Unit)? = null,
    maxLines: Int? = null
) {
    Column(
        modifier = if (onClick != null) {
            modifier.clickable { onClick() }
        } else modifier
    ) {
        Text(
            text = title ?: "",
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            modifier = modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Gray,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = Color.LightGray,
                focusedIndicatorColor = Color.LightGray
            ),
            placeholder = {
                Text(text = label, color = Color.LightGray)
            },
            supportingText = {
                if (textError != null) {
                    Text(text = textError, color = Color.Red)
                }
            },
            maxLines = maxLines ?: 1,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = trailingIcon,
            visualTransformation = if (passwordVisible?.value == false) PasswordVisualTransformation()
            else VisualTransformation.None
        )
    }
}