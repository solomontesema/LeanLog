package com.solufast.leanlog.ui.dashboard

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

data class DashboardState(
    val latestWeightKg: Double? = null,
    val latestWaistCm: Double? = null,
    val latestBodyFatPercent: Double? = null,
    val sessionsThisWeek: Int = 0,
    val caloriesThisWeek: Int = 0,
    val lastWorkoutDate: LocalDate? = null,
    val statusMessage: String = "Loading..."
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutRepo = WorkoutRepository(application)
    private val bodyMetricRepo = BodyMetricRepository(application)

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val latestMetric = bodyMetricRepo.getLatestMetric()
            val today = LocalDate.now()
            // Monday of current week
            val weekStart = today.minusDays(today.dayOfWeek.value.toLong() - 1)
            val sessionsThisWeek = workoutRepo.getSessionsSince(weekStart.toEpochDay())
            val latestSession = workoutRepo.getLatestSession()

            val weekCalories = sessionsThisWeek.sumOf { it.totalCalories ?: 0 }
            val lastDate = latestSession?.let { LocalDate.ofEpochDay(it.dateEpochDay) }

            val status = buildStatusMessage(
                hasMetrics = latestMetric != null,
                sessionsThisWeek = sessionsThisWeek.size,
                waistCm = latestMetric?.waistCm
            )

            _state.value = DashboardState(
                latestWeightKg = latestMetric?.weightKg,
                latestWaistCm = latestMetric?.waistCm,
                latestBodyFatPercent = latestMetric?.bodyFatPercent,
                sessionsThisWeek = sessionsThisWeek.size,
                caloriesThisWeek = weekCalories,
                lastWorkoutDate = lastDate,
                statusMessage = status
            )
        }
    }

    private fun buildStatusMessage(
        hasMetrics: Boolean,
        sessionsThisWeek: Int,
        waistCm: Double?
    ): String {
        if (!hasMetrics) return "No body metric logged yet."
        if (sessionsThisWeek == 0) return "No workout logged this week. Get moving."
        if (sessionsThisWeek >= 3) return "Good consistency this week."
        if (waistCm != null && waistCm > 90) return "Belly reduction project is active."
        return "Consistency beats hero workouts."
    }
}
