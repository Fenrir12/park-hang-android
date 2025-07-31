package com.parkhang.mobile.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.layout.CornerRadius
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.layout.Layout.Spacing.Small
import com.parkhang.core.designsystem.layout.Padding
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.theme.CustomColors.Transparencies.White
import com.parkhang.core.designsystem.theme.ParkHangTheme
import com.parkhang.core.designsystem.typography.CustomTextStyle
import kotlinx.coroutines.delay
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    isTransparent: Boolean = false,
    titleTextStyle: TextStyle = CustomTextStyle.Heading4,
    title: @Composable () -> Unit = {},
    centeredTitle: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    topBarMaxHeight: Dp = 72.dp,
    topBarHorizontalPadding: Dp = Padding.Small.L,
    topBarTopPadding: Dp = Padding.Small.S,
    topBarBottomPadding: Dp = Padding.Small.S,
) {
    val actionsRow = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Small.Xs),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }

    Box(
        modifier = modifier
            .conditional(isTransparent) {
                Modifier
                    .background(Color.Transparent)
            }.padding(
                horizontal = topBarHorizontalPadding,
            ).padding(
                top = topBarTopPadding,
                bottom = topBarBottomPadding,
            ),
    ) {
        TopBarLayout(
            modifier = Modifier
                .clipToBounds(),
            height = topBarMaxHeight,
            titleContentColor = White,
            title = title,
            titleTextStyle = titleTextStyle,
            titleHorizontalArrangement =
                if (centeredTitle) Arrangement.Center else Arrangement.Start,
            navigationIcon = navigationIcon,
            actions = actionsRow,
        )
    }
}

@Composable
private fun TopBarLayout(
    modifier: Modifier,
    height: Dp,
    titleContentColor: Color,
    title: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    titleHorizontalArrangement: Arrangement.Horizontal,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    // Assemble the layout composable to support the centralized text even when there are navigation and action items
    Row {
        Layout(
            content = {
                Box {
                    CompositionLocalProvider(
                        content = navigationIcon,
                    )
                }
                Box {
                    ProvideTextStyle(value = titleTextStyle) {
                        CompositionLocalProvider(
                            LocalContentColor provides titleContentColor,
                            content = title,
                        )
                    }
                }
                Box {
                    CompositionLocalProvider(
                        content = actions,
                    )
                }
            },
            modifier = modifier,
        ) { measurables, constraints ->
            // Capture all the 3 component of the TopBar
            val (navigationIconMeasurable, titleMeasurable, actionsMeasurable) = measurables

            // Measure all the 3 components
            val navigationIconPlaceable =
                navigationIconMeasurable
                    .measure(constraints.copy(minWidth = 0))

            val actionIconsPlaceable =
                actionsMeasurable
                    .measure(constraints.copy(minWidth = 0))

            // Capture the TopBar width
            val width = constraints.maxWidth

            /*
             * If there are icons, the title width is the TopBar width minus the composable with the biggest width
             * e.g N - navigation icon, T - title area, A - action icon, X - empty space, B - blocked by calculation
             * In the following cases, the empty area is the available space for the title
             * the blocked area, is the area blocked to make sure the title is in the middle
             * TopBar with navigation and action item = N XXX T XXX A -> Title is in the middle
             * TopBar with only navigation item = N XXX T XXX B -> Title is in the middle
             * TopBar with only action item = B XXX T XXX A -> Title is in the middle
             * TopBar with 1 navigation item and 2 action items = N BXX T XX AA -> Title is in the middle
             * TopBar with 2 navigation items and 1 action item = NN XX T XXB A -> Title is in the middle
             */
            val titleWidth = if (navigationIconPlaceable.width == 0 || actionIconsPlaceable.width == 0) {
                width - max(navigationIconPlaceable.width, actionIconsPlaceable.width)
            } else {
                width - (navigationIconPlaceable.width + actionIconsPlaceable.width)
            }

            // Measure the title adding the new width
            val titlePlaceable = titleMeasurable.measure(
                if (titleWidth > 0) {
                    constraints.copy(
                        minWidth = 0,
                        maxWidth = titleWidth,
                    )
                } else {
                    constraints.copy(
                        minWidth = max(constraints.minWidth, 0),
                        maxWidth = max(constraints.maxWidth, 0),
                    )
                },
            )

            val layoutHeight = height
                .toPx()
                .roundToInt()

            layout(constraints.maxWidth, layoutHeight) {
                // Navigation icon
                navigationIconPlaceable.placeRelative(
                    x = 0,
                    y = (layoutHeight - navigationIconPlaceable.height) / 2,
                )

                // Title
                titlePlaceable.placeRelative(
                    x = when (titleHorizontalArrangement) {
                        Arrangement.Center -> (constraints.maxWidth - titlePlaceable.width) / 2
                        else -> navigationIconPlaceable.width
                    },
                    y = (layoutHeight - titlePlaceable.height) / 2,
                )

                // Action icons
                actionIconsPlaceable.placeRelative(
                    x = constraints.maxWidth - actionIconsPlaceable.width,
                    y = (layoutHeight - actionIconsPlaceable.height) / 2,
                )
            }
        }
    }
}

@Composable
fun TopBarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = CustomColors.Neutrals.Black60,
        contentColor = White,
    ),
    icon: @Composable () -> Unit = {},
    text: String = "",
    textStyle: TextStyle = CustomTextStyle.Heading4,
    enable: Boolean = true,
    addBorder: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Padding.Small.M,
        vertical = Padding.Small.M,
    ),
    content: @Composable (() -> Unit)? = null,
) {
    var isButtonEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(isButtonEnabled) {
        if (isButtonEnabled.not()) {
            delay(DISABLE_BUTTON_DURATION_1_SECOND)
            isButtonEnabled = true
        }
    }

    Button(
        onClick = {
            onClick()
            isButtonEnabled = false
        },
        modifier = modifier
            .height(Layout.Spacing.Medium.L)
            .defaultMinSize(Layout.Spacing.Medium.L)
            .conditional(condition = text.isEmpty() && content == null) {
                Modifier.widthIn(min = Layout.Spacing.Medium.S)
            }.then(
                if (addBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = CustomColors.Border.StrokeTint,
                        shape = CornerRadius.Rounded,
                    )
                } else {
                    Modifier
                },
            ),
        enabled = enable && isButtonEnabled,
        shape = CornerRadius.Rounded,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        if (content != null) {
            content()
        } else if (text.isNotEmpty()) {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier
                    .padding(horizontal = Padding.Small.Xs),
            )
        }

        icon()
    }
}

@Composable
fun TopBarButtonIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tintColor: Color? = null,
    contentScale: ContentScale = ContentScale.None,
) {
    Image(
        painter = painterResource(id = icon),
        colorFilter = if (tintColor == null) null else ColorFilter.tint(color = tintColor),
        contentDescription = contentDescription,
        modifier = modifier
            .widthIn(Layout.Spacing.Medium.S)
            .size(Layout.Spacing.Medium.S),
        contentScale = contentScale,
    )
}

@Preview
@Composable
private fun TopBarWithoutBorderPreview() {
    ParkHangTheme {
        TopBar(
            title = {
                Text(
                    text = "Park Hangouts",
                    style = CustomTextStyle.Heading2
                        .copy(color = White),
                )
            },
            centeredTitle = true,
            navigationIcon = {
                TopBarButton(
                    icon = {
                        TopBarButtonIcon(
                            icon = Icons.Navigation.Back.outlined,
                            tintColor = White,
                            contentDescription = "back button",
                        )
                    },
                    onClick = {},
                    addBorder = false,
                )
            },
            actions = {
                TopBarButton(
                    icon = {
                        TopBarButtonIcon(
                            icon = Icons.Navigation.Options.outlined,
                            tintColor = White,
                            contentDescription = "options",
                        )
                    },
                    onClick = {},
                    addBorder = false,
                )
            },
        )
    }
}

private const val DISABLE_BUTTON_DURATION_1_SECOND = 1000L
