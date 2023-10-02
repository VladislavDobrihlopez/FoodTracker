package com.voitov.common_ui

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit

@Composable
fun HyperLinkText(
    text: String,
    textsShouldBeLinkedToLinks: Map<String, String>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    linkTextColor: Color = MaterialTheme.colors.primaryVariant,
    linkTextWeight: FontWeight = FontWeight.Normal,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
) {
    val formattedText = buildAnnotatedString {
        append(text)
        textsShouldBeLinkedToLinks.forEach { entry ->
            val start = text.indexOf(entry.key)
            if (start == -1) {
                return@forEach
            }
            val end = start + entry.key.length
            addStyle(
                SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextWeight,
                    textDecoration = linkTextDecoration
                ),
                start = start,
                end = end
            )
            addStringAnnotation(tag = "URL", annotation = entry.value, start = start, end = end)
        }
        addStyle(style = SpanStyle(fontSize = fontSize), start = 0, end = text.length)
    }
    val uriHandler = LocalUriHandler.current

    ClickableText(modifier = modifier, text = formattedText) { characterOffset ->
        formattedText.getStringAnnotations(characterOffset, characterOffset)
            .firstOrNull()?.let { annotation ->
                uriHandler.openUri(annotation.item)
            }
    }
}