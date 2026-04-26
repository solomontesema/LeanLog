package com.solufast.leanlog.ui.workout

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.solufast.leanlog.data.db.entity.ExerciseEntryEntity
import com.solufast.leanlog.data.db.entity.WorkoutSessionEntity
import com.solufast.leanlog.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ExerciseDraft(
    val name: String = "",
    val weightKg: String = "",
    val sets: String = "",
    val reps: String = "",
    val difficulty: String = ""
)

data class AddWorkoutUiState(
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
    val errorMessage: String? = null
)

class AddWorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = WorkoutRepository(application)

    private var editingSessionId: Long? = null

    // Session header fields
    var date: LocalDate = LocalDate.now()
    var title: String = ""
    var totalCalories: String = ""
    var cardioMinutes: String = ""
    var notes: String = ""

    val exercises = mutableStateListOf<ExerciseDraft>()

    private val _uiState = MutableStateFlow(AddWorkoutUiState())
    val uiState: StateFlow<AddWorkoutUiState> = _uiState.asStateFlow()

    init {
        exercises.add(ExerciseDraft())
    }

    fun loadSessionForEdit(sessionId: Long) {
        if (editingSessionId == sessionId) return
        editingSessionId = sessionId
        viewModelScope.launch {
            val session = repo.getSessionById(sessionId) ?: return@launch
            val entries = repo.getExercisesForSessionOnce(sessionId)

            date = LocalDate.ofEpochDay(session.dateEpochDay)
            title = session.title ?: ""
            totalCalories = session.totalCalories?.toString() ?: ""
            cardioMinutes = session.cardioMinutes?.toString() ?: ""
            notes = session.notes ?: ""

            exercises.clear()
            if (entries.isEmpty()) {
                exercises.add(ExerciseDraft())
            } else {
                entries.forEach { e ->
                    exercises.add(
                        ExerciseDraft(
                            name = e.exerciseName,
                            weightKg = e.weightKg?.toString() ?: "",
                            sets = e.sets?.toString() ?: "",
                            reps = e.reps?.toString() ?: "",
                            difficulty = e.difficulty ?: ""
                        )
                    )
                }
            }
            _uiState.value = AddWorkoutUiState(isEditMode = true)
        }
    }

    fun addExerciseRow() { exercises.add(ExerciseDraft()) }

    fun updateExercise(index: Int, draft: ExerciseDraft) {
        if (index in exercises.indices) exercises[index] = draft
    }

    fun removeExercise(index: Int) {
        if (exercises.size > 1 && index in exercises.indices) exercises.removeAt(index)
    }

    fun saveSession() {
        viewModelScope.launch {
            val filledExercises = exercises.filter { it.name.isNotBlank() }
            if (filledExercises.isEmpty()) {
                _uiState.value = _uiState.value.copy(errorMessage = "Add at least one exercise.")
                return@launch
            }

            val entries = filledExercises.map { draft ->
                ExerciseEntryEntity(
                    sessionId = 0,
                    exerciseName = draft.name.trim(),
                    weightKg = draft.weightKg.trim().toDoubleOrNull(),
                    sets = draft.sets.trim().toIntOrNull(),
                    reps = draft.reps.trim().toIntOrNull(),
                    difficulty = draft.difficulty.trim().ifBlank { null }
                )
            }

            val existingId = editingSessionId
            if (existingId != null) {
                // Update existing session
                val session = WorkoutSessionEntity(
                    id = existingId,
                    dateEpochDay = date.toEpochDay(),
                    title = title.trim().ifBlank { null },
                    totalCalories = totalCalories.trim().toIntOrNull(),
                    cardioMinutes = cardioMinutes.trim().toIntOrNull(),
                    notes = notes.trim().ifBlank { null }
                )
                repo.updateSession(session, entries)
            } else {
                // Insert new session
                val session = WorkoutSessionEntity(
                    dateEpochDay = date.toEpochDay(),
                    title = title.trim().ifBlank { null },
                    totalCalories = totalCalories.trim().toIntOrNull(),
                    cardioMinutes = cardioMinutes.trim().toIntOrNull(),
                    notes = notes.trim().ifBlank { null }
                )
                repo.insertSession(session, entries)
            }
            _uiState.value = AddWorkoutUiState(isSaved = true, isEditMode = editingSessionId != null)
        }
    }

    fun clearError() { _uiState.value = _uiState.value.copy(errorMessage = null) }

    fun resetForm() {
        editingSessionId = null
        date = LocalDate.now()
        title = ""
        totalCalories = ""
        cardioMinutes = ""
        notes = ""
        exercises.clear()
        exercises.add(ExerciseDraft())
        _uiState.value = AddWorkoutUiState()
    }
}
