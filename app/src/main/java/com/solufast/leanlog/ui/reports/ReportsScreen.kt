package com.solufast.leanlog.ui.reports

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(vm: ReportsViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Reports") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            ChartCard(
                title = "Workout Calories Over Time",
                points = state.caloriePoints,
                lineColor = MaterialTheme.colorScheme.primary,
                unit = "kcal"
            )

            ChartCard(
                title = "Weight Over Time",
                points = state.weightPoints,
                lineColor = MaterialTheme.colorScheme.tertiary,
                unit = "kg"
            )

            ChartCard(
                title = "Waist Over Time",
                points = state.waistPoints,
                lineColor = MaterialTheme.colorScheme.error,
                unit = "cm"
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ChartCard(
    title: String,
    points: List<ChartPoint>,
    lineColor: Color,
    unit: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            if (points.size < 2) {
                Text(
                    text = "Log more entries to see trends.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val minVal = points.minOf { it.value }
                val maxVal = points.maxOf { it.value }
                val valRange = (maxVal - minVal).coerceAtLeast(1f)

                val minDay = points.minOf { it.epochDay }
                val maxDay = points.maxOf { it.epochDay }
                val dayRange = (maxDay - minDay).coerceAtLeast(1L).toFloat()

                // Y-axis labels
                Text(
                    text = "max: ${"%.1f".format(maxVal)} $unit   min: ${"%.1f".format(minVal)} $unit",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val padding = 12f

                    // Grid lines
                    val gridColor = Color.Gray.copy(alpha = 0.2f)
                    for (i in 0..4) {
                        val y = padding + (h - 2 * padding) * i / 4f
                        drawLine(gridColor, Offset(padding, y), Offset(w - padding, y), strokeWidth = 1f)
                    }

                    // Line path
                    val path = Path()
                    points.forEachIndexed { idx, pt ->
                        val x = padding + (pt.epochDay - minDay) / dayRange * (w - 2 * padding)
                        val y = h - padding - (pt.value - minVal) / valRange * (h - 2 * padding)
                        if (idx == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    drawPath(path, lineColor, style = Stroke(width = 3f, cap = StrokeCap.Round))

                    // Dots
                    points.forEach { pt ->
                        val x = padding + (pt.epochDay - minDay) / dayRange * (w - 2 * padding)
                        val y = h - padding - (pt.value - minVal) / valRange * (h - 2 * padding)
                        drawCircle(lineColor, radius = 5f, center = Offset(x, y))
                    }
                }
            }
        }
    }
}
