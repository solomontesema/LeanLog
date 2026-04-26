package com.solufast.leanlog.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.solufast.leanlog.data.db.entity.BodyMetricEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyMetricDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: BodyMetricEntity)

    @Query("SELECT * FROM body_metrics ORDER BY dateEpochDay DESC")
    fun getAllMetrics(): Flow<List<BodyMetricEntity>>

    @Query("SELECT * FROM body_metrics ORDER BY dateEpochDay DESC LIMIT 1")
    suspend fun getLatestMetric(): BodyMetricEntity?

    @Query("SELECT COUNT(*) FROM body_metrics")
    suspend fun countMetrics(): Int

    @Query("SELECT * FROM body_metrics ORDER BY dateEpochDay ASC")
    suspend fun getAllMetricsOnce(): List<BodyMetricEntity>
}
