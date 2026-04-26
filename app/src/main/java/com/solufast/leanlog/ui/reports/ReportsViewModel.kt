package com.solufast.leanlog.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.solufast.leanlog.data.repository.BodyMetricRepository
import com.solufast.leanlog.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ChartPoint(val epochDay: Long, val value: Float)

data class ReportsState(
    val weightPoints: List<ChartPoint> = emptyList(),
    val waistPoints: List<ChartPoint> = emptyList(),
    val caloriePoints: List<ChartPoint> = emptyList()
)

class ReportsViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutRepo = WorkoutRepository(application)
    private val metricRepo = BodyMetricRepository(application)

    private val _state = MutableStateFlow(ReportsState())
    val state: StateFlow<ReportsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val metrics = metricRepo.getAllMetricsOnce()
            val sessions = workoutRepo.getAllSessionsOnce()

            val weightPoints = metrics
                .filter { it.weightKg != null }
                .map { ChartPoint(it.dateEpochDay, it.weightKg!!.toFloat()) }

            val waistPoints = metrics
                .filter { it.waistCm != null }
                .map { ChartPoint(it.dateEpochDay, it.waistCm!!.toFloat()) }

            val caloriePoints = sessions
                .filter { it.totalCalories != null }
                .map { ChartPoint(it.dateEpochDay, it.totalCalories!!.toFloat()) }

            _state.value = ReportsState(
                weightPoints = weightPoints,
                waistPoints = waistPoints,
                caloriePoints = caloriePoints
            )
        }
    }
}
