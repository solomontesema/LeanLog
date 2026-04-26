package com.solufast.leanlog.data.repository

import android.content.Context
import com.solufast.leanlog.data.db.LeanLogDatabase
import com.solufast.leanlog.data.db.entity.ExerciseEntryEntity
import com.solufast.leanlog.data.db.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(context: Context) {

    private val dao = LeanLogDatabase.getInstance(context).workoutDao()

    suspend fun insertSession(session: WorkoutSessionEntity, exercises: List<ExerciseEntryEntity>) {
        val sessionId = dao.insertSession(session)
        val linked = exercises.map { it.copy(sessionId = sessionId) }
        if (linked.isNotEmpty()) dao.insertExercises(linked)
    }

    suspend fun deleteSession(session: WorkoutSessionEntity) = dao.deleteSession(session)

    suspend fun updateSession(session: WorkoutSessionEntity, exercises: List<ExerciseEntryEntity>) {
        dao.updateSession(session)
        dao.deleteExercisesForSession(session.id)
        val linked = exercises.map { it.copy(sessionId = session.id) }
        if (linked.isNotEmpty()) dao.insertExercises(linked)
    }

    suspend fun getSessionById(id: Long): WorkoutSessionEntity? = dao.getSessionById(id)

    suspend fun getExercisesForSessionOnce(sessionId: Long): List<ExerciseEntryEntity> =
        dao.getExercisesForSessionOnce(sessionId)

    fun getAllSessions(): Flow<List<WorkoutSessionEntity>> = dao.getAllSessions()

    fun getExercisesForSession(sessionId: Long): Flow<List<ExerciseEntryEntity>> =
        dao.getExercisesForSession(sessionId)

    fun getExercisesForSessions(sessionIds: List<Long>): Flow<List<ExerciseEntryEntity>> =
        dao.getExercisesForSessions(sessionIds)

    suspend fun getLatestSession(): WorkoutSessionEntity? = dao.getLatestSession()

    suspend fun countSessions(): Int = dao.countSessions()

    suspend fun getSessionsSince(fromDay: Long): List<WorkoutSessionEntity> =
        dao.getSessionsSince(fromDay)

    suspend fun getAllSessionsOnce(): List<WorkoutSessionEntity> = dao.getAllSessionsOnce()
}
