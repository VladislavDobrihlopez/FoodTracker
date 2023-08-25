package com.voitov.tracker_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing

@Composable
fun SearchBar(
    text: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = stringResource(id = R.string.search),
    shouldShowHint: Boolean = true,
    textAndHintStyle: TextStyle = MaterialTheme.typography.body1,
    textColor: Color = MaterialTheme.colors.onSurface,
    maxLines: Int = 2,
) {
    val spacing = LocalSpacing.current
    Box(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .padding(2.dp)
                .shadow(2.dp, RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.surface)
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .padding(spacing.spaceMedium)
                .padding(end = spacing.spaceSmall)
                .onFocusChanged { onFocusChange(it) }
                .testTag("searchbar_textfield"),
            // to let text not overlap the search icon
            value = text,
            onValueChange = onValueChange,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(text)
                defaultKeyboardAction(ImeAction.Search)
            }),
        )
        if (shouldShowHint) {
            Text(
                modifier = Modifier
                    .padding(start = spacing.spaceSmall)
                    .align(Alignment.CenterStart),
                text = hint,
                color = textColor,
                style = textAndHintStyle
            )
        }
        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
            onSearch(text)
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        }
    }
}