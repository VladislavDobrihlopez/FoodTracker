package com.voitov.onboarding_presentation.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.voitov.common_ui.LocalSpacing

@Composable
fun UnitEditText(
    value: String,
    unit: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(color = MaterialTheme.colors.primaryVariant, fontSize = 72.sp),
    onValueChange: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        BasicTextField(
            modifier = Modifier
                .padding(spacing.spaceSmall)
                .width(IntrinsicSize.Min),
            textStyle = textStyle,
            value = value,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange
        )
        Spacer(Modifier.width(spacing.spaceSmall))
        Text(
            text = unit,
            modifier = Modifier.alignBy(FirstBaseline),
            style = textStyle.copy(fontSize = 24.sp, color = MaterialTheme.colors.onBackground)
        )
    }
}
