package com.parkhang.mobile.feature.view.components

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.parkhang.core.designsystem.layout.Layout
import com.parkhang.core.designsystem.theme.CustomColors
import com.parkhang.core.designsystem.typography.CustomTextStyle
import com.parkhang.mobile.feature.hangout.entity.Hangout

@Composable
fun HangoutContentCheckedIn(
    hangoutList: List<Hangout>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(),
        verticalArrangement = spacedBy(
            space = Layout.Spacing.Medium.S,
        ),
    ) {
        item {
            Text(
                text = "Hangouts",
                style = CustomTextStyle.Heading2
                    .copy(color = CustomColors.Transparencies.White),
            )
        }
        itemsIndexed(hangoutList) { _, hangout ->
            HangoutItem(
                hangout = hangout,
            )
        }
    }
}

@Preview
@Composable
fun HangoutContentCheckedInPreview() {
    HangoutContentCheckedIn(
        hangoutList = listOf(
            Hangout(
                ownerName = "John Doe",
                title = "Park Picnic",
                description = "Join us for a fun picnic at the park!",
                createdAt = "2023-10-01T12:00:00Z",
            ),
            Hangout(
                ownerName = "Jane Smith",
                title = "Evening Walk",
                description = "Let's take a relaxing walk in the evening.",
                createdAt = "2023-10-02T18:00:00Z",
            ),
        ),
    )
}
