package com.solufast.leanlog.ui.metrics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.solufast.leanlog.data.db.entity.BodyMetricEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyMetricsScreen(vm: BodyMetricsViewModel = viewModel()) {
    val formState by vm.formState.collectAsState()
    val metrics by vm.metrics.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy")

    LaunchedEffect(formState.savedMessage) {
        formState.savedMessage?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessages()
        }
    }
    LaunchedEffect(formState.errorMessage) {
        formState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessages()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Body Metrics") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(4.dp))
                SectionLabel("Log Today")
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Weight + Waist
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = formState.weightKg,
                                onValueChange = { vm.updateWeight(it) },
                                label = { Text("Weight (kg)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                            OutlinedTextField(
                                value = formState.waistCm,
                                onValueChange = { vm.updateWaist(it) },
                                label = { Text("Waist (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }

                        // Neck + Height
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = formState.neckCm,
                                onValueChange = { vm.updateNeck(it) },
                                label = { Text("Neck (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                            OutlinedTextField(
                                value = formState.heightCm,
                                onValueChange = { vm.updateHeight(it) },
                                label = { Text("Height (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        // Body Fat % (manual or auto-filled) + Navy estimate label
                        OutlinedTextField(
                            value = formState.bodyFatPercent,
                            onValueChange = { vm.updateBodyFat(it) },
                            label = { Text("Body Fat (%)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            supportingText = formState.navyBfEstimate?.let {
                                { Text("Navy estimate: ${"%.1f".format(it)}%  (auto-filled)") }
                            }
                        )

                        Button(
                            onClick = { vm.saveMetric() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(4.dp))
                SectionLabel("History")
            }

            if (metrics.isEmpty()) {
                item {
                    Text(
                        "No metrics logged yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(metrics, key = { it.id }) { metric ->
                    MetricHistoryRow(metric, dateFmt)
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun MetricHistoryRow(metric: BodyMetricEntity, dateFmt: DateTimeFormatter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = LocalDate.ofEpochDay(metric.dateEpochDay).format(dateFmt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                metric.weightKg?.let { MetricChip("Weight", "%.1f kg".format(it)) }
                metric.waistCm?.let { MetricChip("Waist", "%.1f cm".format(it)) }
                metric.neckCm?.let { MetricChip("Neck", "%.1f cm".format(it)) }
                metric.bodyFatPercent?.let { MetricChip("BF", "%.1f%%".format(it)) }
            }
        }
    }
}

@Composable
private fun MetricChip(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
