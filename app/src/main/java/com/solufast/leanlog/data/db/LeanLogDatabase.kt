package com.solufast.leanlog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.solufast.leanlog.data.db.dao.BodyMetricDao
import com.solufast.leanlog.data.db.dao.WorkoutDao
import com.solufast.leanlog.data.db.entity.BodyMetricEntity
import com.solufast.leanlog.data.db.entity.ExerciseEntryEntity
import com.solufast.leanlog.data.db.entity.WorkoutSessionEntity

@Database(
    entities = [WorkoutSessionEntity::class, ExerciseEntryEntity::class, BodyMetricEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LeanLogDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
    abstract fun bodyMetricDao(): BodyMetricDao

    companion object {
        @Volatile
        private var instance: LeanLogDatabase? = null

        fun getInstance(context: Context): LeanLogDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    LeanLogDatabase::class.java,
                    "leanlog.db"
                ).build().also { instance = it }
            }
        }
    }
}
