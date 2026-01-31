package com.juma.mobilewalletandroidapplication.presentation.common_composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juma.mobilewalletandroidapplication.ui.theme.PrimaryBlue

@Composable
fun FilledOutlineButton(
    onClick: () -> Unit,
    desc: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    fontWeight: FontWeight? = null,
) {
    OutlinedButton(
        enabled = isEnabled,
        contentPadding = PaddingValues(15.dp),
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = PrimaryBlue,
            disabledContainerColor = PrimaryBlue.copy(0.5f)
        ),
        onClick = {
            onClick()
        }) {

        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = Color.White,
                modifier = modifier.size(30.dp)
            )
        } else {
            Text(
                desc,
                color = Color.White,
                fontWeight = fontWeight
            )
        }
    }
}