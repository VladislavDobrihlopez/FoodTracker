package com.voitov.onboarding_presentation.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing
import com.voitov.onboarding_presentation.welcome.components.ActionButton

@Composable
fun HelloScreen() {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.fillMaxSize().padding(spacing.spaceMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1
        )
        Spacer(Modifier.height(spacing.spaceLarge))
        ActionButton(text = stringResource(id = R.string.next)) {

        }
    }
}