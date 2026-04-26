package com.solufast.leanlog.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.solufast.leanlog.data.db.entity.ExerciseEntryEntity
import com.solufast.leanlog.data.db.entity.WorkoutSessionEntity
import com.solufast.leanlog.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class SessionWithExercises(
    val session: WorkoutSessionEntity,
    val exercises: List<ExerciseEntryEntity>
)

class WorkoutHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = WorkoutRepository(application)

    private val _items = MutableStateFlow<List<SessionWithExercises>>(emptyList())
    val items: StateFlow<List<SessionWithExercises>> = _items.asStateFlow()

    private val _expandedIds = MutableStateFlow<Set<Long>>(emptySet())
    val expandedIds: StateFlow<Set<Long>> = _expandedIds.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getAllSessions().collect { sessions ->
                val ids = sessions.map { it.id }
                if (ids.isEmpty()) {
                    _items.value = emptyList()
                    return@collect
                }
                // Load exercises for all sessions at once
                repo.getExercisesForSessions(ids).collect { exercises ->
                    val exercisesBySession = exercises.groupBy { it.sessionId }
                    _items.value = sessions.map { session ->
                        SessionWithExercises(
                            session = session,
                            exercises = exercisesBySession[session.id] ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    fun toggleExpanded(sessionId: Long) {
        _expandedIds.value = _expandedIds.value.toMutableSet().apply {
            if (contains(sessionId)) remove(sessionId) else add(sessionId)
        }
    }

    fun deleteSession(session: WorkoutSessionEntity) {
        viewModelScope.launch { repo.deleteSession(session) }
    }
}
