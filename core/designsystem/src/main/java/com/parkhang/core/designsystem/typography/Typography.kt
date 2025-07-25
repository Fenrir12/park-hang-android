package com.parkhang.core.designsystem.typography

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import com.parkhang.core.designsystem.R
import com.parkhang.core.designsystem.theme.CustomColors

val proximaNovaFamily =
    FontFamily(
        Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
        Font(R.font.nunito_bold, FontWeight.Bold),
        Font(R.font.nunito_semibold, FontWeight.SemiBold),
        Font(R.font.nunito_regular, FontWeight.Normal),
        Font(R.font.nunito_light, FontWeight.Light),
        Font(R.font.nunito_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.nunito_black_regular, FontWeight.Black, FontStyle.Normal),
    )

/**
 * [CustomTextStyle] containing the custom text styles defined on figma.
 */
object CustomTextStyle {
    val Heading0 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = FontSize.ExtraExtraExtraLarge,
            lineHeight = LineHeight.Heading,
            letterSpacing = LetterSpacing.ExtraExtraExtraTight,
        )

    val Heading1 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = FontSize.ExtraExtraLarge,
            lineHeight = LineHeight.Heading,
            letterSpacing = LetterSpacing.ExtraExtraTight,
        )

    val Heading2 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = FontSize.ExtraLarge,
            lineHeight = LineHeight.Heading,
            letterSpacing = LetterSpacing.ExtraTight,
        )

    val Heading3 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.Bold,
            fontSize = FontSize.Large,
            lineHeight = LineHeight.Reset,
            letterSpacing = LetterSpacing.Tight,
        )

    val Heading4 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.Bold,
            fontSize = FontSize.Medium,
            lineHeight = LineHeight.Reset,
            letterSpacing = LetterSpacing.Reset,
        )

    val Body1 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = FontSize.Base,
            lineHeight = LineHeight.Body,
            letterSpacing = LetterSpacing.Reset,
        )

    val Body2 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.Normal,
            fontSize = FontSize.Base,
            lineHeight = LineHeight.Body,
            letterSpacing = LetterSpacing.Reset,
        )

    val Body3 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = FontSize.Small,
            lineHeight = LineHeight.Body,
            letterSpacing = LetterSpacing.Reset,
        )

    val Body4 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.Normal,
            fontSize = FontSize.Small,
            lineHeight = LineHeight.Body,
            letterSpacing = LetterSpacing.Reset,
        )

    val Body5 =
        TextStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = FontSize.ExtraSmall,
            lineHeight = LineHeight.Body,
            letterSpacing = LetterSpacing.Reset,
        )
}

object CustomShadowStyle {
    val Shadow4 =
        Shadow(
            color = CustomColors.Transparencies.Black,
            offset = Offset(2f, 2f),
            blurRadius = 4f,
        )
}

object CustomSpanStyle {
    val Body1SpanStyle =
        SpanStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = FontSize.Base,
            letterSpacing = LetterSpacing.Reset,
        )

    val UnderlinedBody1SpanStyle =
        SpanStyle(
            fontFamily = proximaNovaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = FontSize.Base,
            letterSpacing = LetterSpacing.Reset,
            textDecoration = TextDecoration.Underline,
        )
}

// Reference: https://material.io/blog/migrating-material-3
internal val Typography =
    Typography(
        displaySmall = CustomTextStyle.Heading0,
        headlineLarge = CustomTextStyle.Heading1,
        headlineMedium = CustomTextStyle.Heading2,
        headlineSmall = CustomTextStyle.Heading3,
        titleLarge = CustomTextStyle.Heading4,
        bodyLarge = CustomTextStyle.Body1,
        bodyMedium = CustomTextStyle.Body2,
        bodySmall = CustomTextStyle.Body3,
        labelLarge = CustomTextStyle.Body4,
        labelSmall = CustomTextStyle.Body5,
    )

@PreviewFontScale
@Preview
@Composable
fun PreviewCustomTextStyle() {
    Column {
        Text(
            text = "H0 Heading\nProxima Nova Extra Bold 80",
            style = CustomTextStyle.Heading0,
        )
        Text(
            text = "H1 Heading\nProxima Nova Extra Bold 48",
            style = CustomTextStyle.Heading1,
        )
        Text(
            text = "H2 Heading\nProxima Nova Extra Bold 32",
            style = CustomTextStyle.Heading2,
        )
        Text(
            text = "H3 Heading\nProxima Nova Bold 24",
            style = CustomTextStyle.Heading3,
        )
        Text(
            text = "H4 Heading\nProxima Nova Bold 20",
            style = CustomTextStyle.Heading4,
        )
        Text(
            text = "B1\nProxima Nova Semi Bold 16",
            style = CustomTextStyle.Body1,
        )
        Text(
            text = "B2\nProxima Nova Normal 16",
            style = CustomTextStyle.Body2,
        )
        Text(
            text = "B3\nProxima Nova Semi Bold 14",
            style = CustomTextStyle.Body3,
        )
        Text(
            text = "B4\nProxima Nova Normal 14",
            style = CustomTextStyle.Body4,
        )
        Text(
            text = "B5\nProxima Nova Normal 12",
            style = CustomTextStyle.Body5,
        )
    }
}
