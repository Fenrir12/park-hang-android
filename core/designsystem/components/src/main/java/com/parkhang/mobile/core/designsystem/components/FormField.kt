package com.parkhang.mobile.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.CornerRadius
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.theme.ParkHangTheme
import com.parkhang.core.designsystem.typography.CustomTextStyle

/**
 * A composable form field with label, error handling, and customization options.
 *
 * @param text State for the field's content.
 * @param onValueChanged Callback for text changes.
 * @param shouldShowText Controls visibility of text input. Default is true.
 * @param label Label text for the field.
 * @param modifier Modifier for styling. Default is [Modifier].
 * @param imeAction IME action for the keyboard. Default is [ImeAction.Default].
 * @param keyboardType Keyboard type for input. Default is [KeyboardType.Text].
 * @param keyboardActions Actions for the IME key. Default is [KeyboardActions.Default].
 * @param supportingText Text displayed below the field, often for errors. Default is empty.
 * @param onFocusChanged Callback for focus state changes. Default is no-op.
 * @param hasError Indicates if the field is in an error state. Default is false.
 * @param isEnabled Enables or disables the field. Default is true.
 * @param readOnly Makes the field read-only. Default is false.
 * @param autoFocus Requests focus on initialization. Default is false.
 * @param maxLines Maximum visible lines. Default is 1.
 * @param maxCharacters Maximum allowed characters. Default is null.
 * @param supportingTextComponent Custom composable for supporting text. Default is null.
 */
@Composable
fun FormField(
    text: MutableState<String>,
    shouldShowText: Boolean = true,
    onValueChanged: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    supportingText: String = "",
    onFocusChanged: (Boolean) -> Unit = { },
    hasError: Boolean = false,
    isEnabled: Boolean = true,
    readOnly: Boolean = false,
    autoFocus: Boolean = false,
    maxLines: Int = 1,
    maxCharacters: Int? = null,
    supportingTextAlignment: Alignment.Vertical = Alignment.CenterVertically,
    supportingTextComponent: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text.value)) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(text.value) {
        textFieldValue = TextFieldValue(text.value, selection = TextRange(text.value.length))
    }

    LaunchedEffect(Unit) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier,
    ) {
        TextField(
            value = textFieldValue,
            visualTransformation = if (shouldShowText) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = CustomTextStyle.Heading4,
            onValueChange = { newValue ->
                if (maxCharacters == null || newValue.text.length <= maxCharacters) {
                    textFieldValue = newValue
                    onValueChanged(newValue.text)
                }
            },
            maxLines = maxLines,
            singleLine = maxLines == 1,
            enabled = isEnabled,
            readOnly = readOnly,
            label = {
                val style = getPlaceHolderTextStyle(isFocused = isFocused)
                PlaceholderLabel(
                    label = label,
                    style = style,
                )
            },
            keyboardOptions =
                KeyboardOptions(
                    imeAction = imeAction,
                    keyboardType = keyboardType,
                ),
            keyboardActions = keyboardActions,
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = CustomColors.Neutrals.White60,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            interactionSource = interactionSource,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = FORM_FIELD_BORDER_WIDTH.dp,
                        color =
                            borderColor(
                                hasError = hasError,
                                isFocused = isFocused,
                            ),
                        shape = CornerRadius.Large,
                    ).padding(
                        vertical = Padding.Small.S,
                    ).background(Color.Transparent)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
                        }
                    }.focusRequester(focusRequester),
        )
        SupportingTextLabel(
            supportingText = supportingText,
            hasError = hasError,
            supportingTextAlignment = supportingTextAlignment,
            supportingTextComponent = supportingTextComponent,
        )
    }
}

@Composable
private fun getPlaceHolderTextStyle(isFocused: Boolean): TextStyle {
    val style =
        if (isFocused) {
            CustomTextStyle.Body1
        } else {
            CustomTextStyle.Heading4
        }
    return style
}

@Composable
private fun SupportingTextLabel(
    modifier: Modifier = Modifier,
    supportingText: String = "",
    supportingIconColor: Color = CustomColors.Neutrals.White85,
    supportingTextAlignment: Alignment.Vertical = Alignment.CenterVertically,
    hasError: Boolean = false,
    supportingTextComponent: @Composable (() -> Unit)? = null,
) {
    val showSupportingText = hasError && (supportingTextComponent != null || supportingText.isNotEmpty())

    if (showSupportingText) {
        Row(
            modifier =
                modifier
                    .padding(
                        top = Padding.Small.S,
                    ),
            verticalAlignment = supportingTextAlignment,
        ) {
            Image(
                painter =
                    painterResource(
                        id = Icons.Functional.Warning,
                    ),
                contentDescription = "error field",
                colorFilter = ColorFilter.tint(supportingIconColor),
            )

            if (supportingTextComponent != null) {
                supportingTextComponent()
            } else {
                Text(
                    text = supportingText,
                    modifier =
                        Modifier
                            .padding(
                                horizontal = Padding.Small.Xs,
                                vertical = Padding.Small.XXs,
                            ),
                    color = CustomColors.Neutrals.White60,
                    style = CustomTextStyle.Body3,
                )
            }
        }
    }
}

/**
 * A component that renders a placeholder label for a form field.
 *
 * @param label The text to be used as the placeholder label.
 * @param modifier An optional [Modifier] applied to the label. Default is [Modifier].
 */
@Composable
private fun PlaceholderLabel(
    label: String,
    modifier: Modifier = Modifier,
    style: TextStyle = CustomTextStyle.Heading4,
) {
    Text(
        modifier = modifier,
        text = label,
        color = CustomColors.Neutrals.White60,
        style = style,
    )
}

/**
 * A Composable to determine the border color of the form field based on its error and focus states.
 *
 * @param hasError A boolean indicating if the form field is in an error state.
 * @param isFocused A boolean indicating if the form field is currently focused.
 * @return [Color] The color to be used for the form field's border.
 */
@Composable
private fun borderColor(
    hasError: Boolean,
    isFocused: Boolean,
): Color = when {
    hasError -> CustomColors.Status.Error
    isFocused -> CustomColors.Transparencies.White
    else -> CustomColors.Neutrals.White60
}

@Composable
@Preview
private fun Preview() {
    var email = "git@gud.ca"
    var pwd = "password"
    ParkHangTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(CustomColors.Primary.LightGreen),
        ) {
            FormField(
                text = remember { mutableStateOf("email") },
                onValueChanged = { newText -> email = newText },
                label = "Email",
            )
            Spacer(modifier = Modifier.size(30.dp))
            FormField(
                text = remember { mutableStateOf(pwd) },
                onValueChanged = { newText -> pwd = newText },
                shouldShowText = false,
                label = "Password",
                supportingText = "Please enter your email address in format: yourname@example.com",
                onFocusChanged = { isFocused -> },
                hasError = true,
            )
        }
    }
}

private const val FORM_FIELD_BORDER_WIDTH = 2
