package com.solufast.leanlog.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val title: String?,
    val totalCalories: Int?,
    val cardioMinutes: Int?,
    val notes: String?
)
