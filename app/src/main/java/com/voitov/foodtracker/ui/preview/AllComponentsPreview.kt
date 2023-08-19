package com.voitov.foodtracker.ui.preview

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.voitov.foodtracker.ui.theme.FoodTrackerTheme
import com.voitov.onboarding_presentation.components.SelectionButton
import com.voitov.onboarding_presentation.components.UnitEditText

@Preview(showBackground = true)
@Composable
internal fun PreviewUnitEditText() {
    FoodTrackerTheme {
        UnitEditText(value = "17", unit = "cm", onValueChange = {})
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewSelectionButton() {
    FoodTrackerTheme {
        SelectionButton(
            value = "first_option",
            {},
            isSelected = true,
            selectedTextColor = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewSelectionButtonDisabled() {
    FoodTrackerTheme {
        SelectionButton(
            value = "first_option",
            {},
            isSelected = false,
            selectedTextColor = MaterialTheme.colors.onPrimary
        )
    }
}