package com.solufast.leanlog.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.solufast.leanlog.data.db.entity.ExerciseEntryEntity
import com.solufast.leanlog.data.db.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntryEntity>)

    @Delete
    suspend fun deleteSession(session: WorkoutSessionEntity)

    @androidx.room.Update
    suspend fun updateSession(session: WorkoutSessionEntity)

    @Query("DELETE FROM exercise_entries WHERE sessionId = :sessionId")
    suspend fun deleteExercisesForSession(sessionId: Long)

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): WorkoutSessionEntity?

    @Query("SELECT * FROM exercise_entries WHERE sessionId = :sessionId ORDER BY id ASC")
    suspend fun getExercisesForSessionOnce(sessionId: Long): List<ExerciseEntryEntity>

    @Query("SELECT * FROM workout_sessions ORDER BY dateEpochDay DESC")
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM exercise_entries WHERE sessionId = :sessionId ORDER BY id ASC")
    fun getExercisesForSession(sessionId: Long): Flow<List<ExerciseEntryEntity>>

    @Query("SELECT * FROM exercise_entries WHERE sessionId IN (:sessionIds) ORDER BY sessionId ASC, id ASC")
    fun getExercisesForSessions(sessionIds: List<Long>): Flow<List<ExerciseEntryEntity>>

    @Query("SELECT * FROM workout_sessions ORDER BY dateEpochDay DESC LIMIT 1")
    suspend fun getLatestSession(): WorkoutSessionEntity?

    @Query("SELECT COUNT(*) FROM workout_sessions")
    suspend fun countSessions(): Int

    @Query("SELECT * FROM workout_sessions WHERE dateEpochDay >= :fromDay ORDER BY dateEpochDay ASC")
    suspend fun getSessionsSince(fromDay: Long): List<WorkoutSessionEntity>

    @Query("SELECT * FROM workout_sessions ORDER BY dateEpochDay ASC")
    suspend fun getAllSessionsOnce(): List<WorkoutSessionEntity>
}
