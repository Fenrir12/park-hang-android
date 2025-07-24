package com.parkhang.mobile.feature.parks.view.map.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.parkhang.core.designsystem.icons.Icons
import com.parkhang.core.designsystem.typography.CustomTextStyle

@Composable
fun ClusterCircleContent(
    clusterSizeText: String,
    clusterSize: Int,
    modifier: Modifier = Modifier,
) {
    val scaleFactor =
        when {
            clusterSize < 100 -> 0.77f
            clusterSize < 1000 -> 0.89f
            else -> 1f
        }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = Icons.Map.Cluster.Circle),
            contentDescription = "Cluster containing multiple pins",
            modifier = modifier.scale(scaleFactor),
        )
        Text(
            text = clusterSizeText,
            style = CustomTextStyle.Body2,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

class ClusterSizeProviderPreview : PreviewParameterProvider<String> {
    override val values: Sequence<String> =
        sequenceOf(
            "7",
            "18",
            "110",
            "1k",
            "3k",
            "12k",
        )
}

@Preview
@Composable
private fun ClusterCircleContentPreview(
    @PreviewParameter(ClusterSizeProviderPreview::class) clusterSizeText: String,
) {
    ClusterCircleContent(clusterSizeText = clusterSizeText, clusterSize = 1000)
}
