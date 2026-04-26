package com.solufast.leanlog.data.repository

import android.content.Context
import com.solufast.leanlog.data.db.LeanLogDatabase
import com.solufast.leanlog.data.db.entity.BodyMetricEntity
import kotlinx.coroutines.flow.Flow

class BodyMetricRepository(context: Context) {

    private val dao = LeanLogDatabase.getInstance(context).bodyMetricDao()

    suspend fun insertMetric(metric: BodyMetricEntity) = dao.insertMetric(metric)

    fun getAllMetrics(): Flow<List<BodyMetricEntity>> = dao.getAllMetrics()

    suspend fun getLatestMetric(): BodyMetricEntity? = dao.getLatestMetric()

    suspend fun countMetrics(): Int = dao.countMetrics()

    suspend fun getAllMetricsOnce(): List<BodyMetricEntity> = dao.getAllMetricsOnce()
}
