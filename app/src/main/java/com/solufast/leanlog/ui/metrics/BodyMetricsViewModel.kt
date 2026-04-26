package com.solufast.leanlog.ui.metrics

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.solufast.leanlog.data.db.entity.BodyMetricEntity
import com.solufast.leanlog.data.repository.BodyMetricRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.log10

private const val PREFS_NAME = "leanlog_prefs"
private const val PREF_HEIGHT_CM = "height_cm"
private const val PREF_IS_MALE = "is_male"

data class MetricsFormState(
    val weightKg: String = "",
    val waistCm: String = "",
    val neckCm: String = "",
    val heightCm: String = "",
    val isMale: Boolean = true,
    val bodyFatPercent: String = "",
    val navyBfEstimate: Double? = null,
    val savedMessage: String? = null,
    val errorMessage: String? = null
)

class BodyMetricsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = BodyMetricRepository(application)
    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _metrics = MutableStateFlow<List<BodyMetricEntity>>(emptyList())
    val metrics: StateFlow<List<BodyMetricEntity>> = _metrics.asStateFlow()

    private val _formState = MutableStateFlow(
        MetricsFormState(
            heightCm = prefs.getFloat(PREF_HEIGHT_CM, 171f).let {
                if (it == 171f && !prefs.contains(PREF_HEIGHT_CM)) "" else "%.0f".format(it)
            },
            isMale = prefs.getBoolean(PREF_IS_MALE, true)
        )
    )
    val formState: StateFlow<MetricsFormState> = _formState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getAllMetrics().collect { _metrics.value = it }
        }
    }

    fun updateWeight(v: String) { _formState.value = _formState.value.copy(weightKg = v) }
    fun updateWaist(v: String) {
        _formState.value = _formState.value.copy(waistCm = v)
        recalcNavy()
    }
    fun updateNeck(v: String) {
        _formState.value = _formState.value.copy(neckCm = v)
        recalcNavy()
    }
    fun updateBodyFat(v: String) { _formState.value = _formState.value.copy(bodyFatPercent = v) }
    fun updateHeight(v: String) {
        _formState.value = _formState.value.copy(heightCm = v)
        v.toFloatOrNull()?.let { prefs.edit().putFloat(PREF_HEIGHT_CM, it).apply() }
        recalcNavy()
    }
    fun updateIsMale(male: Boolean) {
        _formState.value = _formState.value.copy(isMale = male)
        prefs.edit().putBoolean(PREF_IS_MALE, male).apply()
        recalcNavy()
    }

    // US Navy body fat formula (male):
    //   BF% = 86.010 * log10(waist - neck) - 70.041 * log10(height) + 36.76
    // Female: requires hip measurement — not included in MVP, use male formula as fallback.
    private fun recalcNavy() {
        val f = _formState.value
        val waist = f.waistCm.toDoubleOrNull() ?: return
        val neck = f.neckCm.toDoubleOrNull() ?: return
        val height = f.heightCm.toDoubleOrNull() ?: return

        if (waist <= neck || height <= 0) {
            _formState.value = _formState.value.copy(navyBfEstimate = null)
            return
        }

        val bf = 86.010 * log10(waist - neck) - 70.041 * log10(height) + 36.76
        val clamped = bf.coerceIn(1.0, 60.0)
        _formState.value = _formState.value.copy(
            navyBfEstimate = clamped,
            bodyFatPercent = "%.1f".format(clamped)
        )
    }

    fun saveMetric() {
        viewModelScope.launch {
            val f = _formState.value
            val weight = f.weightKg.toDoubleOrNull()
            val waist = f.waistCm.toDoubleOrNull()
            val neck = f.neckCm.toDoubleOrNull()
            val bf = f.bodyFatPercent.toDoubleOrNull()

            if (weight == null && waist == null && neck == null && bf == null) {
                _formState.value = f.copy(errorMessage = "Enter at least one value.")
                return@launch
            }

            repo.insertMetric(
                BodyMetricEntity(
                    dateEpochDay = LocalDate.now().toEpochDay(),
                    weightKg = weight,
                    waistCm = waist,
                    neckCm = neck,
                    bodyFatPercent = bf
                )
            )
            val savedHeight = f.heightCm.toFloatOrNull()
            if (savedHeight != null) prefs.edit().putFloat(PREF_HEIGHT_CM, savedHeight).apply()

            _formState.value = MetricsFormState(
                heightCm = f.heightCm,
                isMale = f.isMale,
                savedMessage = "Metric saved."
            )
        }
    }

    fun clearMessages() {
        _formState.value = _formState.value.copy(savedMessage = null, errorMessage = null)
    }
}
