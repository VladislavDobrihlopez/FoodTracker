package com.voitov.onboarding_presentation.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.voitov.common_ui.HyperLinkText
import com.voitov.common_ui.LocalSpacing
import com.voitov.onboarding_presentation.components.ActionButton

@Composable
fun HelloScreen(onNavigate: () -> Unit) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h1
                )
                Spacer(Modifier.height(spacing.spaceLarge))
                ActionButton(text = stringResource(id = R.string.next)) {
                    onNavigate()
                }
            }
        }
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        HyperLinkText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = MaterialTheme.typography.body1.fontSize,
            text = stringResource(id = R.string.privacy_text, stringResource(id = R.string.for_linking_privacy_policy)),
            textsShouldBeLinkedToLinks = mapOf(
                Pair(
                    stringResource(id = R.string.for_linking_privacy_policy),
                    "https://doc-hosting.flycricket.io/jeza-food-diabetes-tracker-privacy-policy/1d6b967c-47b7-40f0-b776-76d34ab4404f/privacy"
                )
            )
        )
    }
}