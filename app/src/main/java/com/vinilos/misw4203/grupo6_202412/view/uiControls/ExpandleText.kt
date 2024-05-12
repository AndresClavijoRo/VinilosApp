package com.vinilos.misw4203.grupo6_202412.view.uiControls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.vinilos.misw4203.grupo6_202412.R

const val DEFAULT_MINIMUM_TEXT_LINE = 3

data class ExpandableTextOptions(
    val showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    val showLessStyle: SpanStyle = showMoreStyle,
    val textAlign: TextAlign? = null,
    val fontStyle: FontStyle? = null,
)

@Composable
fun buildExpandableText(
    text: String,
    isExpanded: Boolean,
    clickable: Boolean,
    lastCharIndex: Int,
    showMoreText: String,
    showLessText: String,
    expandableTextOptions: ExpandableTextOptions
): AnnotatedString {
    return buildAnnotatedString {
        if (clickable) {
            if (isExpanded) {
                // Display the full text and "Show Less" button when expanded.
                append(text)
                withStyle(style = expandableTextOptions.showLessStyle) { append(showLessText) }
            } else {
                // Display truncated text and "Show More" button when collapsed.
                val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreText.length)
                    .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                append(adjustText)
                withStyle(style = expandableTextOptions.showMoreStyle) { append(showMoreText) }
            }
        } else {
            // Display the full text when not clickable.
            append(text)
        }
    }
}

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
    expandableTextOptions: ExpandableTextOptions = ExpandableTextOptions(),
    fontSize: TextUnit
) {
    // State variables to track the expanded state, clickable state, and last character index.
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    val showMoreText = stringResource(R.string.show_more)
    val showLessText = stringResource(R.string.show_less)
    val style: TextStyle = LocalTextStyle.current

    // Box composable containing the Text composable.
    Box(modifier = Modifier
        .clickable(clickable) {
            isExpanded = !isExpanded
        }
        .then(modifier)
    ) {
        // Text composable with buildAnnotatedString to handle "Show More" and "Show Less" buttons.
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildExpandableText(
                text,
                isExpanded,
                clickable,
                lastCharIndex,
                showMoreText,
                showLessText,
                expandableTextOptions
            ),
            // Set max lines based on the expanded state.
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            fontStyle = expandableTextOptions.fontStyle,
            // Callback to determine visual overflow and enable click ability.
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = expandableTextOptions.textAlign,
            fontSize = fontSize
        )
    }
}