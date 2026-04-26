package com.solufast.leanlog.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_metrics")
data class BodyMetricEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val weightKg: Double?,
    val waistCm: Double?,
    val neckCm: Double?,
    val bodyFatPercent: Double?
)
