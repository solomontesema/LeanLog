package com.solufast.leanlog.data

import com.solufast.leanlog.data.db.entity.BodyMetricEntity
import com.solufast.leanlog.data.db.entity.ExerciseEntryEntity
import com.solufast.leanlog.data.db.entity.WorkoutSessionEntity
import com.solufast.leanlog.data.repository.BodyMetricRepository
import com.solufast.leanlog.data.repository.WorkoutRepository
import java.time.LocalDate

// Representative sessions from the April 2026 log, inserted on first launch.
suspend fun seedIfEmpty(
    workoutRepo: WorkoutRepository,
    bodyMetricRepo: BodyMetricRepository
) {
    if (workoutRepo.countSessions() > 0) return

    fun dayOf(year: Int, month: Int, day: Int): Long =
        LocalDate.of(year, month, day).toEpochDay()

    // 12 April 2026
    workoutRepo.insertSession(
        WorkoutSessionEntity(
            dateEpochDay = dayOf(2026, 4, 12),
            title = "Full Body",
            totalCalories = 765,
            cardioMinutes = 15,
            notes = "Indoor cycling 15 min"
        ),
        listOf(
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Arm Extension", weightKg = 35.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Shoulder Press", weightKg = 15.0, sets = 3, reps = 8, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Chest Press", weightKg = 30.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Linear AR Leg Press", weightKg = 120.0, sets = 4, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Seated Row", weightKg = 40.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Lat Pulldown", weightKg = 40.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Tricep Pull-down", weightKg = 15.0, sets = 4, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Iso Lateral High Row", weightKg = 50.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Leg Curl", weightKg = 30.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Leg Extension", weightKg = 30.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Arm Curl", weightKg = 20.0, sets = 3, reps = 12, difficulty = null)
        )
    )

    // 15 April 2026
    workoutRepo.insertSession(
        WorkoutSessionEntity(
            dateEpochDay = dayOf(2026, 4, 15),
            title = "Chest + Legs",
            totalCalories = 745,
            cardioMinutes = 35,
            notes = "Elliptical 15 min + indoor run 20 min"
        ),
        listOf(
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Elliptical", weightKg = null, sets = null, reps = null, difficulty = "15 min, 157 kcal"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Pectoral", weightKg = 35.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Arm Extension", weightKg = 40.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Shoulder Press", weightKg = 20.0, sets = 3, reps = 8, difficulty = "very difficult"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Hack Squat", weightKg = 80.0, sets = 3, reps = 10, difficulty = "difficult"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Seated Calf", weightKg = 20.0, sets = 3, reps = 15, difficulty = "medium"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Bench Press Dual", weightKg = 10.0, sets = 3, reps = 10, difficulty = "super difficult"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Lat Pulldown", weightKg = 60.0, sets = 3, reps = 10, difficulty = null)
        )
    )

    // 17 April 2026
    workoutRepo.insertSession(
        WorkoutSessionEntity(
            dateEpochDay = dayOf(2026, 4, 17),
            title = "Legs + Core",
            totalCalories = 785,
            cardioMinutes = 29,
            notes = "Stair climbing 14 min + inclined walk 15 min"
        ),
        listOf(
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Stair Climbing", weightKg = null, sets = null, reps = null, difficulty = "14 min, 177 kcal"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Arm Extension", weightKg = 45.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Shoulder Press", weightKg = 20.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "AB Crunch", weightKg = 10.0, sets = 3, reps = 30, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Linear AR Leg Press", weightKg = 140.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Rear Kick", weightKg = 50.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Lat Pulldown", weightKg = 40.0, sets = 3, reps = 5, difficulty = null)
        )
    )

    // 18 April 2026
    workoutRepo.insertSession(
        WorkoutSessionEntity(
            dateEpochDay = dayOf(2026, 4, 18),
            title = "Upper Body + Cardio",
            totalCalories = 970,
            cardioMinutes = 30,
            notes = "Running 10 min + brisk walk 20 min"
        ),
        listOf(
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Running", weightKg = null, sets = null, reps = null, difficulty = "10 min, 115 kcal"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Chest Press", weightKg = 35.0, sets = 4, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Reverse Fly", weightKg = 5.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Pectoral", weightKg = 15.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Abductor", weightKg = 65.0, sets = 3, reps = 15, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Leg Curl", weightKg = 35.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Arm Curl", weightKg = 20.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "AB Crunch", weightKg = 10.0, sets = 3, reps = 30, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Rear Kick", weightKg = 70.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Iso Lateral High Row", weightKg = 60.0, sets = 5, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Standing Chest Press", weightKg = 30.0, sets = 3, reps = 15, difficulty = null)
        )
    )

    // 20 April 2026
    workoutRepo.insertSession(
        WorkoutSessionEntity(
            dateEpochDay = dayOf(2026, 4, 20),
            title = "Full Body",
            totalCalories = 970,
            cardioMinutes = 30,
            notes = "Elliptical 20 min + stair climbing 10 min"
        ),
        listOf(
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Elliptical", weightKg = null, sets = null, reps = null, difficulty = "20 min, 200 kcal"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Pectoral", weightKg = 30.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Arm Extension", weightKg = 50.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Shoulder Press", weightKg = 20.0, sets = 4, reps = 8, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Linear AR Leg Press", weightKg = 140.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Rear Kick", weightKg = 70.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Standing Chest Press", weightKg = 40.0, sets = 5, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Lateral Pull (Rowing)", weightKg = 40.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Stair Climbing", weightKg = null, sets = null, reps = null, difficulty = "10 min, 120 kcal")
        )
    )

    // 24 April 2026
    workoutRepo.insertSession(
        WorkoutSessionEntity(
            dateEpochDay = dayOf(2026, 4, 24),
            title = "Upper + Cardio",
            totalCalories = 1000,
            cardioMinutes = 35,
            notes = "Elliptical 15 min + running 20 min"
        ),
        listOf(
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Elliptical", weightKg = null, sets = null, reps = null, difficulty = "15 min, 180 kcal"),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Pectoral", weightKg = 35.0, sets = 3, reps = 12, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Reverse Fly", weightKg = 10.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Vertical Leg Press", weightKg = 80.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Seated Calf", weightKg = 15.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Iso Lateral High Row", weightKg = 60.0, sets = 5, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Iso Lateral Shoulder Press", weightKg = 40.0, sets = 3, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Standing Chest Press", weightKg = 40.0, sets = 4, reps = 10, difficulty = null),
            ExerciseEntryEntity(sessionId = 0, exerciseName = "Running", weightKg = null, sets = null, reps = null, difficulty = "20 min, 215 kcal")
        )
    )

    // Initial body metric
    if (bodyMetricRepo.countMetrics() == 0) {
        bodyMetricRepo.insertMetric(
            BodyMetricEntity(
                dateEpochDay = dayOf(2026, 4, 12),
                weightKg = 73.5,
                waistCm = 95.0,
                neckCm = 39.0,
                bodyFatPercent = 24.5
            )
        )
    }
}
